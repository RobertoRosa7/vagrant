package common.kafka;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaService<T> implements Closeable {
  private final KafkaConsumer<String, T> consumer;
  private final ConsumerFunction<T> parser;

  public KafkaService(String groupId, String topic, ConsumerFunction<T> parser, Class<T> type,
      Map<String, ?> properties) {
    this(groupId, parser, type, properties);
    this.consumer.subscribe(Collections.singletonList(topic));
  }

  public KafkaService(String groupId, Pattern pattern, ConsumerFunction<T> parser, Class<T> type,
      Map<String, ?> properties) {
    this(groupId, parser, type, properties);
    this.consumer.subscribe(pattern);
  }

  private KafkaService(String groupId, ConsumerFunction<T> parser, Class<T> type, Map<String, ?> properties) {
    this.parser = parser;
    this.consumer = new KafkaConsumer<>(getProperties(type, groupId, properties));
  }

  public void run() {
    while (true) {
      var records = this.consumer.poll(Duration.ofMillis(100));

      if (!records.isEmpty()) {
        System.out.println("Encontrei " + records.count() + " registros");
        for (var record : records) {
          try {
            this.parser.consume(record);
          } catch (Exception e) {
            // only catches exception because no matter which Exception
            // i want to recover and parse the next one
            // so far, just logging the execption for this message
            e.printStackTrace();
        }
      }
    }
  }

  @Override
  public void close() {
    this.consumer.close();
  }

  private Properties getProperties(Class<T> type, String groupId, Map<String, ?> props) {
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.25:9092");
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
    properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
    properties.putAll(props);
    return properties;
  }
}