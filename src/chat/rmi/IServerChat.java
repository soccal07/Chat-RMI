package chat.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IServerChat extends Remote {
    public List<RoomChat> getRooms() throws RemoteException;
    public void createRoom(String roomName) throws RemoteException;
}
