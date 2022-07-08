package common.kafka.consumer;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ServiceProvider<T> implements Callable<Void> {

  private final ServiceFactory<T> factory;

  public ServiceProvider(ServiceFactory<T> factory) {
    this.factory = factory;
  }

  public Void call() throws ExecutionException, InterruptedException {
    ConsumerService<T> myService = factory.create();

    try (var service = new KafkaService<>(myService.getConsumerGroup(), myService.getTopic(), myService::parser, Map.of())) {
      service.run();
    }

    return null;
  }
}
