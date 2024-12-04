package UI;

import com.springMVC.entity.User;
import dto.request.Request;
import dto.response.Response;
import common.Constants;
import common.Execution;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import socket.Client;

public class CreateUserForm extends JFrame {

  private JPanel contentPane;
  private JPanel centerPanel;
  private JTextField textFieldUsername;
  private JPasswordField passwordField;
  private JTextField textFieldFirstName;
  private JTextField textFieldLastName;
  private JComboBox<Integer> comboBoxYear;
  private JComboBox<Integer> comboBoxMonth;
  private JComboBox<Integer> comboBoxDay;
  private JButton createButton;

  public CreateUserForm(String maCN) {
    super("Create User Form");
    setSize(500, 370);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setVisible(true);
    setContentPane(contentPane);

    int yearNow = LocalDate.now().getYear();

    for (int i = yearNow; i >= 1900; i--) {
      comboBoxYear.addItem(i);
    }

    for (int i = 1; i <= 12; i++) {
      comboBoxMonth.addItem(i);
    }

    for (int i = 1; i <= 31; i++) {
      comboBoxDay.addItem(i);
    }

    createButton.addActionListener(e -> {
      User user = new User();
      user.setUsername(textFieldUsername.getText());
      StringBuilder password = new StringBuilder();
      for (int i = 0; i < passwordField.getPassword().length; i++) {
        password.append(passwordField.getPassword()[i]);
      }
      user.setPassword(password.toString());
      user.setFirstName(textFieldFirstName.getText());
      user.setLastName(textFieldLastName.getText());
      user.setDob(Date.valueOf(
          LocalDate.of(
              (Integer) Objects.requireNonNull(comboBoxYear.getSelectedItem()),
              (Integer) Objects.requireNonNull(comboBoxMonth.getSelectedItem()),
              (Integer) Objects.requireNonNull(comboBoxDay.getSelectedItem()))));
      Client client = Client.getInstance();
      Request request = new Request();
      request.setMessage(Execution.ADD_USER.getRequest() + maCN);
      request.setData(user);
      System.out.println(request.toString());
      String message = null;
      try {
        client.startConnection(Constants.SERVER_ADDRESS, Constants.PORT_NUMBER);
        client.setDataToSend(request);
        client.sendDataToServer();
        Response response = client.readDataFromServer();
        message = response.getMessage();
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(null, "Server is not available");
      } catch (ClassNotFoundException ex) {
        throw new RuntimeException(ex);
      } finally {
        client.closeConnection();
        this.dispose();
        JOptionPane.showMessageDialog(null, message);
      }
    });

  }

}
