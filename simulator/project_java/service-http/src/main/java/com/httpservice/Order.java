package com.httpservice;

import java.math.BigDecimal;

public class Order {
  private final String orderId, email;
  private final BigDecimal amount;

  public Order(String orderId, BigDecimal amount, String email) {
    this.orderId = orderId;
    this.amount = amount;
    this.email = email;
  }

  public String getOrderId() {

  }
}
