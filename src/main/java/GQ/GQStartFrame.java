package GQ;



import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.util.concurrent.*;

import javax.swing.*;


/*不能实例化*/
abstract class GQGeneralThread
{
	abstract public void init(GQFriendInfo selfInfo);
}

abstract class GQGeneralFrame extends JFrame
{
	public void setLocationByScreenSize(int frameWidth, int frameHeight){
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(d.width/2-frameWidth/2, d.height/2-frameHeight/2);
	}
}

class XComponent extends JComponent
{
	private final static int width = 25;
	private final static int width_zoomin = 6;
	private Color border;
	
	public XComponent(){
		this.setSize(new Dimension(width, width));
		border = Color.white;//new Color(0, 80, 255);
		init();
	}
	
	public void init(){
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				setColor(Color.orange);
				repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent e){
				setColor(Color.white);//new Color(0, 80, 255));
				repaint();
			}
			
			@Override
			public void mouseClicked(MouseEvent e){
				System.exit(0);
			}
			
		});
	}
	
	public void setColor(Color c){
		border = c;
	}
	
	@Override
	public void paintComponent(Graphics g){
		//System.out.println("paintX");
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(border);
		/*g2.setStroke(new BasicStroke(1.0f));
		g2.drawRect(1, 1, width, width);*/
		Color tmp = (Color)g2.getPaint();
		if (border.equals(Color.orange))
		{
			g2.setPaint(Color.red);
			g2.fillRect(1, 1, width, width);
		}
		g2.setPaint(tmp);
		//g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.drawLine(1+width_zoomin, 1+width_zoomin, width-width_zoomin, width-width_zoomin);
		g2.drawLine(1+width_zoomin, 1+width_zoomin-1, width-width_zoomin, width-width_zoomin-1);
		g2.drawLine(1+width_zoomin, 1+width_zoomin+1, width-width_zoomin, width-width_zoomin+1);
		//g2.setStroke(new BasicStroke(1.0f/*3.535f*/, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.drawLine(width-width_zoomin, 1+width_zoomin, 1+width_zoomin, width-6);
		g2.drawLine(width-width_zoomin, 1+width_zoomin-1, 1+width_zoomin, width-6-1);
		g2.drawLine(width-width_zoomin, 1+width_zoomin+1, 1+width_zoomin, width-6+1);
		
	}
	
	public Dimension getPreferedSize(){
		return new Dimension(width, width);
	}
}

class __Component extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static int width = 25;
	private final static int width_zoomin = 6;
	private Color border;
	private GQStartFrame frame;
	
	public __Component(GQStartFrame frame){
		this.setSize(new Dimension(width, width));
		border = Color.white;//new Color(0, 80, 255);
		init();
		this.frame = frame;
	}
	
	public void init(){
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				setColor(Color.orange);
				repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent e){
				setColor(Color.white);//new Color(0, 80, 255));
				repaint();
			}
			
			@Override
			public void mouseClicked(MouseEvent e){
				frame.setExtendedState(JFrame.ICONIFIED);
			}
			
		});
	}
	
	public void setColor(Color c){
		border = c;
	}
	
	@Override
	public void paintComponent(Graphics g){
		//System.out.println("paintX");
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(border);
		/*g2.setStroke(new BasicStroke(1.0f));
		g2.drawRect(1, 1, width, width);*/
		Color tmp = (Color)g2.getPaint();
		if (border.equals(Color.orange))
		{
			g2.setPaint(Color.red);
			g2.fillRect(1, 1, width, width);
		}
		g2.setPaint(tmp);
		//g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.drawLine(1+width_zoomin, width/2-1, width-width_zoomin, width/2-1);
		g2.drawLine(1+width_zoomin, width/2, width-width_zoomin, width/2);
		g2.drawLine(1+width_zoomin, width/2+1, width-width_zoomin, width/2+1);
		
	}
	
	public Dimension getPreferedSize(){
		return new Dimension(width, width);
	}
}
class SelfImageComponent extends JComponent
{
	private final int WIDTH = 399;
	private final int HEIGHT = 120;
	private final int IMAGESIZE = 80; //60*60
	private Image selfImage;
	private int startFrameWidth;
	
