import random
import uuid
from email import Email

from kafka_dispatcher import KafkaDispatcher
from order import Order


class NewOrder:
    def __init__(self):
        dispatcher = KafkaDispatcher()

        try:
            user_id = str(uuid.uuid4())
            order_id = str(uuid.uuid4())
            amount = random.random()

            new_order = Order(user_id, order_id, amount)
            new_email = Email("Novo Membro", "Bem vindo")

            for _ in range(11):
                dispatcher.send(
                    "ECOMMERCE_NEW_ORDER", key=user_id, value=new_order.serializer()
                )
                dispatcher.send(
                    "ECOMMERCE_SEND_EMAIL", key=user_id, value=new_email.serialize()
                )

        except Exception as e:
            dispatcher.close(e)


NewOrder()
