package chat.rmi;

import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private IRoomChat iRoomChat = null;
    private UserChat userChat = null;
    private List<IRoomChat> roomList = null;
    private ObservableList<String> observableList;
    
    @FXML
    private ComboBox<String> cmbRooms;
    
    @FXML
    private ListView<String> listUsersOnRoom;
            
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
    void btnNewRoomAction(ActionEvent event) throws RemoteException {
        
        if(!"none".equals(userChat.getUsrName())){
            
            String roomName = txtNewRoomName.getText();
            List<String> allRooms = new ArrayList();
            for(IRoomChat rc: roomList){
                allRooms.add(rc.getRoomName());
            }
            if(!roomName.isEmpty() && !allRooms.contains(roomName)) {
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
            } else {
                Alert alert;
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Invalid Room name!");
                alert.show();
            }
        } else {
            Alert alert;
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please, inform your Username first!");
            alert.show();
        }
    }
    
    @FXML
    void btnJoinAction(ActionEvent event) throws RemoteException {
        
        if(!"none".equals(userChat.getUsrName())){
            
            String selectedRoom = cmbRooms.getValue();
            String myRoom = "";
            if(iRoomChat != null){
                    myRoom = iRoomChat.getRoomName();
            }
            if(selectedRoom != null && !myRoom.equals(selectedRoom)){

                if(iRoomChat != null){
                    iRoomChat.leaveRoom(userChat.getUsrName());
                }

                String roomName = selectedRoom;
                try {
                    iRoomChat = (IRoomChat) registry.lookup(Definitions.roomBindPrefix + roomName);
                    iRoomChat.joinRoom(userChat.getUsrName());
                } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(UserChatController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (myRoom.equals(selectedRoom)){
                Alert alert;
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("You are already in that room!");
                alert.show();
            } else {
                Alert alert;
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Select a room!");
                alert.show();
            }
        } else {
            Alert alert;
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please, inform your Username first!");
            alert.show();
        }
    }

    @FXML
    void btnLeaveAction(ActionEvent event) throws RemoteException {
        if (iRoomChat != null)
        {
            iRoomChat.leaveRoom(userChat.getUsrName());
            
        }
        iRoomChat = null;
    }
    
    public IRoomChat getRoomRef(String roomName) throws RemoteException{ 
        try {
            IRoomChat stub = (IRoomChat) registry.lookup(Definitions.roomBindPrefix + roomName);
            return stub;
        } catch (NotBoundException | AccessException ex) {
            Logger.getLogger(UserChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    void getRoomUsers(String selectedRoomName) throws RemoteException {
        
        try {
            iRoomChat = getRoomRef(selectedRoomName);
        } catch (RemoteException ex) {
            Logger.getLogger(UserChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(iRoomChat != null && roomList.contains(iRoomChat)){
            List<IUserChat> usrList = iRoomChat.getUsrList();
            
            List<String> usersNames = new ArrayList();
            for(IUserChat usr : usrList){
                usersNames.add(usr.getUsrName()); 
            }
            
            ObservableList<String> observableListUsrs = FXCollections.observableArrayList(usersNames);
            listUsersOnRoom.setItems(observableListUsrs);
        }
    }

    @FXML
    void btnRefreshAction(ActionEvent event) throws RemoteException {
        try {
            roomList = iServerChat.getRooms();
        } catch (RemoteException ex) {
            Alert alert;
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Remote Exception");
            alert.show();
        } finally {
            List<String> roomNameList = new ArrayList();
            for(IRoomChat rc : roomList){
                roomNameList.add(rc.getRoomName());
            }
            observableList = FXCollections.observableArrayList(roomNameList);
            cmbRooms.setItems(observableList);
            
            String selectedRoomName = cmbRooms.getValue();
            if(selectedRoomName != null){
                getRoomUsers(selectedRoomName);
            }
            else{
                listUsersOnRoom.setItems(null);
            }
        }
    }

    @FXML
    void btnSendAction(ActionEvent event) {
        String message = txtMsg.getText();
        if(!message.isEmpty() && iRoomChat != null){
            try {
                iRoomChat.sendMsg(userChat.getUsrName(), message);
                txtMsg.setText("");
            } catch (RemoteException ex) {
                Logger.getLogger(UserChatController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML
    void btnSetNameAction(ActionEvent event) {
        
        String usrName = txtUserName.getText();
        if(!usrName.isEmpty()){
            try {
                userChat.setUsrName(usrName);
            } catch (RemoteException ex) {
                Logger.getLogger(UserChatController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            registry = LocateRegistry.getRegistry(Definitions.serverIp, 2020);
            iServerChat = (IServerChat) registry.lookup(Definitions.serverBindName);
            userChat = new UserChat();
            userChat.chatArea = chatArea;
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
