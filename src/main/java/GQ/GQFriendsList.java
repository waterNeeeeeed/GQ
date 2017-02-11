package GQ;


import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.BlockingQueue;

import javax.swing.*;
import javax.swing.border.*;


class GQSelfInfo extends JComponent
{
	private final static int DEFAULT_WIDTH = 260;
	private final static int DEFAULT_HEIGHT = 80;
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setPaint(new GradientPaint(0, 0, Color.WHITE, 
				DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.ORANGE));
		g2.fillRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		g2.setPaint(Color.BLACK);
		g2.setFont(new Font("SansSerif", Font.BOLD, 16));
		g.drawString(GQFriendsListFrame.selfInfo.getName(), 30, 30);
		g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
		g.drawString(GQFriendsListFrame.selfInfo.getGoOnlineAddress().Ip.getHostAddress() + ":" + GQFriendsListFrame.selfInfo.getGoOnlineAddress().PORT, 
				30, 50);
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
}

class GQFriendsListCell extends JComponent
	implements ListCellRenderer
{
	private static final int DEFAULT_PRE_SIZE_WIDTH = 156;
	private static final int DEFAULT_PRE_SIZE_HEIGHT = 60 + 20;
	private static final int DEFAULT_PORTRAIT_SIZE = 60;
	
	private Image portrait;
	private Image portrait2;
	private Image SVIP;
	private String name;
	private Border bd;
	//private HashMap<String, GQFriendInfo> info;
	private DefaultListModel<GQFriendInfo> info;
	private Color foreground;
	private Color background;
	
	private int n = 0;
	private int i = 0;
	private int j = 0;
	
	public GQFriendsListCell(){
		this.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
	}
	
	public GQFriendsListCell(DefaultListModel<GQFriendInfo> m){
		this.setBorder(BorderFactory.createEmptyBorder());
		//info = (HashMap<String, GQFriendInfo>)m;
		info = (DefaultListModel<GQFriendInfo>)m;
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, 
			boolean isSelected,
			boolean cellHasFocus) {
		// TODO Auto-generated method stub
		name = ((GQFriendInfo)value).getName();
		portrait2 = ((GQFriendInfo)value).getPortrait();
		SVIP = ((GQFriendInfo)value).getSVIP();
		background = isSelected? list.getSelectionBackground() : list.getBackground();
		foreground = isSelected? list.getSelectionForeground() : list.getForeground();
		
		return this;
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(foreground);
		g.drawImage(portrait2, 6, this.getHeight()/2-30, null);
		g.setColor(Color.BLACK);
		g.drawString(name, portrait2.getWidth(null) + 10 + 6, 20);
		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = g2.getFont().getStringBounds(name, context);
		g.drawImage(SVIP, portrait2.getWidth(null) + 10 + 6 + (int)bounds.getWidth() + 3, 23 - (int)bounds.getHeight() + 2, null);
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(DEFAULT_PRE_SIZE_WIDTH, DEFAULT_PRE_SIZE_HEIGHT);
	}
}

class GQFriendsListFrame extends GQGeneralFrame
{
	/*单例类*/
	private static GQFriendsListFrame instance;
	
	private final static int LISTFRAME_WIDTH = 266;
	private final static int LISTFRAME_HEIGHT = 521;
	private final static int LISTFRAME_MAX_WIDTH = 180;
	private final static int LISTFRAME_MAX_HEIGHT = 600;
	private final static String SOFTWARENAME = "GQ2016";

	private JList<GQFriendInfo> friendsList;
	private static DefaultListModel<GQFriendInfo> newFriendsListModel;
	private JMenu headMenu;
	private GQSelfInfo selfInfoHead;
	
	public static GQFriendInfo selfInfo;
	
	/*
	 * 映射
	 */
	private static Map<String, StringBuffer> IDAndBuff;//ID作为key，socket在buff类中
	private static Map<String, Socket> IDAndSocket;
	private static Map<String, GQDialogFrame> IDAndFrame;
	private static Map<String, GQDlgNet> IDAndNet;//可以被GQConnectResv或者GQ
	private static Map<String, BlockingQueue<StringBuffer>> IDAndSendQueue;
	private static Map<String, GQFriendInfo> IDAndFriendInfo;
	
