package UI;

import com.springMVC.entity.User;
import enums.Constants;
import enums.Execution;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import socket.Client;

public class AddUserView extends JFrame {

  private JPanel contentPane;
  private JTextField textField1;
  private JPasswordField passwordField1;
  private JTextField textField2;
  private JTextField textField3;
  private JComboBox<Integer> comboBox1;
  private JComboBox<Integer> comboBox2;
  private JComboBox<Integer> comboBox3;
  private JButton submitButton;
  private String maCN;

  public AddUserView(String maCN) {
    super("Add User Form");
    setContentPane(contentPane);
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setVisible(true);
    setLocationRelativeTo(null);

    for (int i = 1; i <= 12; i++) {
      comboBox2.addItem(i);
    }

    for (int i = 1; i <= 31; i++) {
      comboBox3.addItem(i);
    }

    int yearNow = LocalDate.now().getYear();
    for (int i = yearNow; i >= 1900; i--) {
      comboBox1.addItem(i);
    }

    submitButton.addActionListener((e) -> {
      User user = new User();
      user.setUsername(textField1.getText());
      user.setPassword(Arrays.toString(passwordField1.getPassword()));
      user.setFirstName(textField2.getText());
      user.setLastName(textField3.getText());
      user.setMaCN(maCN);
      user.setDob(Date.valueOf(
          LocalDate.of(
              (Integer) Objects.requireNonNull(comboBox1.getSelectedItem()),
              (Integer) Objects.requireNonNull(comboBox2.getSelectedItem()),
              (Integer) Objects.requireNonNull(comboBox3.getSelectedItem())
          )
      ));
      Map<String, Object> request = new HashMap<>();
      request.put("message", Execution.ADD_USER.getRequest() + maCN);
      request.put("data", user);
      Client client = Client.getInstance();

      try {
        client.startConnection(Constants.SERVER_ADDRESS, Constants.PORT_NUMBER);
        client.setDataToSend(request);
        client.sendDataToServer();

      } catch (IOException ex) {
        throw new RuntimeException(ex);
      } finally {
        client.closeConnection();
        this.dispose();
      }
    });
  }
}
