package GQ;


import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.FutureTask;


class GQRecvBuffer 
{
	private StringBuffer buff;

	public GQRecvBuffer(){
		buff = new StringBuffer();
	}
}

public class GQConnectRecv implements Runnable
{
	//private Map<String, Socket> IDAndSocket;
	private int port;
	private GQFriendInfo selfInfo;
	
	public GQConnectRecv(GQFriendsListFrame frame){
		this.selfInfo = frame.selfInfo;
		this.port = frame.selfInfo.getGoOnlineAddress().PORT;
		//IDAndSocket = frame.getIDAndSocket();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Scanner scan = null;
		try {
			ServerSocket ss = new ServerSocket(port);
			while(true){
				/*
				 * 接收列表中朋友的连接请求
				 */
				Socket s = ss.accept();
				
				/*
				 * 用新socket形成ID（即IP:PORT）唯一标识
				 */
				scan = new Scanner(s.getInputStream());
				String ID = scan.nextLine();
				
				System.out.println("GQConnectRecv ID" + ":" + ID + ID);
				/*
				 * 加入MAP<ID, Socket>
				 * MAP<ID, NETTHREAD>
				 * 启动新线程
				 */
				if (null == GQFriendsListFrame.getIDAndSocket().get(ID)){
					GQFriendsListFrame.addSocketToMap(ID, s);
				}
				
				if (null == GQFriendsListFrame.getIDAndNetMap().get(ID)){
					GQFriendInfo gf;
					if (null == (gf = GQFriendsListFrame.getIDAndFriendInfo().get(ID)))
					{
						System.out.println("invalid friend info:" + ID);
						System.exit(0);
					}
					GQDlgNet dlgNet = new GQDlgNet(ID, gf);
					FutureTask<Integer> task = new FutureTask<Integer>(dlgNet);
					GQFriendsListFrame.addNetTaskToMap(ID, dlgNet);
					System.out.println("GQConnectRecv create DLG NET:" + ID);
					new Thread(task).start();
					//task.cancel(true);
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			/*
			 * close all socket;
			 */
			for (Socket s : GQFriendsListFrame.getIDAndSocket().values()){
				try {
					scan.close();
					s.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
