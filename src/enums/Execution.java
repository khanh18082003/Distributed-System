package enums;

public enum Execution {

  GET_LIST("GET_LIST_"),
  ADD_USER("ADD_"),
  DELETE_USER("DELETE_"),
  SAVE_USER("SAVE_");

  private String request;

  Execution(String request) {
    this.request = request;
  }

  public String getRequest() {
    return request;
  }
}
