package socket;

import dto.request.Request;
import dto.response.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Client {

  private Request request;
  private Socket clientSocket;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private static Client instance;

  private Client() {

  }

  public static Client getInstance() {
    if (instance == null) {
      instance = new Client();
    }
    return instance;
  }

  public void setDataToSend(Request request) throws IOException {
    this.request = request;
    System.out.println(this.request.toString());
  }

  public void startConnection(String host, int port) throws IOException {
    System.out.println("Begin connect!");
    clientSocket = new Socket(host, port);
    System.out.println("Connect: " + clientSocket.isConnected());
    System.out.println(clientSocket.getPort());
  }

  public void sendDataToServer() throws IOException {

    //Gửi chuỗi ký tự tới Server thông qua outputStream đã nối với Socket (ở trên)
    out = new ObjectOutputStream(clientSocket.getOutputStream());
    out.writeObject(request);
    out.flush();
    System.out.println("TO SERVER " + clientSocket.getInetAddress().getHostAddress() + ": " + request.getMessage());
  }

  public Response readDataFromServer() throws IOException, ClassNotFoundException {

    //print kết qua ra màn hình
    in = new ObjectInputStream(clientSocket.getInputStream());
    System.out.println("FROM SERVER " + clientSocket.getInetAddress().getHostAddress());
    return (Response) in.readObject();
  }

  public void closeConnection() {
    try {
      in.close();
      out.close();
      clientSocket.close();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Cannot close connection");
    }
  }
}
