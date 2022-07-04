package service.order;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import common.kafka.Email;
import common.kafka.KafkaDispatcher;

public class NewOrderMain {
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    try (var orderDispatcher = new KafkaDispatcher<Order>()) {
      try (var emailDispatcher = new KafkaDispatcher<Email>()) {
        var userEmail = "user" + NewOrderMain.getRandomIntNumber(1, 6000) + "@gmail.com";

        for (var i = 0; i < 5; i++) {
          var orderId = UUID.randomUUID().toString();
          var amount = new BigDecimal(Math.random() * 5000 + 1);

          var order = new Order(orderId, amount, userEmail);
          var email = new Email("Novo Membro", "Seja Bem vindo");

          orderDispatcher.send("ECOMMERCE_NEW_ORDER", userEmail, order);
          emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userEmail, email);
        }
      }
    }
  }

  public static int getRandomIntNumber(int min, int max) {
    return (int) (Math.random() * (max - min) + min);
  }
}
