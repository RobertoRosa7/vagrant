package common.kafka.consumer;

import common.kafka.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerService<T> {
  void parser(ConsumerRecord<String, Message<T>> record) throws Exception;

  String getTopic();

  String getConsumerGroup();
}
