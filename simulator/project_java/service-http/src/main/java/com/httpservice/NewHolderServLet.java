package com.httpservice;

import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.kafka.Email;
import common.kafka.KafkaDispatcher;

public class NewHolderServLet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    try (var orderDispatcher = new KafkaDispatcher<Order>()) {
      try (var emailDispatcher = new KafkaDispatcher<Email>()) {
        try {
          var userEmail = "user" + NewHolderServLet.getRandomIntNumber(1, 6000) + "@gmail.com";
          var orderId = UUID.randomUUID().toString();
          var amount = new BigDecimal(Math.random() * 5000 + 1);

          var order = new Order(orderId, amount, userEmail);
          var email = new Email("Novo Membro", "Seja Bem vindo");

          orderDispatcher.send("ECOMMERCE_NEW_ORDER", userEmail, order);
          emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userEmail, email);
        } catch (ExecutionException e) {
          throw new ServletException(e);
        } catch (InterruptedException e) {
          throw new ServletException(e);
        }
      }
    }
  }

  public static int getRandomIntNumber(int min, int max) {
    return (int) (Math.random() * (max - min) + min);
  }
}
