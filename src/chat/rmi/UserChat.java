package chat.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;


public class UserChat implements IUserChat, Serializable{
    
    private String usrName;

    public UserChat() {
        usrName = "none";
    }

    public String getUsrName() {
        return usrName;
    }
    
    public void setUsrName(String usrName){
        this.usrName = usrName;
    }
    
    @Override
    public void deliverMsg(String senderName, String msg) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
