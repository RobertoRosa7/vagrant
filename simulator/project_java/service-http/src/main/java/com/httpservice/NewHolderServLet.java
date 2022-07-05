package com.httpservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.kafka.Email;
import common.kafka.KafkaDispatcher;

public class NewHolderServLet extends HttpServlet {
  private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
  private final KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<>();

  @Override
  public void destroy() {
    super.destroy();
    this.orderDispatcher.close();
    this.emailDispatcher.close();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    try {
      var email = req.getParameter("email");
      var amount = new BigDecimal(req.getParameter("amount"));
      var orderId = UUID.randomUUID().toString();

      var newOrder = new Order(orderId, amount, email);
      var newEmail = new Email("Novo Membro", "Seja Bem vindo");

      this.orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, newOrder);
      this.emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, newEmail);

      System.out.println("New order sent successfully.");
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.getWriter().println("New order was sent");

    } catch (ExecutionException e) {
      throw new ServletException(e);
    } catch (InterruptedException e) {
      throw new ServletException(e);
    } catch (IOException e) {
      throw new ServletException(e);
    }
  }

  public static int getRandomIntNumber(int min, int max) {
    return (int) (Math.random() * (max - min) + min);
  }
}
