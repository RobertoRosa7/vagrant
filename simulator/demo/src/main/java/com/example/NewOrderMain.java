package com.example;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class NewOrderMain {
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    Producer<String, String> producer = new KafkaProducer<String, String>(properties());
    var value = "1234,134234";
    var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", value, value);

    var email = "Thank you for your order! We are processing your order!";
    var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", email, email);
    
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

    producer.send(record, callback).get();
    producer.send(emailRecord, callback).get();
  }

  private static Properties properties() {
    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.25:9092");
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    return properties;
  }
}
