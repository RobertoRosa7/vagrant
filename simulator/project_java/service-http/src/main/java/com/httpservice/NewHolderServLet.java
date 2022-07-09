package com.httpservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.kafka.CorrelationId;
import common.kafka.Email;
import common.kafka.KafkaDispatcher;

public class NewHolderServLet extends HttpServlet {
  private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
  private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

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
      var orderId = req.getParameter("uuid");
      var corrId = new CorrelationId(NewHolderServLet.class.getSimpleName());
      var newOrder = new Order(orderId, amount, email);

      try (var database = new OrderDatabase();) {
        if (database.saveNew(newOrder)) {
          this.orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, newOrder, corrId);

          resp.setStatus(HttpServletResponse.SC_OK);
          resp.getWriter().println("New order was sent");
          System.out.println("New order sent successfully.");
        } else {
          resp.setStatus(HttpServletResponse.SC_OK);
          resp.getWriter().println("Old order was sent");
          System.out.println("Old order received.");
        }
      }


    } catch (ExecutionException e) {
      throw new ServletException(e);
    } catch (InterruptedException e) {
      throw new ServletException(e);
    } catch (IOException e) {
      throw new ServletException(e);
    } catch (SQLException e) {
      throw new ServletException(e)
    }
  }

  public static int getRandomIntNumber(int min, int max) {
    return (int) (Math.random() * (max - min) + min);
  }
}
