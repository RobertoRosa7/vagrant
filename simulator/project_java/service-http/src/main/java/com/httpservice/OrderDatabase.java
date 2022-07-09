package com.httpservice;

public class OrderDatabase implements Closeable {
  private final LocalDatabase database;

  public OrderDatabase() {
    this.database = new LocalDatabase("orders_database");
    this.database.createIfNotExists("create table Orders (uuid varchar(200) primary key)");
  }

  public boolean saveNew(Order order) throws SQLException {
    if (this.wasProcessed(order)) {
      return false;
    }
    this.database.update("insert into Orders (uuid) values (?)", order.getOrderId());
    return true;
  }

  private boolean wasProcessed(Order order) throws SQLException {
    ResultSet results = this.database.query("select uuid from Orders where uuid = ? limit 1", order.getOrderId());
    return results.next();
  }

  @override
  public void close() throws IOException {
    try {
      this.database.close();
    } catch (SQLException e) {
      throw new IOException
    }
  }
}