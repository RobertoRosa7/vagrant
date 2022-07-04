package common.kafka;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaDispatcher<T> implements Closeable {
  private final KafkaProducer<String, T> producer;

  public KafkaDispatcher() {
    this.producer = new KafkaProducer<>(properties());
  }

  public void send(String topic, String key, T value) throws InterruptedException, ExecutionException {
    var record = new ProducerRecord<>(topic, key, value);
    Callback callback = (RecordMetadata metadata, Exception e) -> {
      if (e != null) {
        e.printStackTrace();
        return;
      }
      System.out
          .println(
              metadata.topic() + ":::partition" + metadata.partition() + "/offeset" + metadata.offset() + "/timestamp"
                  + metadata.timestamp());
    };

    this.producer.send(record, callback).get();
  }

  @Override
  public void close() {
    this.producer.close();
  }

  private static Properties properties() {
    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.25:9092");
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
    return properties;
  }
}
