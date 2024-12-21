package gui;

import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import ocsf.server.ConnectionToClient;
import server.LibraryServer;
import server.ServerApplication;

/*
 * controller for ServerSocket.fxml
 */

public class ServerSocket extends AbstractScreen{
  @FXML
  private TextField Porttxt = null;

  /*
   * Creates Server with wanted port
   */

  public void Create(ActionEvent event) throws Exception {
    String port = Porttxt.getText();
    if (port.trim().isEmpty()) {
      System.out.println("You must enter port number");
    } else {
      ServerApplication.getInstance().createServer(port);
    }
  }
}