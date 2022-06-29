from kafka import KafkaProducer, KafkaConsumer

class NewOrderMain:

  def __initi__(self):
    pass
  
  def main(self):
    new_order = {'key': '1234', 'value': 'teste'}
    new_email = {'key': 'email', 'value': 'sendo email'}

    producer = KafkaProducer(bootstrap_servers=["localhost:9092"], key_serializer=str.encode, value_serializer=str.encode, api_version=(1, 0, 0))
    futureOrder = producer.send('ECOMMERCE_NEW_ORDER', key=new_order['key'], value=new_order['value'])
    futureEmail = producer.send('ECOMMERCE_SEND_EMAIL', key=new_email['key'], value=new_email['value'])

    recordOrder = futureOrder.get(timeout=60)
    recordEmail = futureEmail.get(timeout=60)

    self.on_sucess(recordOrder)
    self.on_sucess(recordEmail)

  def on_sucess(self, record):
    print("Topic => {}".format(record.topic))
    print("Partition => {}".format(record.partition))
    print("Offset => {}".format(record.offset))
    print("TimeStamp => {}".format(record.timestamp))

NewOrderMain().main()