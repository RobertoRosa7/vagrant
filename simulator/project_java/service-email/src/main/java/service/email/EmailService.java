package service.email;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import common.kafka.consumer.KafkaService;
import common.kafka.Message;

public class EmailService {
  private final String topic = "ECOMMERCE_SEND_EMAIL";

  public static void main(String[] args) throws InterruptedException, ExecutionException{
    var emailService = new EmailService();

    try (var service = new KafkaService<>(
        EmailService.class.getSimpleName(),
        emailService.topic,
        emailService::parser,
        Map.of())) {
      service.run();
    }
  }

  private void parser(ConsumerRecord<String, Message<String>> record) {
    var message = record.value();

    System.out.println("-----------------------------------------");
    System.out.println("Sending email");
    System.out.println("Key => " + record.key());
    System.out.println("Value =>" + message.getPayload());
    System.out.println("Partition =>" + record.partition());
    System.out.println("Offset =>" + record.offset());

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Order proccessed");
  }
}
