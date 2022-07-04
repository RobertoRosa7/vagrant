import json


class Email:
    subject = None
    body = None

    def __init__(self, subject, body):
        self.subject = subject
        self.body = body

    def serialize(self):
        return json.dumps({"subject": self.subject, "body": self.body})
