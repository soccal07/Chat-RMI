package chat.rmi;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public class ServerChat implements IServerChat{
    
    private List<RoomChat> roomList;
    
    public ServerChat(){
        roomList = new ArrayList<>();
    }
    
    @Override
    public List<RoomChat> getRooms() throws RemoteException {
        return roomList;
    }

    @Override
    public void createRoom(String roomName) throws RemoteException {
        RoomChat rc = new RoomChat(roomName);
        roomList.add(rc);
    }
}
