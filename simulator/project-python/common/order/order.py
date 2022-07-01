import json


class Order:
    user_id: str
    order_id: str
    amount: float

    def __init__(self, user_id, order_id, amount):
        self.user_id = user_id
        self.order_id = order_id
        self.amount = amount

    def serializer(self):
        return json.dumps(
            {"user_id": self.user_id, "order_id": self.order_id, "amount": self.amount}
        )
