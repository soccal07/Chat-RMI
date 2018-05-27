package chat.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IUserChat extends Remote {
    public void deliverMsg(String senderName, String msg) throws RemoteException;
    public void setUsrName(String usrName) throws RemoteException;
    public String getUsrName() throws RemoteException;
}
