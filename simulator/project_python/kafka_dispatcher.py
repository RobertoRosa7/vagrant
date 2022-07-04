from kafka import KafkaProducer


class KafkaDispatcher:
    producer = None

    def __init__(self):
        self.producer = KafkaProducer(
            bootstrap_servers=self.properties()["servers"],
            key_serializer=self.properties()["key"],
            value_serializer=self.properties()["value"],
            api_version=self.properties()["version"],
        )

    def send(self, topic, key, value):
        future = self.producer.send(topic, key=key, value=value)
        result = future.get(timeout=60)
        self.on_sucess(result)

    def on_sucess(self, record):
        print("Topic => {}".format(record.topic))
        print("Partition => {}".format(record.partition))
        print("Offset => {}".format(record.offset))
        print("TimeStamp => {}".format(record.timestamp))

    def properties(self):
        properties = {}
        properties["servers"] = ["localhost:9092"]
        properties["key"] = str.encode
        properties["value"] = str.encode
        properties["version"] = (1, 0, 0)
        return properties

    def close(self, e):
        print(str(e))
        self.producer.close()
