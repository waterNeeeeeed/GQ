package GQ;


import java.awt.*;


public class GQTest implements Runnable
{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringBuffer str;
			if (null != (str=GQFriendsListFrame.getIDAndBuffMap().get("192.168.194.66:36666"))){
				System.out.println(str);
			}
			else{
				System.out.println("NULL");
			}
			
		}
		
	}
	
}