	public SelfImageComponent(int w,Image im){
		/*if (im != null){
			
		}*/
		startFrameWidth = w;
		if (im.getWidth(null) != IMAGESIZE)
			selfImage = new ImageIcon(im.getScaledInstance(IMAGESIZE, IMAGESIZE, 
					Image.SCALE_SMOOTH)).getImage();
		else
			selfImage = im;
		
		
		
		//this.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
	}
	
	@Override
	public void paintComponent(Graphics g){
		int imageX = startFrameWidth/2-selfImage.getWidth(null)/2-3;
		int imageY = 10; 
		//g.setColor(Color.GRAY);
		//g.fillRect(imageX+4, imageY+4, IMAGESIZE, IMAGESIZE);
		/*Graphics2D g2 = (Graphics2D)g;
		//g2.setPaint(new GradientPaint(imageX+30, imageY+30, Color.ORANGE,  
		//		imageX+30+IMAGESIZE, imageY+30+IMAGESIZE, Color.RED));
		g2.setPaint(new GradientPaint(0, 0, Color.ORANGE,  
				WIDTH, HEIGHT, Color.RED));
		//g2.fillRect(imageX+4, imageY+4, IMAGESIZE, IMAGESIZE);
		g2.fillRect(0, 0, WIDTH, HEIGHT);*/
		g.drawImage(selfImage, imageX, imageY, null);
		
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(WIDTH, HEIGHT);
	}
}

class GQStartFrameBackGround extends JPanel
{
	public GQStartFrameBackGround(){
		
	}
	
	@Override
	public void paintComponent(Graphics g){
		//System.out.println("paint");
		Graphics2D g2 = (Graphics2D)g;
		g2.setPaint(new GradientPaint(GQStartFrame.STARTFRAME_WIDTH/2, 0, Color.LIGHT_GRAY,  
				GQStartFrame.STARTFRAME_WIDTH/2, GQStartFrame.STARTFRAME_HEIGHT/2+52, Color.white));
		
		g2.fillRect(0, 0, 500, GQStartFrame.STARTFRAME_HEIGHT/2);
		
		g2.setPaint(new GradientPaint(GQStartFrame.STARTFRAME_WIDTH/2, GQStartFrame.STARTFRAME_HEIGHT/2, Color.white,  
				GQStartFrame.STARTFRAME_WIDTH/2, GQStartFrame.STARTFRAME_HEIGHT-20, Color.LIGHT_GRAY));
		
		g2.fillRect(0, GQStartFrame.STARTFRAME_HEIGHT/2, 500, GQStartFrame.STARTFRAME_HEIGHT/2);
	}
	
	/*@Override
	public void paintComponent(Graphics g){
		//this.re
	}*/
}

public class GQStartFrame extends GQGeneralFrame
{
	private final static int SELFIMAGESIZE = 80;
	private final static String imagAddr = "image/self.jpg";
	public final static int STARTFRAME_WIDTH = 360;
	public final static int STARTFRAME_HEIGHT = 260;
	
	/*整体布局*/
	private Box startBox;
	private Image selfImage;
	private Box nameBox;
	private JLabel nameLb;
	private JTextField name;
	private JComboBox<String> gender;
	private JButton confirmBtn;
	private JButton exitBtn;
	private Box confirmBox;
	private GQFriendInfo selfInfo;
	
	//private final static int PORT = 36666;//gongT;//TCP server
	private final static int PORT = 38888;//panX
	
