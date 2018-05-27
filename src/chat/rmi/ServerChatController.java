package chat.rmi;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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
import javafx.scene.control.TextArea;


public class ServerChatController implements Initializable {
    
    Registry registry = null;
    ServerChat serverChat = null;
    IServerChat iServerChat = null;
    private List<RoomChat> roomList = null;
    private ObservableList<RoomChat> observableList;
    
    @FXML
    private ComboBox<RoomChat> cbRooms;

    @FXML
    private Button btnCloseRoom;
    
    @FXML
    private TextArea serverMsgs;
    
    @FXML
    private Button btnStartServer;
    
    @FXML
    private Button btnRefresh;

    
    @FXML
    void btnCloseRoomAction(ActionEvent event) {
        
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
            cbRooms.setItems(observableList);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            registry = LocateRegistry.createRegistry(2020);
            serverChat = new ServerChat();
            iServerChat = (IServerChat) UnicastRemoteObject.exportObject(serverChat, 0);
            registry.rebind("Servidor", iServerChat);
        } catch (RemoteException ex) {
            Alert alert;
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Remote Exception");
            alert.show();  
        }
        finally{
            serverMsgs.setText("Server started...");
        }
    }
}
