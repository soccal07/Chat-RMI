package chat.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public class RoomChat implements IRoomChat, Serializable {
    private String roomName;
    private List<UserChat> usrList;

    public RoomChat(String roomName) {
        this.roomName = roomName;
        usrList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return roomName; 
    }
    
    @Override
    public void sendMsg(String usrName, String msg) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void joinRoom(String usrName) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void leaveRoom(String usrName) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void closeRoom() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRoomName() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