	public GQStartFrame(){
		try {
			init();
			this.setTitle("GQ2016");
			selfInfo = new GQFriendInfo();
			startBox = Box.createVerticalBox();
			selfImage = setDefaultSelfImage();

			nameBox = Box.createHorizontalBox();
			name = new JTextField();
			name.setToolTipText("请输入您的大名，O(∩_∩)O~");
			String[] genders = new String[] { "男", "女" };
			gender = new JComboBox<>(genders);
			
			nameLb = new JLabel("姓名:");
			nameBox.add(Box.createHorizontalStrut(15));
			nameBox.add(nameLb);
			nameBox.add(name);
			nameBox.add(gender);
			nameBox.add(Box.createHorizontalStrut(26));

			confirmBox = Box.createHorizontalBox();
			confirmBtn = new JButton("确认");
			exitBtn = new JButton("退出");
			addConfirmBtnListener(confirmBtn, new GQFriendsList());
			exitBtn.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					System.exit(0);
				}
			});

			
			startBox.add(new SelfImageComponent(STARTFRAME_WIDTH, selfImage));
			startBox.add(Box.createVerticalStrut(6));
			startBox.add(nameBox);
			startBox.add(Box.createVerticalStrut(15));
			confirmBox.add(Box.createHorizontalStrut(26));
			confirmBox.add(exitBtn);
			confirmBox.add(confirmBtn);
			confirmBox.add(Box.createHorizontalStrut(26));
			startBox.add(confirmBox);
			startBox.setBounds(0, 30, 360, 189);
			
			//去掉标题框并增加背景色
			this.setUndecorated(true);
			JLayeredPane test = new JLayeredPane();
			GQStartFrameBackGround b = new GQStartFrameBackGround();
			b.setBounds(0, 0, 600, 600);
			
			/*增加自绘制x退出按钮*/
			XComponent x = new XComponent();
			x.setLocation(STARTFRAME_WIDTH-30, 0);
			
			__Component __minimizedBtn = new __Component(this);
			__minimizedBtn.setLocation(STARTFRAME_WIDTH-60, 0);
			
			/*设置三层窗格*/
			test.add(b, JLayeredPane.DEFAULT_LAYER);
			test.add(x, JLayeredPane.DRAG_LAYER);
			test.add(__minimizedBtn, JLayeredPane.DRAG_LAYER);
			test.add(startBox, JLayeredPane.MODAL_LAYER);
			test.setPreferredSize(new Dimension(STARTFRAME_WIDTH, STARTFRAME_HEIGHT));
			test.setVisible(true);
			//pack();
			add(test);
			
			setSize(STARTFRAME_WIDTH, STARTFRAME_HEIGHT);
			//pack()
			this.setResizable(false);
			this.setAlwaysOnTop(true);
			setVisible(true);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setLocationByScreenSize(STARTFRAME_WIDTH, STARTFRAME_HEIGHT);
			this.setIconImage(selfImage);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addConfirmBtnListener(JButton con, GQGeneralThread gl){
		con.addActionListener(eve->{
			if (this.name.getText().isEmpty())
				selfInfo.setName("自己");
			else
				selfInfo.setName(this.name.getText());
			selfInfo.setPortrait(this.selfImage);
			if ("男" == (String)gender.getSelectedItem())
				selfInfo.setGender(Gender.MALE);
			else
				selfInfo.setGender(Gender.FEMALE);
			/*
			 * 设置IP 和 PORT
			 */
			try {
				selfInfo.setGoOnlineAddress(new OnlineAddressID(InetAddress.getLocalHost(),
						PORT));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			FutureTask<String> task = new FutureTask<>((Callable<String>)()->{
				gl.init(selfInfo);
				return "";
			});
			new Thread(task, "").start();
			this.hide();
		});
	}
	
	public Image setDefaultSelfImage(){
		return new ImageIcon(new ImageIcon(imagAddr).getImage()
				.getScaledInstance(SELFIMAGESIZE, SELFIMAGESIZE, Image.SCALE_SMOOTH)).getImage();
	}
	
	public void init(){
		try{
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this.getContentPane());/////////////
		}
		catch(Exception e){
			
		}
	}
	
	public static void main(String[] args){
		new GQStartFrame();
	}
}
