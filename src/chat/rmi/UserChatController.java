package chat.rmi;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class UserChatController implements Initializable {
    
    private Registry registry = null;
    private IServerChat iServerChat = null;
    private UserChat userChat = null;
    private List<RoomChat> roomList = null;
    private ObservableList<RoomChat> observableList;
    
    @FXML
    private ComboBox<RoomChat> cmbRooms;
    
    @FXML
    private ListView<UserChat> listUsersOnRoom;
            
    @FXML
    private TextArea chatArea;

    @FXML
    private TextField txtMsg;
    
    @FXML
    private Button btnSetName;

    @FXML
    private Button btnSend;

    @FXML
    private Button btnJoin;

    @FXML
    private Button btnLeave;

    @FXML
    private Button btnGetRooms;
    
    @FXML
    private TextField txtUserName;
    
    @FXML
    private Label lblUserName;
    
    @FXML
    private Button btnNewRoom;
    
    @FXML
    private TextField txtNewRoomName;
    
    @FXML
    void btnNewRoomAction(ActionEvent event) {
        
        if(userChat.getUsrName() != "none"){
            String roomName = txtNewRoomName.getText();
            if(!roomName.isEmpty()) {
                try {
                    iServerChat.createRoom(roomName);
                } catch (RemoteException ex) {
                    Alert alert;
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Remote Exception");
                    alert.show();
                } finally {
                    txtNewRoomName.clear();
                }
            }
        } else {
            Alert alert;
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please, inform your Username first!");
            alert.show();
        }
    }
    
    @FXML
    void btnJoinAction(ActionEvent event) {
        RoomChat selectedRoom = cmbRooms.getValue();
        if(roomList.contains(selectedRoom)){
            String roomName = selectedRoom.toString();
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(roomName);
            alert.show();
        } else {
            Alert alert;
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Select a room!");
            alert.show();
        }
    }

    @FXML
    void btnLeaveAction(ActionEvent event) {

    }

    @FXML
    void btnRefreshAction(ActionEvent event) {
        try {
            roomList = iServerChat.getRooms();
        } catch (RemoteException ex) {
            Alert alert;
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Remote Exception");
            alert.show();
        } finally {
            observableList = FXCollections.observableArrayList(roomList);
            cmbRooms.setItems(observableList);
        }
    }

    @FXML
    void btnSendAction(ActionEvent event) {
        
    }
    
    @FXML
    void btnSetNameAction(ActionEvent event) {
        
        String usrName = txtUserName.getText();
        if(!usrName.isEmpty()){
            userChat.setUsrName(usrName);
            txtUserName.visibleProperty().setValue(Boolean.FALSE);
            txtUserName.disableProperty().setValue(Boolean.TRUE);
            btnSetName.visibleProperty().setValue(Boolean.FALSE);
            btnSetName.disableProperty().setValue(Boolean.TRUE);
            lblUserName.setText(usrName);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            registry = LocateRegistry.getRegistry("localhost", 2020);
            iServerChat = (IServerChat) registry.lookup("Servidor");
            userChat = new UserChat();
        } catch (RemoteException | NotBoundException ex) {
            Alert alert;
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Remote Exception");
            alert.show();
        }
        finally{
            roomList = new ArrayList<>();
        }
    }  
}
