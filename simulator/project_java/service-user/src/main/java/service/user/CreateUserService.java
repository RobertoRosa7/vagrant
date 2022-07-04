package service.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import common.kafka.KafkaService;

public class CreateUserService {
  private final String topic = "ECOMMERCE_NEW_ORDER";
  private final Connection connection;

  CreateUserService() throws SQLException {
    String url = "jdbc:sqlite:target/user_database.db";
    this.connection = DriverManager.getConnection(url);
    try {
      this.connection.createStatement().execute("create table Users (" +
          "uuid varchar(200) primary key," +
          "email varchar(200))");
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  public static void main(String[] args) throws SQLException {
    var user = new CreateUserService();

    try (var service = new KafkaService<>(
        CreateUserService.class.getSimpleName(),
        user.topic,
        user::parser,
        Order.class,
        Map.of())) {
      service.run();
    }
  }

  private void parser(ConsumerRecord<String, Order> record) throws SQLException {
    System.out.println("-----------------------------------------");
    System.out.println("Processing new order, checking for new user");
    System.out.println("Value => " + record.value());

    var order = record.value();
    if (this.isNewUser(order.getEmail())) {
      this.insertNewUser(order.getEmail());
    }
  }

  private boolean isNewUser(String email) throws SQLException {
    var exists = this.connection.prepareStatement("select uuid from Users where email = ? limit 1");
    exists.setString(1, email);

    var results = exists.executeQuery();
    return results.next();
  }

  private void insertNewUser(String email) throws SQLException {
    var insert = this.connection.prepareStatement("insert into Users (uuid, email) values (?, ?)");
    insert.setString(1, UUID.randomUUID().toString());
    insert.setString(2, email);
    insert.execute();
    System.out.println("User uuid e " + email + "added");
  }
}
