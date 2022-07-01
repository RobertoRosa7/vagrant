import os
import random
import sys
import uuid

from common.email.email import Email
from common.order.order import Order
from kafka_dispatcher import KafkaDispatcher

sys.path.append(os.path.abspath(os.getcwd()))


class NewOrderMain:
    def __init__(self):
        kafkaDispatcher = KafkaDispatcher()

        try:
            user_id = str(uuid.uuid4())
            order_id = str(uuid.uuid4())
            amount = random.random()

            new_order = Order(user_id, order_id, amount)
            new_email = Email("Novo Membro", "Bem vindo")

            kafkaDispatcher.send(
                "ECOMMERCE_NEW_ORDER", key=user_id, value=new_order.serializer()
            )
            kafkaDispatcher.send(
                "ECOMMERCE_SEND_EMAIL", key=user_id, value=new_email.serialize()
            )

        except Exception as e:
            kafkaDispatcher.close(e)


NewOrderMain()
