package chat.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IRoomChat extends Remote{
    public void sendMsg(String usrName, String msg) throws RemoteException;
    public void joinRoom(String usrName) throws RemoteException;
    public void leaveRoom(String usrName) throws RemoteException;
    public void closeRoom() throws RemoteException;
    public String getRoomName() throws RemoteException;
    public List<IUserChat> getUsrList() throws RemoteException;
}
