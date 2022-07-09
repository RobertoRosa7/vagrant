package common.kafka.consumer;

import common.kafka.Message;
import common.kafka.dispatcher.GsonSerializer;
import common.kafka.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {
  private final KafkaConsumer<String, Message<T>> consumer;
  private final ConsumerFunction<T> parser;

  public KafkaService(String groupId, String topic, ConsumerFunction<T> parser,
                      Map<String, String> properties) {
    this(groupId, parser, properties);
    this.consumer.subscribe(Collections.singletonList(topic));
  }

  public KafkaService(String groupId, Pattern pattern, ConsumerFunction<T> parser,
                      Map<String, String> properties) {
    this(groupId, parser, properties);
    this.consumer.subscribe(pattern);
  }

  private KafkaService(String groupId, ConsumerFunction<T> parser, Map<String, String> properties) {
    this.parser = parser;
    this.consumer = new KafkaConsumer<>(getProperties(groupId, properties));
  }

  public void run() throws InterruptedException, ExecutionException {
    try (var deadLetter = new KafkaDispatcher<>()) {
      while (true) {
        ConsumerRecords<String, Message<T>> records = this.consumer.poll(Duration.ofMillis(100));

        if (!records.isEmpty()) {
          System.out.println("Founded " + records.count() + " registers");
          for (var record : records) {
            try {
              this.parser.consume(record);
            } catch (Exception e) {
              // only catches exception because no matter which Exception
              // I want to recover and parse the next one
              // so far, just logging the exception for this message
              e.printStackTrace();
              Message<T> message = record.value();
              deadLetter.send("ECOMMERCE_DEADLETTER", message.getId().toString(),
                  new GsonSerializer().serialize("", message),
                  message.getId().continueWith("DeadLetter"));
            }
          }
        }
      }
    }
  }

  @Override
  public void close() {
    this.consumer.close();
  }

  private Properties getProperties(String groupId, Map<String, ?> props) {
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.25:9092");
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    properties.putAll(props);
    return properties;
  }
}
