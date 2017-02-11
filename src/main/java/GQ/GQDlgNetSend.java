package GQ;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class GQDlgNetSend implements Callable<Integer>
{
	private String ID;
	private int DATA_LEN = 4096;
	private BufferedWriter bw;
	
	
	public GQDlgNetSend(String id){
		this.ID = id;
	}
	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		/*
		 * 创建发送流
		 */
		Socket s;
		BlockingQueue<StringBuffer> bq;
		if (null == (bq = GQFriendsListFrame.getIDAndSendQueue().get(ID))){
			System.out.println("GQDlgNetSend: getIDAndSendQueue failed");
			return 1;
		}
		
		if (null == (s = GQFriendsListFrame.getIDAndSocket().get(ID))){
			System.out.println("GQDlgNetSend: getIDAndSocket failed");
			return 1;
		}
		
		/*OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
		bw = new BufferedWriter(out);
		bw.write("hello");
		bw.wr*/
		PrintStream ps = new PrintStream(s.getOutputStream());
		//char[] cb = new char[DATA_LEN];
		while (true){
			try{
				String tmp;
				tmp = bq.take().toString();
				ps.print(tmp);
				ps.flush();// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				//cb = bq.take().toString().toCharArray();
				/*
				 * Prints a String and then terminate the line. 
				 * This method behaves as though it invokes print(String) and then println().
				 * !!
				 */
				
				//bw.write(cb, 0, cb.length);
				/*System.out.println("Msg:" + tmp + "Send!");*/
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
