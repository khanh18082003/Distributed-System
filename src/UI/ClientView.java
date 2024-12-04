package UI;

import com.springMVC.entity.User;
import dto.request.Request;
import dto.response.Response;
import common.Constants;
import common.Execution;
import java.awt.Dimension;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import socket.Client;

public class ClientView extends JFrame {

  private JPanel contentPane;
  private JPanel northPanel;
  private JPanel headerPanel;
  private JButton getListBtn;
  private JPanel footerPanel;
  private JComboBox<String> branchCombobox;
  private JLabel branchLabel;
  private JPanel dataPanel;
  private JTable userList;
  private JPanel infoUserPanel;
  private JTextField tfId;
  private JTextField tfFirstName;
  private JTextField tfLastName;
  private JTextField tfBranch;
  private JTextField tfDob;
  private JCheckBox checkBoxPanel;
  private JButton createUserButton;
  private JButton deleteUserButton;
  private final Client client;
  private List<User> users;
  private CreateUserForm createUserForm;

  private void init() {
    Constants.BRANCH_MAP.forEach((key, value) -> branchCombobox.addItem(key));
    branchCombobox.setPreferredSize(new Dimension(200, 30));
    deleteUserButton.setEnabled(false);
  }

  public ClientView() {
    super(Constants.TITLE);
    init();
    setContentPane(contentPane);
    setSize(1000, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Add a list selection listener to the userList
    userList.getSelectionModel().addListSelectionListener(e -> {
      int index = userList.getSelectedRow();
      System.out.println(index);
      if (index >= 0) {
        User user = users.get(index);
        tfId.setText(String.valueOf(user.getId()));
        tfFirstName.setText(user.getFirstName());
        tfLastName.setText(user.getLastName());
        tfBranch.setText(user.getMaCN());
        tfDob.setText(user.getDob().toString());
      }
    });
    // Create a new instance of the Client class
    client = Client.getInstance();

    // Add an action listener to the getListBtn button
    getListBtn.addActionListener(e -> {
      Request request = new Request();

      if (checkBoxPanel.isSelected()) {
        request.setMessage(Execution.GET_LIST.getRequest());
      }else {
        // Set the request to get the list of users from the server
        request.setMessage(Execution.GET_LIST.getRequest() + Constants.BRANCH_MAP.get(branchCombobox.getSelectedItem()));
      }
      System.out.println(request);
      
      try {
        client.startConnection(Constants.SERVER_ADDRESS, Constants.PORT_NUMBER);
        client.setDataToSend(request);
        client.sendDataToServer();
        Response response = client.readDataFromServer();
        users = (List<User>) response.getData();
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(null, "Server is not available");
      } catch (ClassNotFoundException ex) {
        JOptionPane.showMessageDialog(null, "Cannot read data from server");
      } finally {
        client.closeConnection();
      }

      // Add the list of users to the userList table
      DefaultTableModel model = new DefaultTableModel(new Object[][]{},
          new Object[]{"ID", "Name", "Age"});
      userList.setModel(model);
      if (users != null) {
        for (User user : users) {
          LocalDate dob = user.getDob().toLocalDate();
          long age = ChronoUnit.YEARS.between(dob, LocalDate.now());
          model.addRow(
              new Object[]{user.getId(), user.getFirstName()+ " " + user.getLastName(), age});
        }
        if (model.getRowCount() > 0) {
          userList.setRowSelectionInterval(0, 0);
          deleteUserButton.setEnabled(true);
        }
      }else {
        JOptionPane.showMessageDialog(null, "No data found");
      }
    });

    createUserButton.addActionListener(e -> {
      createUserForm = new CreateUserForm(Constants.BRANCH_MAP.get(branchCombobox.getSelectedItem()));

    });

    deleteUserButton.addActionListener(e -> {
      Request request = new Request();
      request.setMessage(Execution.DELETE_USER.getRequest() + Constants.BRANCH_MAP.get(branchCombobox.getSelectedItem()));
      System.out.println(tfId.getText());
      request.setData(tfId.getText());
      System.out.println(request.toString());
      String message = null;
      ((DefaultTableModel)userList.getModel()).removeRow(userList.getSelectedRow());
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
      }
    });
  }
}
