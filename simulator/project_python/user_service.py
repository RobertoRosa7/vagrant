import sqlite3


class UserService:
    connection = None

    def __init__(self):
        self.connection = sqlite3.connect("file:users_database.db?mode=rw", uri=True)
        cur = self.connection.cursor()
        cur.execute("""create table Users (uuid text primary key, email text)""")
