package GQ;


import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javax.swing.*;


public class GQDialogFrame extends GQGeneralFrame
{
	private final static int DEFAULT_FRAME_WIDTH = 300;
	private final static int DEFAULT_FRAME_HEIGHT = 300;

	private Box frameLeftBox;
	private Box frameRightBox;
	private JEditorPane showPane;
	private JTextArea inputArea;
	private JSplitPane leftSP;
	private JButton sendBtn;
	private Box sendBox;
	private GQFriendInfo info;
	private OnlineAddressID friendAddr;
	private Socket sock;//通讯线程返回的SOCKET
	//private StringBuffer textBuff;
	private String ID;
	
	public GQDialogFrame(GQFriendInfo info){
		this.info = info;
		frameLeftBox = Box.createVerticalBox();
		sendBox = Box.createHorizontalBox();
		showPane = new JTextPane();
		//showPane.setText("<html><i>my love</i></html>");
		//textBuff = new StringBuffer();
		showPane.setEditable(false);
		sendBtn = new JButton("<html><i>发送</i></html>");//"发送");
		sendMessageListener(sendBtn);
		inputArea = new JTextArea(3, 18);
		sendBox.add(new JScrollPane(inputArea));
		sendBox.add(sendBtn);
		leftSP = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
				new JScrollPane(showPane), sendBox);
		leftSP.setDividerLocation(200);
		add(leftSP);
		
		/*
		 * 将自身加入MAP id frame
		 * MAP ID net
		 */
		ID = GQFriendsListFrame.createID(info.getGoOnlineAddress().Ip, info.getGoOnlineAddress().PORT);
		if (null == GQFriendsListFrame.getIDAndFrame().get(ID)){
			GQFriendsListFrame.addDlgFrameToMap(ID, this);
		}
		/*
		 * 如果没有对应线程则建立新的线程负责与对方建立连接
		 * GQDlgNet可被本类及GQConnectRecv类创建，故需要检查
		
		if (null == GQFriendsListFrame.getIDAndSendQueue().get(ID)){
			GQFriendsListFrame.addQueueToMap(ID, new ArrayBlockingQueue<StringBuffer>(6));
		} */
		
		if (null == GQFriendsListFrame.getIDAndNetMap().get(ID)){
			GQDlgNet dlgnet = new GQDlgNet(ID, this.info);
			FutureTask<Integer> task = new FutureTask<Integer>(dlgnet);
			GQFriendsListFrame.addNetTaskToMap(ID, dlgnet);
			new Thread(task).start();
			System.out.println("GQDialogFrame create DLG NET:" + ID);
		}
		
		if (null == GQFriendsListFrame.getIDAndBuffMap().get(ID)){
			GQFriendsListFrame.addBuffToMap(ID);
			showPane.setText(GQFriendsListFrame.getIDAndBuffMap().get(ID).toString());//显示BUFF里的存货
		}
		
		setTitle("与" + info.getName() + "对话中");
		setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setLocationByScreenSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
	}
	
	public void setSocket(Socket s){
		this.sock = s;
	}
	
	public GQFriendInfo getFriendInfo(){
		return this.info;
	}
	
	public void sendMessageListener(JButton sendBtn){
		sendBtn.addActionListener(lis->{
			StringBuffer s = new StringBuffer();
			//添加到本地
			s = addLocalContentToBuffer(GQFriendsListFrame.selfInfo.getName(), inputArea.getText());		
			//发送给对方
			inputArea.setText(null);
			sendLocalMsgToOther(s);
		});
	}
	
	public void sendLocalMsgToOther(StringBuffer s){
		try{
			/*
			 * 5000 milliseconds timeout
			 */
			GQFriendsListFrame.getIDAndSendQueue().get(ID).offer(s, 5000, TimeUnit.MILLISECONDS);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public StringBuffer addLocalContentToBuffer(String name, String newText){
		StringBuffer s = new StringBuffer();
		LocalDateTime ld = LocalDateTime.now();
		s.append(name + " " + "(" + ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd" + " " + "HH:mm:ss")) + ")" + "\n");
		s.append(newText + "\n");
		addContentToBuffer(s);
		return s;
	}
	
	public void addRecvMsgToBuffer(StringBuffer s){
		addContentToBuffer(s);
	}
	
	/*方法同步，避免确认按钮与接受线程同时调用*/
	public synchronized void addContentToBuffer(StringBuffer str){
		//new version
		showPane.setText(GQFriendsListFrame.getIDAndBuffMap().get(ID).append(str).toString());
		//textBuff.append(str);
		//showPane.setText(textBuff.toString());//显示出来
	}
	
}
