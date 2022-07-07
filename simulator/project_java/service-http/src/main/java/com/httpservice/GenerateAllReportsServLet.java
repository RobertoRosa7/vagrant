package com.httpservice;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.kafka.CorrelationId;
import common.kafka.KafkaDispatcher;

public class GenerateAllReportsServLet extends HttpServlet {
  private final KafkaDispatcher<String> batchDispatcher = new KafkaDispatcher<>();

  @Override
  public void destroy() {
    super.destroy();
    this.batchDispatcher.close();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    try {
      batchDispatcher.send("ECOMMERCE_SEND_MESSAGE_TO_ALL_USERS", "ECOMMERCE_USER_GENERATE_READING_REPORT",
          "ECOMMERCE_USER_GENERATE_READING_REPORT", new CorrelationId(GenerateAllReportsServLet.class.getSimpleName()));

      resp.setStatus(HttpServletResponse.SC_OK);
      resp.getWriter().println("Report request generate success");

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
