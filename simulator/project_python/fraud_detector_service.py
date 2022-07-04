from asyncore import dispatcher
import json
from kafka_service import KafkaService
from kafka_dispatcher import KafkaDispatcher


class FraudDetectorService:
    topics = "ECOMMERCE_NEW_ORDER"
    dispatcher = KafkaDispatcher()

    def __init__(self):
        service = KafkaService(FraudDetectorService.__name__, self.topics, self.parser)
        try:
            service.run()
        except Exception as e:
            service.close(e)

    def parser(self, record):
        print("-----------------------------------------")
        print("Processing new order, checking for fraud")
        print("Key => {}".format(record.key))
        print("Value => {}".format(record.value))
        print("Partition => {}".format(record.partition))
        print("Offset => {}".format(record.offset))
        print("Order proccessed\n")

        order = json.loads(record.value)

        if self.getFraud(order):
            dispatcher.send(
                "ECOMMERCE_ORDER_REJECT", key=record.key, value=record.value
            )
            print("FRAUD DETECTED => {}".format(order.get("amount")))
        else:
            dispatcher.send(
                "ECOMMERCE_ORDER_APPROVED", key=record.key, value=record.value
            )
            print("APROVED ORDER  USER ID => {}".format(record.key))

    def getFraud(self, order):
        return order.get("amount") >= 4500


FraudDetectorService()
