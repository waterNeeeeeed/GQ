package GQ;


import javax.swing.DefaultListModel;


/*
 * 包含一个TCP收数据的线程和一个UDP发局域网在线的线程
 */
public class GQNetStart 
	implements Runnable
{
	private DefaultListModel<GQFriendInfo> listModel;
	private GQFriendsListFrame frame;
	
	public GQNetStart(GQFriendsListFrame frame){
		this.frame = frame;
		this.listModel = frame.getListModel();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		/*
		 * ServerTCP首先建立TCP server 接收同局域网朋友的连接请求
		 */
		try{
			new Thread(new GQConnectRecv(frame)).start();
		}
		catch(Exception e){
			
		}
		
		/*
		 * MultiCast， 将自己的INFO广播到局域网，
		 * 并将新朋友的INFO加入LIST，通过主框架的静态方法
		 * 
		 */
		/*try {
			//listModel.isEmpty();
			new Thread(new GQGoOnlineMulticast(listModel), "UDP");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
