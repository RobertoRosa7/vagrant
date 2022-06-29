package com.example;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class FraudDetectorService {
  public static void main(String[] args) {
    var consumer = new KafkaConsumer<String, String>(properties());
    consumer.subscribe(Collections.singletonList("ECOMMERCE_NEW_ORDER"));
    while (true) {
      var records = consumer.poll(Duration.ofMillis(100));

      if (!records.isEmpty()) {
        System.out.println("Encontrei " + records.count() + " registros");
        for (var record : records) {
          System.out.println("-----------------------------------------");
          System.out.println("Processing new order, checking for fraud");
          System.out.println(record.key());
          System.out.println(record.value());
          System.out.println(record.partition());
          System.out.println(record.offset());

          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println("Order proccessed");
        }
      }
    }
  }

  private static Properties properties() {
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.25:9092");
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());
    return properties;
  }
}
