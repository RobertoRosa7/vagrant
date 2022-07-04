import re

from kafka_service import KafkaService


class LogService:
    topics = ()

    def __init__(self):
        service = KafkaService(
            LogService.__name__, self.topics, self.parser, re.compile("ECOMMERCE.*")
        )
        try:
            service.run()
        except Exception as e:
            service.close(e)

    def parser(self, record):
        print("-----------------------------------------")
        print("LOG => {}".format(record.topic))
        print("Key => {}".format(record.key.decode("utf-8")))
        print("Value => {}".format(record.value.decode("utf-8")))
        print("Partition => {}".format(record.partition))
        print("Offset => {}".format(record.offset))
        print("Order proccessed\n")


LogService()
