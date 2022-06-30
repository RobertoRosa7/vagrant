from kafka import KafkaProducer

class KafkaDispatcher:
  producer = None

  def __init__(self):
    self.producer = KafkaProducer(bootstrap_servers=self.properties()['bootstrap_servers'],
                            key_serializer=self.properties()['key_serializer'], 
                            value_serializer=self.properties()['value_serializer'],
                            api_version=self.properties()['api_version'])

  def send(self, topic, key, value):
    future = self.producer.send(topic, key, value)
    result = future.get(timeout=60)
    self.on_sucess(result)


  def on_sucess(self, record):
    print("Topic => {}".format(record.topic))
    print("Partition => {}".format(record.partition))
    print("Offset => {}".format(record.offset))
    print("TimeStamp => {}".format(record.timestamp))


  def properties(self):
    properties = {}
    properties['bootstrap_servers'] = ["localhost:9092"]
    properties['key_serializer'] = str.encode
    properties['value_serializer'] = str.encode
    properties['api_version']= (1, 0, 0)
    return properties


  def close(self, e):
    print(str(e))
    self.producer.close()