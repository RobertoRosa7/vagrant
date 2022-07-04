package service.frauddetector;

import java.math.BigDecimal;

public class Order {
  private final String orderId, email;
  private final BigDecimal amount;

  public Order(String orderId, BigDecimal amount, String email) {
    this.orderId = orderId;
    this.amount = amount;
    this.email = email;
  }

  public BigDecimal getAmount() {
    return this.amount;
  }

  public String getOrderId() {
    return this.orderId;
  }

  public String getEmail() {
    return this.email;
  }

  @Override
  public String toString() {
    return "Order{" + "orderId='" + orderId + '\'' + ", amount=" + amount + ", email='" + email + '\'' + '}';
  }
}