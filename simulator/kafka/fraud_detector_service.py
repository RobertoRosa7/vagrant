import logging
from kafka import KafkaConsumer
import time

class FraudDetectorService:

  def __initi__(self):
    pass

  def consumer(self):
    consumer = KafkaConsumer('ECOMMERCE_NEW_ORDER', 
                              bootstrap_servers=["localhost:9092"],
                              group_id=FraudDetectorService.__name__)
    
    for record in consumer:
      # record value and key are raw bytes -- decode if necessary!
      # e.g., for unicode: `record.value.decode('utf-8')`
      print("-----------------------------------------");
      print("Processing new order, checking for fraud");
      print(record.key.decode('utf-8'));
      print(record.value.decode('utf-8'));
      print(record.partition);
      print(record.offset);
      print("Order proccessed");

FraudDetectorService().consumer()