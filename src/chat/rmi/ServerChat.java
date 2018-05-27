package chat.rmi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerChat implements IServerChat{
    
    private List<IRoomChat> roomList;
    
    public ServerChat(){
        roomList = new ArrayList<>();
    }
    
    @Override
    public List<IRoomChat> getRooms() throws RemoteException {
        return roomList;
    }

    @Override
    public void createRoom(String roomName) throws RemoteException {
        for(IRoomChat rc : roomList){
            if(rc.getRoomName().equals(roomName)){
                return;
            }
        }
        
        try {
            RoomChat rc = new RoomChat(roomName);
            Registry registry = LocateRegistry.getRegistry(2020);
            IRoomChat stub = (IRoomChat) registry.lookup(Definitions.roomBindPrefix + roomName);
            roomList.add(stub);
        } catch (NotBoundException | AccessException ex) {
            Logger.getLogger(ServerChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
