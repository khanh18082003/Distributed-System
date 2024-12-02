package UI;

import com.springMVC.entity.User;
import enums.Constants;
import enums.Execution;
import java.awt.Dimension;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

public class WelcomeView extends JFrame {

  private JPanel contentPane;
  private JPanel northPanel;
  private JPanel headerPanel;
  private JButton deleteButton;
  private JButton saveButton;
  private JButton addButton;
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
  private final Client client;
  private List<User> users;
  private AddUserView addUserView;

  private void init() {
    Constants.BRANCH_MAP.forEach((key, value) -> branchCombobox.addItem(key));
    branchCombobox.setPreferredSize(new Dimension(200, 30));

    deleteButton.setEnabled(false);
    saveButton.setEnabled(false);
  }

  public WelcomeView() {
    super(Constants.TITLE);
    init();
    setContentPane(contentPane);
    setSize(1000, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Add a list selection listener to the userList
    userList.getSelectionModel().addListSelectionListener(e -> {
      deleteButton.setEnabled(true);
      saveButton.setEnabled(true);

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
      String request;
      if (checkBoxPanel.isSelected()) {
        request = Execution.GET_LIST.getRequest();
      }else {
        // Set the request to get the list of users from the server
        request = Execution.GET_LIST.getRequest() + Constants.BRANCH_MAP.get(branchCombobox.getSelectedItem());
      }
      System.out.println(request);
      
      try {
        client.startConnection(Constants.SERVER_ADDRESS, Constants.PORT_NUMBER);
        client.setDataToSend(request);
        client.sendDataToServer();
        users = (List<User>) client.readDataFromServer();
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
        }
      }else {
        JOptionPane.showMessageDialog(null, "No data found");
      }
    });

    // Add an action listener to the saveButton button
    saveButton.addActionListener(e -> {
      // Get the selected branch from the branchCombobox
      String branch = Constants.BRANCH_MAP.get(branchCombobox.getSelectedItem());
      User user = new User();
      user.setId(Integer.parseInt(tfId.getText()));
      user.setFirstName(tfFirstName.getText());
      user.setLastName((tfLastName.getText()));
      user.setMaCN(tfBranch.getText());
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      try {
        user.setDob(new Date(format.parse(tfDob.getText()).getTime()));
      } catch (ParseException ex) {
        throw new RuntimeException(ex);
      }
      // Set the request to save the list of users to the server
      String message = Execution.SAVE_USER.getRequest() + branch;
      Map<String, Object> request = new HashMap<>();
      request.put("message", message);
      request.put("data", user);
      // Send the request to the server and read the response
      try {
        client.startConnection(Constants.SERVER_ADDRESS, Constants.PORT_NUMBER);
        client.setDataToSend(request);
        client.sendDataToServer();
        client.readDataFromServer();
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(null, "Server is not available");
      } catch (ClassNotFoundException ex) {
        JOptionPane.showMessageDialog(null, "Cannot read data from server");
      } finally {
        client.closeConnection();
      }
    });

    // Add an action listener to the deleteButton button
    deleteButton.addActionListener(e -> {
      // Get the selected branch from the branchCombobox
      String branch = Constants.BRANCH_MAP.get(branchCombobox.getSelectedItem());
      int id = (Integer) userList.getValueAt(userList.getSelectedRow(), 0);
      System.out.println(id);
      // Send the request to the server and read the response
      Map<String, Object> request = new HashMap<>();
      request.put("message", Execution.DELETE_USER.getRequest() + Objects.requireNonNull(
          branchCombobox.getSelectedItem()).toString());
      request.put("data", id);
      Client client = Client.getInstance();

      try {
        client.startConnection(Constants.SERVER_ADDRESS, Constants.PORT_NUMBER);
        client.setDataToSend(request);
        client.sendDataToServer();

      } catch (IOException ex) {
        throw new RuntimeException(ex);
      } finally {
        client.closeConnection();
      }
    });

    // Add an action listener to the addButton button
    addButton.addActionListener(e -> {
      // Get the selected branch from the branchCombobox
      String branch = Constants.BRANCH_MAP.get(branchCombobox.getSelectedItem());
      addUserView = new AddUserView(Constants.BRANCH_MAP.get(branchCombobox.getSelectedItem()));

    });
  }
}
