from uuid import uuid4

from kafka import KafkaConsumer


class KafkaService:
    consumer = None
    parser = None

    def __init__(self, group_id, topic, parser, pattern=None):
        self.parser = parser
        self.consumer = KafkaConsumer(
            bootstrap_servers=self.properties(group_id)["servers"],
            group_id=self.properties(group_id)["group"],
            client_id=self.properties(group_id)["client"],
            max_poll_records=self.properties(group_id)["max_poll"],
            key_deserializer=self.properties(group_id)["key_deserializer"],
            value_deserializer=self.properties(group_id)["value_deserializer"],
        )

        self.consumer.subscribe(topics=topic, pattern=pattern)

    def run(self):
        for record in self.consumer:
            self.parser(record)

    def properties(self, group_id):
        properties = {}
        properties["servers"] = ["localhost:9092"]
        properties["group"] = group_id
        properties["client"] = str(uuid4())
        properties["max_poll"] = 1
        properties["value_deserializer"] = str.encode
        properties["key_deserializer"] = str.encode
        return properties

    def close(self, e):
        print(str(e))
        self.consumer.close()
