import os
import sys
import uuid

from kafka_dispatcher import KafkaDispatcher

sys.path.append(os.path.abspath(os.getcwd()))

class NewOrderMain:
  def __init__(self):
    kafkaDispatcher = KafkaDispatcher()
    try:
      new_order = {'key': str(uuid.uuid4()), 'value': 'teste'}
      new_email = {'key': str(uuid.uuid4()), 'value': 'sendo email'}
      kafkaDispatcher.send('ECOMMERCE_NEW_ORDER', key=new_order['key'], value=new_order['value'])
      kafkaDispatcher.send('ECOMMERCE_SEND_EMAIL', key=new_email['key'], value=new_email['value'])
    except Exception as e:
      kafkaDispatcher.close(e)

NewOrderMain()
