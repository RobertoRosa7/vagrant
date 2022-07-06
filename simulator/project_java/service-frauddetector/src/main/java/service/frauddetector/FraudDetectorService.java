package service.frauddetector;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import common.kafka.KafkaDispatcher;
import common.kafka.KafkaService;
import common.kafka.Message;

public class FraudDetectorService {
  private final String topic = "ECOMMERCE_NEW_ORDER";
  private final KafkaDispatcher<Order> dispatcher = new KafkaDispatcher<>();

  public static void main(String[] args) {
    var fraud = new FraudDetectorService();

    try (var service = new KafkaService<>(
        FraudDetectorService.class.getSimpleName(),
        fraud.topic,
        fraud::parser,
        Order.class,
        Map.of())) {
      service.run();
    }
  }

  private void parser(ConsumerRecord<String, Message<Order>> record) throws InterruptedException, ExecutionException {
    var message = record.value();
    System.out.println("-----------------------------------------");
    System.out.println("Processing new order, checking for fraud");
    System.out.println("Key => " + record.key());
    System.out.println("Value => " + message.getPayload());
    System.out.println("Partition => " + record.partition());
    System.out.println("Offset => " + record.offset());

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    var order = message.getPayload();

    if (isFraud(order)) {
      dispatcher.send("ECOMMERCE_ORDER_REJECT", order.getEmail(), order);
      System.out.println("Order is a Fraud!!!!! => " + order.getAmount());
    } else {
      dispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getEmail(), order);
      System.out.println("Order APRROVED =>  " + order.getAmount());
    }

  }

  // pretending that the fraud happens when the amount is >= 4500
  private boolean isFraud(Order order) {
    return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
  }
}
