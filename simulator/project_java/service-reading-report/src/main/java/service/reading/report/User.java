package service.reading.report;

public class User {
  private final String uuid;

  User(String uuid) {
    this.uuid = uuid;
  }

  public String getReportPath() {
    return "target/" + this.uuid + "-report.txt";
  }

  public String getUserUuid() {
    return this.uuid;
  }

}
