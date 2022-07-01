package service.email;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import common.kafka.KafkaService;

public class EmailService {
  private final String topic = "ECOMMERCE_SEND_EMAIL";

  public static void main(String[] args) {
    var emailService = new EmailService();
    try (var service = new KafkaService<>(
        EmailService.class.getSimpleName(),
        emailService.topic,
        emailService::parser,
        String.class,
        Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()))) {
      service.run();
    }
  }

  private void parser(ConsumerRecord<String, String> record) {
    System.out.println("-----------------------------------------");
    System.out.println("Sending email");
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
