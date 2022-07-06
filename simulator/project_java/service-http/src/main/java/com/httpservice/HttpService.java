package com.httpservice;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpService {
  public static void main(String[] args) throws Exception {
    System.out.println("Http Server is up");
    var server = new Server(8080);
    var context = new ServletContextHandler();

    context.setContextPath("/");
    context.addServlet(new ServletHolder(new NewHolderServLet()), "/new");
    context.addServlet(new ServletHolder(new GenerateAllReportsServLet()), "/admin/generate-reports");
    server.setHandler(context);

    server.start();
    server.join();
  }
}