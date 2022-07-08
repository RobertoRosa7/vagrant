package service.order;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import common.kafka.CorrelationId;
import common.kafka.Email;
import common.kafka.dispatcher.KafkaDispatcher;

public class NewOrderMain {
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    try (var orderDispatcher = new KafkaDispatcher<Order>()) {
      var userEmail = "user" + NewOrderMain.getRandomIntNumber(1, 6000) + "@gmail.com";

      for (var i = 0; i < 5; i++) {
        var orderId = UUID.randomUUID().toString();
        var amount = new BigDecimal(Math.random() * 5000 + 1);
        var order = new Order(orderId, amount, userEmail);
        var corrId = new CorrelationId(NewOrderMain.class.getSimpleName());

        orderDispatcher.send("ECOMMERCE_NEW_ORDER", userEmail, order, corrId);
      }

    }
  }

  public static int getRandomIntNumber(int min, int max) {
    return (int) (Math.random() * (max - min) + min);
  }
}
