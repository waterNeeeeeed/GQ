package GQ;


import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.DefaultListModel;


class OnlineAddressID
	implements Serializable
{
	public InetAddress Ip;
	public int PORT;
	public String id;
	
	public OnlineAddressID(InetAddress IpAddress, int PORT){
		this.Ip = IpAddress;
		this.PORT = PORT;
		StringBuffer tmp = new StringBuffer(Ip.getHostAddress() + ":" + PORT);
		this.id = tmp.toString();
	}
}

class GoOnlineMessage 
	implements Serializable
{
	public GQFriendInfo friend;
	//public GoOnlineAddress addr;
	
	public GoOnlineMessage(GQFriendInfo friend){//, GoOnlineAddress addr){
		this.friend = friend;
		//this.addr = addr;
	}
}

public class GQGoOnlineMulticast implements Runnable
{
	private static final String BROADCAST_IP = "236.6.6.6";
	private static final int BROADCAST_PORT = 36666;//
	private static final int DATA_LEN = 4096;
	
	private MulticastSocket socket = null;
	private InetAddress broadcastAddress = null;
	private Scanner scan = null;
	private Socket TCP_SOCK;
	byte[] inBuff = new byte[DATA_LEN];
	private DatagramPacket inPacket = new DatagramPacket(inBuff, inBuff.length);
	private DatagramPacket outPacket = null;
	private DefaultListModel<GQFriendInfo> friendsListModel;
	private GoOnlineMessage msg;// = new GoOnlineMessage()
	
	
	public GQGoOnlineMulticast(GQFriendInfo selfInfo, DefaultListModel<GQFriendInfo> friendsListModel) throws IOException{
		try{
			this.friendsListModel = friendsListModel;
			socket = new MulticastSocket(BROADCAST_PORT);
			System.out.println(socket.getInetAddress());
			broadcastAddress = InetAddress.getByName(BROADCAST_IP);
			socket.joinGroup(broadcastAddress);
			socket.setLoopbackMode(false);
			outPacket = new DatagramPacket(new byte[0], 0, broadcastAddress, BROADCAST_PORT);
			
			/*msg = new GoOnlineMessage(new GQFriendInfo("gongtao", null, "靠自己，坚信自己一定会成功", Gender.MALE, 
					new OnlineAddressID(InetAddress.getLocalHost(), BROADCAST_PORT)));*/
			
			msg = new GoOnlineMessage(selfInfo);
			
		 	new Thread(this).start();
			
			ByteArrayOutputStream bo = new ByteArrayOutputStream(DATA_LEN);
			ObjectOutputStream oos = new ObjectOutputStream(bo);
			oos.writeObject(msg);
			inBuff = bo.toByteArray();
			outPacket.setData(inBuff);
			
			while(true){
				socket.send(outPacket);
				Thread.sleep(1000);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			socket.close();
		}
	}

	/*创建线程接收多播数据报*/
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			System.out.println("start");
			byte[] bytes = new byte[DATA_LEN];
			socket.receive(inPacket);
			bytes = inPacket.getData();
			ObjectInputStream oi;
			GoOnlineMessage msg;
			
			while(true)
			{
				socket.receive(inPacket);
				bytes = inPacket.getData();
				
				oi = new ObjectInputStream(new ByteArrayInputStream(bytes, 0, DATA_LEN));
				msg = (GoOnlineMessage)(oi.readObject());
				addElementToFriendsList(msg);
				System.out.println("msg:" + msg.friend.getGoOnlineAddress().Ip.getHostAddress() + ":" + msg.friend.getGoOnlineAddress().PORT);
			}
			
		}
		catch(Exception ex){
			ex.printStackTrace();
			try{
				if (socket != null){
					socket.leaveGroup(broadcastAddress);
					socket.close();
				}
				System.exit(1);
			}
			catch(IOException e){
				e.printStackTrace();
			}
		} 
		finally{
			
		}
	}
	
	public int addElementToFriendsList(GoOnlineMessage msg){
		try {
			for (int i = 0; i < GQFriendsListFrame.getSizeFromFriendsListModel(); i++) {
				GQFriendInfo tmp = GQFriendsListFrame.getElementFromFriendsListModel(i);
				/*IP+PORT is the ID*/
				if ((msg.friend.getGoOnlineAddress().Ip.getHostAddress().equals(tmp.getGoOnlineAddress().Ip.getHostAddress()))
						&& (msg.friend.getGoOnlineAddress().PORT == tmp.getGoOnlineAddress().PORT)) {
					return 0;
					}
				}

				/*System.out.println("msg:" + msg.friend.getGoOnlineAddress().Ip.getHostAddress() + ":" + msg.friend.getGoOnlineAddress().PORT);
				System.out.println(GQFriendsListFrame.getSizeFromFriendsListModel());*/
				GQFriendsListFrame.addFriendInfoToList(msg.friend);
				return 1;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}
	
	/*public static void main(String[] args) throws IOException{
		new GQGoOnlineMulticast();
	}*/
}
