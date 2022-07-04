
from kafka_service import KafkaService

class EmailService:
    topics = "ECOMMERCE_SEND_EMAIL"

    def __init__(self):
        service = KafkaService(EmailService.__name__, self.topics, self.parser)
        try:
            service.run()
        except Exception as e:
            service.close(e)

    def parser(self, record):
        print("-----------------------------------------")
        print("Sending Email")
        print("Key => {}".format(record.key.decode("utf-8")))
        print("Value => {}".format(record.value.decode("utf-8")))
        print("Partition => {}".format(record.partition))
        print("Offset => {}".format(record.offset))
        print("Order proccessed\n")


EmailService()
