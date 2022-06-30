from uuid import uuid4
from kafka import KafkaConsumer

class KafkaService:
  consumer = None
  parser = None

  def __init__(self, group_id, topic, parser, pattern=None):
    self.parser = parser
    self.consumer = KafkaConsumer(bootstrap_servers=self.properties(group_id)['bootstrap_servers'],
                                  group_id=self.properties(group_id)['group_id'],
                                  client_id=self.properties(group_id)['client_id'],
                                  max_poll_records=self.properties(group_id)['max_poll_records'])

    self.consumer.subscribe(topics=topic, pattern=pattern)

  def run(self):
    for record in self.consumer:
      self.parser(record)


  def properties(self, group_id):
    properties = {}
    properties['bootstrap_servers'] = ["localhost:9092"]
    properties['group_id'] = group_id
    properties['client_id'] = str(uuid4())
    properties['max_poll_records'] = 1
    return properties

  def close(self, e):
    print(str(e))
    self.consumer.close()