	private GQFriendsListFrame(GQFriendInfo selfInfo){
		try {
			this.selfInfo = selfInfo;
			newFriendsListModel = new DefaultListModel<>();
			friendsList = new JList(newFriendsListModel);
			friendsList.setVisibleRowCount(2);
			friendsList.setCellRenderer(new GQFriendsListCell(newFriendsListModel));
			friendsList.setBorder(BorderFactory.createEmptyBorder());
			
			IDAndSocket = new HashMap<>();
			IDAndBuff = new HashMap<>();//每个friend都有一个buff，用与接收他们的信息
			IDAndFrame = new HashMap<>();
			IDAndNet = new HashMap<>();
			IDAndSendQueue = new HashMap<>();
			IDAndFriendInfo = new HashMap<>();
			addMouseDoubleClickedToJList(friendsList);
			
			/* only for test 
			addFriendInfoToList("gongT", Gender.MALE, 36666);
			addFriendInfoToList("panX", Gender.FEMALE, 38888);*/
			/*
			 * 开启NET POOL, TCP UDP
			 */
			try{
				//NetPool = Executors.newCachedThreadPool();
				new Thread(new GQNetStart(this)).start();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			/* 添加标头 */
			Box MenuBox = Box.createHorizontalBox();
			JMenuBar headMenuBar = new JMenuBar();
			JMenu mainMenu = new JMenu("主菜单");
			mainMenu.setMenuLocation(10, 10);
			mainMenu.setComponentPopupMenu(mainMenu.getPopupMenu());
			JMenuItem exit = new JMenuItem("exit");
			exit.addActionListener(l->{
				System.exit(0);
			});
			mainMenu.add(exit);
			headMenuBar.add(mainMenu);
			MenuBox.add(headMenuBar);
			MenuBox.add(Box.createHorizontalGlue());
			
			selfInfoHead = new GQSelfInfo();
			selfInfoHead.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.ORANGE));
			
			add(selfInfoHead, BorderLayout.NORTH);
			add(new JScrollPane(friendsList), BorderLayout.CENTER);
			add(MenuBox, BorderLayout.SOUTH);
			
			/**/
			if (SystemTray.isSupported())
			{
				this.addWindowListener(new WindowAdapter(){
					@Override
					public void windowIconified(WindowEvent e){
						setVisible(false);
					}
				});
				
				this.setResizable(false);
				
				SystemTray sysTray = SystemTray.getSystemTray();
				Image image = new ImageIcon("image/Welcome_16px_1071652_easyicon.net.png").getImage();
				PopupMenu pop = new PopupMenu();
				MenuItem popExitItem = new MenuItem("exit");
				pop.add(popExitItem);
				popExitItem.addActionListener(l->{
					System.exit(0);
				});
				TrayIcon ti = new TrayIcon(image, SOFTWARENAME, pop);
				sysTray.add(ti);
				
				ti.addMouseListener(new MouseAdapter(){
					@Override
					public void mouseClicked(MouseEvent e){
						if (e.BUTTON1 == e.getButton()){
							show();
							setExtendedState(JFrame.NORMAL);
						}
					}
				});
			}
			
			//pack();
			this.setAlwaysOnTop(true);
			this.setBounds(20, 20, LISTFRAME_WIDTH, LISTFRAME_HEIGHT);
			setVisible(true);
			setDefaultCloseOperation(EXIT_ON_CLOSE);

			/*????
			this.setMaximumSize(new Dimension(LISTFRAME_MAX_WIDTH, LISTFRAME_MAX_HEIGHT));
			this.setPreferredSize(new Dimension(LISTFRAME_WIDTH, LISTFRAME_HEIGHT));*/

		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/*
	 * 双击打开对话，如果Map id frame 已有，则show
	 * 没有则create
	 */
	public void addMouseDoubleClickedToJList(JList jl){
		jl.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				if (2 == e.getClickCount())
				{
					try {
						GQFriendInfo info = (GQFriendInfo)((JList)e.getSource()).getSelectedValue();
						String id = createID(info.getGoOnlineAddress().Ip, info.getGoOnlineAddress().PORT);
						GQDialogFrame dlg = IDAndFrame.get(id);
						if (null == dlg){
							dlg = new GQDialogFrame(info);
							GQFriendsListFrame.addDlgFrameToMap(id, dlg);
							//IDAndFrame.put(id, dlg);
						}
						else{
							dlg.show();
						}
					} 
					catch (Exception exc) {
						exc.printStackTrace();
					}
				}
					
			}
		});
	}
	
	/*
	 * only for test 
	 */
	public static void addFriendInfoToList(String name, Gender g, int PORT) throws UnknownHostException//, DefaultListModel<GQFriendInfo> list)
	{
		/**/
		Image portrait;
		if (g == Gender.MALE)
			portrait = new ImageIcon("image/ali-male.jpg").getImage();
		else
			portrait = new ImageIcon("image/ali-female.jpg").getImage();
		Image im = new ImageIcon(portrait.getScaledInstance(60, -1, Image.SCALE_SMOOTH)).getImage();
		Image SVIP = new ImageIcon("image/SVIP.png").getImage();
		/**/
		GQFriendInfo person = new GQFriendInfo();
		person.setGender(g);
		person.setName(name);
		person.setPortrait(im);
		person.setSVIP(SVIP);
		//test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
		person.setGoOnlineAddress(new OnlineAddressID(InetAddress.getLocalHost(), PORT));
		//addToIDAndBufferMAP(createID(InetAddress.getLocalHost(), PORT));
		addElementToFriendsListModel(person);
		addFriendInfoToMap(createID(InetAddress.getLocalHost(), PORT), person);
	}
	
	/*
	 * 添加朋友信息到列表中的同时，根据IP：PORT创建ID并加入ID BUFFER映射MAP中
	 */
	public static void addFriendInfoToList(GQFriendInfo info) throws UnknownHostException//, DefaultListModel<GQFriendInfo> list)
	{
		/**/
		Image portrait;
		if (info.getGender() == Gender.MALE)
			portrait = new ImageIcon("image/ali-male.jpg").getImage();
		else
			portrait = new ImageIcon("image/ali-female.jpg").getImage();
		Image im = new ImageIcon(portrait.getScaledInstance(60, -1, Image.SCALE_SMOOTH)).getImage();
		Image SVIP = new ImageIcon("image/SVIP.png").getImage();
		/**/
		GQFriendInfo person = new GQFriendInfo();
		person.setGender(info.getGender());
		person.setName(info.getName());
		person.setSign(info.getSign());
		person.setPortrait(im);
		person.setSVIP(SVIP);
		person.setGoOnlineAddress(info.getGoOnlineAddress());
		//addToIDAndBufferMAP(createID(info.getGoOnlineAddress().Ip, info.getGoOnlineAddress().PORT));
		addElementToFriendsListModel(person);
		addFriendInfoToMap(createID(info.getGoOnlineAddress().Ip, info.getGoOnlineAddress().PORT), person);
	}
	
	/*
	 * 	private static Map<String, StringBuffer> IDAndBuff;//ID作为key，socket在buff类中
	 *	private static Map<String, Socket> IDAndSocket;
	 *	private static Map<String, GQDialogFrame> IDAndFrame;
	 *	private static Map<String, GQDlgNet> IDAndNet;
	 * 	private static Map<String,  FutureTask<Integer>> IDAndNet;
	 *	private static Map<String, BlockingQueue<StringBuffer>> IDAndSendQueue;
	 */
	public static Map<String, GQFriendInfo> getIDAndFriendInfo(){
		return IDAndFriendInfo;
	}
	
	public static void addFriendInfoToMap(String id, GQFriendInfo info){
		IDAndFriendInfo.put(id, info);
	}
	
	public static Map<String, BlockingQueue<StringBuffer>> getIDAndSendQueue(){
		return IDAndSendQueue;
	}
	
	public static void addQueueToMap(String id, BlockingQueue<StringBuffer> que){
		IDAndSendQueue.put(id, que);
	}
	
	public static Map<String,  GQDlgNet> getIDAndNetMap(){
		return IDAndNet;
	}
	
	public static void addNetTaskToMap(String id, GQDlgNet task){
		IDAndNet.put(id, task);
	}
	
	public static Map<String, StringBuffer> getIDAndBuffMap(){
		return IDAndBuff;
	}
	
	public static void addBuffToMap(String id){
		IDAndBuff.put(id, new StringBuffer());
	}
	
	public static Map<String, Socket> getIDAndSocket(){
		return IDAndSocket;
	}
	
	public static void addSocketToMap(String id, Socket s){
		IDAndSocket.put(id, s);
	}
	
	public static Map<String, GQDialogFrame> getIDAndFrame(){
		return IDAndFrame;
	}
	
	public static void addDlgFrameToMap(String id, GQDialogFrame frame){
		IDAndFrame.put(id, frame);
	}
	
	public static DefaultListModel<GQFriendInfo> getListModel(){
		return newFriendsListModel;
	}
	/*
	 * 添加ID BUFFER映射
	 
	public static void addToIDAndBufferMAP(String id){
		IDAndBuff.put(id, new StringBuffer());
	}*/
	
	public static String createID(InetAddress ip, int PORT){
		return new String(ip.getHostAddress() + ":" + PORT);
	}
	
	public static void addElementToFriendsListModel(GQFriendInfo element){
		newFriendsListModel.addElement(element);
	}
	
	public static GQFriendInfo getElementFromFriendsListModel(int index){
		return newFriendsListModel.getElementAt(index);
	}
	
	public static int getSizeFromFriendsListModel(){
		return newFriendsListModel.getSize();
	}
	
	public static GQFriendsListFrame getInstance(GQFriendInfo selfInfo){
		if (instance == null){
			instance = new GQFriendsListFrame(selfInfo);
		}
		return instance;
	}
	
}

public class GQFriendsList extends GQGeneralThread
{
	@Override
	public void init(GQFriendInfo selfInfo){
		GQFriendsListFrame.getInstance(selfInfo).show();
	}
}
