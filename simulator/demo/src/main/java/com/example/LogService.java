package com.example;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class LogService {
  public static void main(String[] args) {
    var consumer = new KafkaConsumer<String, String>(properties());
    consumer.subscribe(Pattern.compile("ECOMMERCE.*"));
    while (true) {
      var records = consumer.poll(Duration.ofMillis(100));

      if (!records.isEmpty()) {
        System.out.println("LOG " + records.count() + " registros");
        for (var record : records) {
          System.out.println("-----------------------------------------");
          System.out.println("LOG: " + record.topic());
          System.out.println("Key " + record.key());
          System.out.println("Value " + record.value());
          System.out.println("Partition " + record.partition());
          System.out.println("Offset " + record.offset());

          try {
            Thread.sleep(1000);
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
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, LogService.class.getSimpleName());
    return properties;
  }
}
