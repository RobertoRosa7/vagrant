package common.kafka;

public class Message<T> {
  private final CorrelationId id;
  private final T payload;

  Message(CorrelationId id, T payload) {
    this.id = id;
    this.payload = payload;
  }

  @Override
  public String toString() {
    return "Message{" + "id='" + this.id + '\'' + ", payload='" + this.payload + '\'' + '}';
  }

  public T getPayload() {
    return this.payload;
  }

  public CorrelationId getId() {
    return this.id;
  }
}
