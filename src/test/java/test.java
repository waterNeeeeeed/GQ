import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

class XComponent extends JComponent
{
	private final static int width = 30;
	private final static int width_zoomin = 6;
	private Color border;
	public XComponent(){
		this.setSize(new Dimension(width, width));
		border = new Color(0, 80, 255);
	}
	
	public void setColor(Color c){
		border = c;
	}
	
	@Override
	public void paintComponent(Graphics g){
		System.out.println("paintX");
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

class GQStartFrameBackGround extends JComponent
{
	@Override
	public void paintComponent(Graphics g){
		//super.paintComponent(g);
		System.out.println("GQStartFrameBackGround");
		Graphics2D g2 = (Graphics2D)g;
		g2.setPaint(new GradientPaint(0, 0, Color.ORANGE,  
				260, 260, Color.RED));
		g2.fillRect(0, 0, 500, 500);
		
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(60, 60);
	}
}

class JButtonSelf extends AbstractButton
{
	
}

public class test extends JFrame
{
	public test(){
		/*JPanel jp = new JPanel();
		jp.setSize(new Dimension(60, 60));*/
		/*JButton x = new JButton("x");
		x.setLocation(60, 60);
		x.setVisible(true);
		System.out.println(x.getSize());
		add(x);*/
		
		JLayeredPane test = new JLayeredPane();
		//test.add(x, JLayeredPane.MODAL_LAYER);
		GQStartFrameBackGround bg = new GQStartFrameBackGround();
		
		XComponent x = new XComponent();
		
		bg.setBounds(0, 0, 99, 99);
		
		//bg.setLocation(0, );
		
		
		//x.setBounds(60, 60, 61, 61);
		x.setLocation(66, 66);
		/*����ӵĺ���ƣ���������������������������������������������������*/
		
		test.add(x, 7);
		test.add(bg, 6);
		//test.add(x, 78);
		
		test.setPreferredSize(new Dimension(60, 60));
		x.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				x.setColor(Color.orange);
				x.repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent e){
				x.setColor(new Color(0, 80, 255));
				x.repaint();
			}
			
			@Override
			public void mouseClicked(MouseEvent e){
				System.exit(0);
			}
			
		});
		/*test.setVisible(true);
		test.setPreferredSize(new Dimension(600, 600));
		add(test);*/
		/*this.setUndecorated(true);
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);*/
		//this.setContentPane();
		add(test);
		this.setLocation(300, 300);
		//this.setBounds(300, 300, 300, 300);
		pack();
		this.setSize(new Dimension(300, 300));
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args){
		new test();
	}
}
