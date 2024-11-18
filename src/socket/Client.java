package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Client {

  private String request;
  private Socket clientSocket;
  private final String host;
  private final int port;
  private ObjectInputStream in;
  private ObjectOutputStream out;

  //Tạo Inputstream(từ bàn phím)
  public Client(String host, int port) {
    this.port = port;
    this.host = host;
    System.out.println("Init success " + this.host + " " + this.port);
  }

  public void setDataToSend(String request) throws IOException {
    this.request = request;
    System.out.println(this.request);
  }

  public void sendDataToServer() throws IOException {
    System.out.println("Begin connect");
    //Tạo socket cho client kết nối đến server qua ID address và port number
    clientSocket = new Socket(host, port);
    System.out.println("Connect success");
    out = new ObjectOutputStream(clientSocket.getOutputStream());
    //Gửi chuỗi ký tự tới Server thông qua outputStream đã nối với Socket (ở trên)
    out.writeBytes(request + '\n');
    out.flush();
    System.out.println("TO SERVER " + clientSocket.getInetAddress().getHostAddress() + ": " + request);
  }

  public Object readDataFromServer() throws IOException, ClassNotFoundException {
    in = new ObjectInputStream(clientSocket.getInputStream());

    //print kết qua ra màn hình
    System.out.println("FROM SERVER " + clientSocket.getInetAddress().getHostAddress());
    return in.readObject();
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
