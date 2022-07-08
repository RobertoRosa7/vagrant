package email;

import common.kafka.Message;
import common.kafka.consumer.ConsumerService;
import common.kafka.consumer.ServiceRunner;
import common.kafka.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;

public class EmailNewOrderService implements ConsumerService<Order> {
  private final String topic = "ECOMMERCE_NEW_ORDER";
  private final KafkaDispatcher<String> dispatcher = new KafkaDispatcher<>();

  private final String className = EmailNewOrderService.class.getSimpleName();

  public static void main(String[] args) {
    new ServiceRunner<>(EmailNewOrderService::new).start(1);
  }

  public void parser(ConsumerRecord<String, Message<Order>> record) throws InterruptedException, ExecutionException {
    Message<Order> message = record.value();
    System.out.println("-----------------------------------------");
    System.out.println("Processing new email, preparing...");

    String emailCode = "Thank you for your order";
    Order order = record.value().getPayload();
    String payload = "Thank you for your order!";
    this.dispatcher.send(this.getTopic(), order.getEmail(), payload, record.value().getId().continueWith(this.getClassName()));
  }

  @Override
  public String getTopic() {
    return this.topic;
  }

  @Override
  public String getConsumerGroup() {
    return this.getClassName();
  }

  private String getClassName() {
    return this.className;
  }
}
