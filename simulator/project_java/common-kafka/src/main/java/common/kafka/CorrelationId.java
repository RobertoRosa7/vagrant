package common.kafka;

import java.util.UUID;

public class CorrelationId {
  private final String id;
  private String title;

  public CorrelationId(String title) {
    this.id = this.title + "(" + UUID.randomUUID().toString() + ")";
    this.title = title;
  }

  @Override
  public String toString() {
    return "CorrelationId{" + "id='" + this.id + '\'' + '}';
  }

  public CorrelationId continueWith(String title) {
    return new CorrelationId(this.id + "-" + title);
  }
}
