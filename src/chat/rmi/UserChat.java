package chat.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserChat extends UnicastRemoteObject implements IUserChat{
    
    private String usrName;
    private Registry registry;

    public UserChat() throws RemoteException{
        super();
        usrName = "none";
        try {
            registry = LocateRegistry.getRegistry(2020);
            registry.rebind(Definitions.userBindPrefix + usrName, this);
        } catch (RemoteException ex) {
            Logger.getLogger(RoomChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUsrName() {
        return usrName;
    }
    
    public void setUsrName(String usrName) throws RemoteException{
        this.usrName = usrName;
        try {
            registry = LocateRegistry.getRegistry(2020);
            registry.rebind(Definitions.userBindPrefix + usrName, this);
        } catch (RemoteException ex) {
            Logger.getLogger(RoomChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void deliverMsg(String senderName, String msg) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
