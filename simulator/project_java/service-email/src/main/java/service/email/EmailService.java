package service.email;

import common.kafka.Message;
import common.kafka.consumer.ConsumerService;
import common.kafka.consumer.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService implements ConsumerService<String> {
  private final String topic = "ECOMMERCE_SEND_EMAIL";

  public static void main(String[] args) {
    new ServiceRunner<>(EmailService::new).start(5);
  }

  public String getTopic() {
    return this.topic;
  }

  public String getConsumerGroup() {
    return EmailService.class.getSimpleName();
  }

  public void parser(ConsumerRecord<String, Message<String>> record) {
    Message<String> message = record.value();

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
