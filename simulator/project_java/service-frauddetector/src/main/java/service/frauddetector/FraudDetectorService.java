package service.frauddetector;

import common.kafka.CorrelationId;
import common.kafka.Message;
import common.kafka.consumer.ConsumerService;
import common.kafka.consumer.ServiceRunner;
import common.kafka.dispatcher.KafkaDispatcher;
import database.LocalDatabase;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService implements ConsumerService<Order> {
  private final String topic = "ECOMMERCE_NEW_ORDER";
  private final KafkaDispatcher<Order> dispatcher = new KafkaDispatcher<>();

  private final LocalDatabase database;

  public FraudDetectorService() throws SQLException {
    this.database = new LocalDatabase("frauds_database");
    this.database.createIfNotExist("create table Orders (" + "uuid varchar(200) primary key," + "is_fraud boolean)");
  }

  public static void main(String[] args) {
    new ServiceRunner<>(FraudDetectorService::new).start(1);
  }

  public void parser(ConsumerRecord<String, Message<Order>> record) throws InterruptedException, ExecutionException, SQLException {
    Message<Order> message = record.value();
    Order order = message.getPayload();
    CorrelationId corrId = message.getId().continueWith(FraudDetectorService.class.getSimpleName());
    if (wasProcessed(order)) {
      System.out.println("Order (" + order.getOrderId() + ") was already processced");
      return;
    }
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


    if (isFraud(order)) {
      this.database.update("insert into Orders (uuid, is_fraud) values (?, true)", order.getOrderId());
      dispatcher.send("ECOMMERCE_ORDER_REJECT", order.getEmail(), order, corrId);
      System.out.println("Order is a Fraud!!!!! => " + order.getAmount());
    } else {
      this.database.update("insert into Orders (uuid, is_fraud) values (?, false)", order.getOrderId());
      dispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getEmail(), order, corrId);
      System.out.println("Order APRROVED =>  " + order.getAmount());
    }

  }

  private boolean wasProcessed(Order order) throws SQLException {
    ResultSet results = this.database.query("select uuid from Orders where uuid = ? limit 1", order.getOrderId());
    return results.next();
  }

  @Override
  public String getTopic() {
    return this.topic;
  }

  @Override
  public String getConsumerGroup() {
    return FraudDetectorService.class.getSimpleName();
  }

  // pretending that the fraud happens when the amount is >= 4500
  private boolean isFraud(Order order) {
    return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
  }
}
