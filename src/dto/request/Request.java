package dto.request;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  private String message;
  private Object data;

  public Request() {}

  public Request(String message, Object data) {
    this.message = message;
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
