package service.log;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import common.kafka.KafkaService;
import common.kafka.Message;

public class LogService {
  public static void main(String[] args) {
    var logService = new LogService();
    try (var service = new KafkaService<>(
        LogService.class.getSimpleName(), Pattern.compile("ECOMMERCE.*"),
        logService::parser,
        String.class,
        Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()))) {
      service.run();
    }
  }

  private void parser(ConsumerRecord<String, Message<String>> record) {
    var message = record.value();
    System.out.println("-----------------------------------------");
    // System.out.println("LOG => " + record.topic());
    // System.out.println("Key => " + record.key());
    System.out.println("Value => " + message.getPayload());
    // System.out.println("Partition =>" + record.partition());
    // System.out.println("Offset =>" + record.offset());

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Order proccessed");
  }
}
