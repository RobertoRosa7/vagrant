package service.user;

public class User {
  private final String uuid;

  User(String uuid) {
    this.uuid = uuid;
  }

  public String getUserUuid() {
    return this.uuid;
  }

}
