package service.user;

import java.math.BigDecimal;

public class Order {
  private final String userId, orderId;
  private final BigDecimal amount;

  public Order(String userId, String orderId, BigDecimal amount) {
    this.userId = userId;
    this.orderId = orderId;
    this.amount = amount;
  }

  public BigDecimal getAmount() {
    return this.amount;
  }

  public String getUserId() {
    return this.userId;
  }

  public String getOrderId() {
    return this.orderId;
  }

  public String getEmail() {
    return "";
  }
}