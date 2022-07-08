package common.kafka.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceRunner<T> {
  private final ServiceProvider<T> provider;

  public ServiceRunner(ServiceFactory<T> factory) {
    this.provider = new ServiceProvider<>(factory);
  }

  public void start(int THREADS) {
    ExecutorService pool = Executors.newFixedThreadPool(THREADS);

    for (int i = 0; i <= THREADS; i++) {
      pool.submit(this.provider);
    }
  }
}
