package service.frauddetector;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import common.kafka.GsonDeserializer;
import common.kafka.KafkaService;
import com.example.Order;

public class FraudDetectorService1 {
  private final String topic = "ECOMMERCE_NEW_ORDER";

  public static void main(String[] args) {
    var fraudDetectorService1 = new FraudDetectorService1();
    try (var service = new KafkaService<>(
        FraudDetectorService1.class.getSimpleName(),
        fraudDetectorService1.topic,
        fraudDetectorService1::parser,
        Order.class,
        Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName()))) {
      service.run();
    }
  }

  private void parser(ConsumerRecord<String, Order> record) {
    System.out.println("-----------------------------------------");
    System.out.println("Processing new order, checking for fraud");
    System.out.println("Key " + record.key());
    System.out.println("Value " + record.value());
    System.out.println("Partition " + record.partition());
    System.out.println("Offset " + record.offset());

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Order proccessed");
  }
}
