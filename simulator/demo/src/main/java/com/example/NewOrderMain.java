package com.example;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
  public static void main(String[] args) throws InterruptedException, ExecutionException {

    try (var producer = new KafkaDispatcher()) {
      for (var i = 0; i < 10; i++) {
        var key = UUID.randomUUID().toString();
        var value = key + ",134234";
        producer.send("ECOMMERCE_NEW_ORDER", key, value);
        producer.send("ECOMMERCE_SEND_EMAIL", key, value);
      }
    }
  }
}
