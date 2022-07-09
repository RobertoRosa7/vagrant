package service.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import common.kafka.KafkaService;
import common.kafka.Message;

public class CreateUserService implements ConsumerService<Order> {
  private final String topic = "ECOMMERCE_NEW_ORDER";
  private final LocalDatabase database;

  public CreateUserService() throws SQLException {
    this.database = new LocalDatabase("user_database");
    this.database.createIfNotExist("create table Users (" + "uuid varchar(200) primary key," + "email varchar(200))");
  }

  public static void main(String[] args) {
    new ServiceRunner<>(CreateUserService::new).start(1);
  }

  private void parser(ConsumerRecord<String, Message<Order>> record) throws SQLException {
    var message = record.value();

    System.out.println("-----------------------------------------");
    System.out.println("Processing new order, checking for new user");
    System.out.println("Value => " + record.value());

    if (this.isNewUser(message.getPayload().getEmail())) {
      this.insertNewUser(message.getPayload().getEmail());
    }
  }

  public boolean isNewUser(String email) throws SQLException {
    ResultSet results = this.database.query("select uuid from Users where email = ? limit 1");
    return !results.next();
  }

  public void insertNewUser(String email) throws SQLException {
    String uuid = UUID.randomUUID().toString();
    this.database.update("insert into Users (uuid, email) values (?, ?)", uuid, email);
    System.out.println("User uuid e " + email + "added");
  }

  @override
  public String getTopic() {
    return this.topic;
  }

  @override
  public String getConsumerGroup() {
    return CreateUserService.class.getSimpleNam();
  }
}
