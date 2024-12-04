package common;

public enum Execution {

  GET_LIST("GET_LIST_"),
  ADD_USER("ADD_USER_"),
  DELETE_USER("DELETE_USER_"),
  SAVE_USER("SAVE_USER_");

  private final String request;

  Execution(String request) {
    this.request = request;
  }

  public String getRequest() {
    return request;
  }
}
