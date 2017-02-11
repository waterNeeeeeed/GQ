package GQ;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class GQDlgNet implements Callable<Integer>
{
	private GQDialogFrame frame;
	private String ID;
	private String name;
	private InetAddress ip;
	private int port;

	private int DATA_LEN = 4096;
	
	private BufferedReader br;
	
	public GQDlgNet(String ID, GQFriendInfo info){
		this.ID = ID;
		ip = info.getGoOnlineAddress().Ip;
		port = info.getGoOnlineAddress().PORT;
		
	}
	
	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		PrintStream ps = null;
		Scanner scan = null;
		try {
			char[] str = new char[DATA_LEN];
			/*
			 * 创建或者获取可靠套接字 Map<id, socket> 
			 * Map<id, stringBuffer>
			 */
			Socket s;
			if (null == GQFriendsListFrame.getIDAndSocket().get(ID)){
				s = new Socket(ip.getHostAddress(), port);
				ps = new PrintStream(s.getOutputStream());
				String selfIP = new String(GQFriendsListFrame.selfInfo.getGoOnlineAddress().Ip.getHostAddress()
						+ ":" + GQFriendsListFrame.selfInfo.getGoOnlineAddress().PORT);
				ps.println(selfIP);//将自己的ID发送给对方
				
				System.out.println("GQDlgNet:SelfIP:" + selfIP);
				GQFriendsListFrame.addSocketToMap(ID, s);
			}
			s = GQFriendsListFrame.getIDAndSocket().get(ID);
			
			if (null == GQFriendsListFrame.getIDAndBuffMap().get(ID)){
				GQFriendsListFrame.addBuffToMap(ID);
			}
			
			if (null == GQFriendsListFrame.getIDAndSendQueue().get(ID)){
				GQFriendsListFrame.addQueueToMap(ID, new ArrayBlockingQueue<StringBuffer>(6));
			}
			FutureTask<Integer> task = new FutureTask<Integer>(new GQDlgNetSend(ID));
			new Thread(task, ID).start();
			
			
			/*
			 * 创建读取流
			 *
			InputStreamReader in = new InputStreamReader(s.getInputStream());
			br = new BufferedReader(in);
			 */
			scan = new Scanner(s.getInputStream());
			System.out.println(ID + ":waiting for new msg");
			//char[] cb = new char[DATA_LEN];
			while (true) {
				StringBuffer sb = new StringBuffer();
				sb.append(scan.nextLine());
				sb.append("\n");
				System.out.println("GQDlgNet recieve:" + ID + ":" + sb);
				if (null != (frame = GQFriendsListFrame.getIDAndFrame().get(ID))) {
					System.out.println("frame has created");
					frame.addRecvMsgToBuffer(sb);

				} else {
					GQFriendsListFrame.getIDAndBuffMap().get(ID).append(sb);
				}
			}
				/*br.read(cb, 0, DATA_LEN);
				if (null != (frame = GQFriendsListFrame.getIDAndFrame().get(ID))) {
					System.out.println("frame has created");
					frame.addRecvMsgToBuffer(sb.append(cb.toString()));

				} else {
					GQFriendsListFrame.getIDAndBuffMap().get(ID).append(sb);
				}*/
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ps.close();
		scan.close();
		return null;
	}

}
