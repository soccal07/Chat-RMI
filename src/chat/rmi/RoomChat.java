package chat.rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RoomChat extends UnicastRemoteObject implements IRoomChat {
    private String roomName;
    private List<IUserChat> usrList;
    private Registry registry;

    public RoomChat(String roomName) throws RemoteException {
        super();
        this.roomName = roomName;
        usrList = new ArrayList<>();
        try {
            registry = LocateRegistry.getRegistry(2020);
            registry.bind(Definitions.roomBindPrefix + roomName, this);
        } catch (RemoteException | AlreadyBoundException ex) {
            Logger.getLogger(RoomChat.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        
        List<String> usrsOnline = new ArrayList();
        for(IUserChat usr : usrList){
            usrsOnline.add(usr.getUsrName());
        }
        if(usrsOnline.contains(usrName)){
            return; // Usuario não pode entrar duas vezes na mesma sala
        }
        
        try {
            IUserChat user = (IUserChat) registry.lookup(Definitions.userBindPrefix + usrName);
            usrList.add(user);
            System.out.println("User " + user.getUsrName() + " joined room " + roomName);
        } catch (Exception ex) {
            Logger.getLogger(RoomChat.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        return roomName;
    }
    
    @Override
    public List<IUserChat> getUsrList() throws RemoteException {
        return usrList;
    }
    
}
