package common.kafka.consumer;

public interface ServiceFactory<T> {
  ConsumerService<T> create();
}
