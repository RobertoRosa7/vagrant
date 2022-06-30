package com.example;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaService implements Closeable {
  private final KafkaConsumer<String, String> consumer;
  private final ConsumerFunction parser;

  public KafkaService(String groupId, String topic, ConsumerFunction parser) {
    this.parser = parser;
    this.consumer = new KafkaConsumer<>(properties(groupId));
    this.consumer.subscribe(Collections.singletonList(topic));
  }

  public void run() {
    while (true) {
      var records = this.consumer.poll(Duration.ofMillis(100));

      if (!records.isEmpty()) {
        System.out.println("Encontrei " + records.count() + " registros");
        for (var record : records) {
          parser.consume(record);
        }
      }
    }
  }

  @Override
  public void close() {
    this.consumer.close();
  }

  private static Properties properties(String groupId) {
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.25:9092");
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
    return properties;
  }
}
