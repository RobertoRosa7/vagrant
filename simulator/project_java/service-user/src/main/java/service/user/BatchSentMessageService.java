package service.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import common.kafka.KafkaDispatcher;
import common.kafka.KafkaService;
import common.kafka.Message;

public class BatchSentMessageService {
  private final String topic = "ECOMMERCE_SENT_MESSAGE_TO_ALL_USERS";
  private final Connection connection;
  private final KafkaDispatcher<User> userDispatcher = new KafkaDispatcher<>();

  public BatchSentMessageService() throws SQLException {
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

  public static void main(String[] args) throws InterruptedException, ExecutionException, SQLException {
    var batchService = new BatchSentMessageService();

    try (var service = new KafkaService<>(
        BatchSentMessageService.class.getSimpleName(),
        batchService.topic,
        batchService::parser,
        Map.of())) {
      service.run();
    }
  }

  private void parser(ConsumerRecord<String, Message<String>> record) throws SQLException, Exception {
    System.out.println("-----------------------------------------");
    System.out.println("Processing new batch");
    var message = record.value();
    System.out.println("Topic => " + message.getPayload());

    for (User user : this.getAllUsers()) {
      this.userDispatcher.sendAsync(message.getPayload(), user.getUserUuid(), user,
          message.getId().continueWith(BatchSentMessageService.class.getSimpleName()));
    }

  }

  private List<User> getAllUsers() throws SQLException, Exception {
    var results = connection.prepareStatement("select uuid from Users").executeQuery();
    List<User> users = new ArrayList<>();

    while (results.next()) {
      users.add(new User(results.getString(1)));
    }

    return users;
  }

}
