package axohEngine2.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import axohEngine2.Judgement;
import axohEngine2.Main;
import axohEngine2.project.STATE;

public class LoadGame extends JPanel {
	
	private static final long serialVersionUID = -7767308362370253035L;
	
	Judgement judge;
	Main main;
	JFrame frame;
	File folder = new File("data/");
	File[] files = folder.listFiles();
	JPanel buttons = new JPanel(new GridLayout(4,0,10,10));
	Color bg = new Color(81, 122, 166);
	Color fg = Color.white;
	JButton one = new JButton("Empty");
	JButton two = new JButton("Empty");
	JButton three = new JButton("Empty");
	JButton four = new JButton("Empty");
	Font titleFont;
	
	public LoadGame(Judgement j, Main m, JFrame f){
		
		this.setFocusable(true);
		
		frame = f;
		judge = j;
		main = m;

		this.setLayout(null);

		addButtons();
		add(buttons);
	}
	

	
	public void addButtons(){
				
		buttons.setOpaque(false);

		//Font font = new Font("Arial", Font.BOLD, main.winWidth/40);
		
		one.setBackground(bg);
		two.setBackground(bg);
		three.setBackground(bg);
		four.setBackground(bg);
		
		one.setForeground(fg);
		two.setForeground(fg);
		three.setForeground(fg);
		four.setForeground(fg);
		
		one.setFont(main.titleFont);
		two.setFont(main.titleFont);
		three.setFont(main.titleFont);
		four.setFont(main.titleFont);
		
		buttons.add(one);
		buttons.add(two);
		buttons.add(three);
		buttons.add(four);
				
		if(files.length > 0){
			
			if(files[0] != null){
				one.setText(files[0].getName().replace(".ser", ""));
				one.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						main.loadState(files[0]);
						judge.setFile(files[0]);
						judge.setState(STATE.GAME);
						load(files[0].getName().replace(".ser", ""));
					}
					
				});
			}
			if(files.length > 1){
				two.setText(files[1].getName().replace(".ser", ""));
				two.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						main.loadState(files[1]);
						judge.setFile(files[1]);
						judge.setState(STATE.GAME);
						load(files[1].getName().replace(".ser", ""));
					}
					
				});
				
			}
			if(files.length > 2){
				three.setText(files[2].getName().replace(".ser", ""));
				three.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						main.loadState(files[2]);
						judge.setFile(files[2]);
						judge.setState(STATE.GAME);
						load(files[2].getName().replace(".ser", ""));
					}
					
				});
				}
			if(files.length  > 3){
				four.setText(files[3].getName().replace(".ser", ""));
				four.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						main.loadState(files[3]);
						judge.setFile(files[3]);
						judge.setState(STATE.GAME);
						load(files[3].getName().replace(".ser", ""));
					}
					
				});
				}
			
		}	
		
		buttons.setBounds(main.winWidth/4,main.winHeight/3,main.winWidth/2,main.winHeight/2);
	}
	

	

	
	public void paintComponent(Graphics g){
		BufferedImage in;
		try {
			in = ImageIO.read(new File("res/menus/titlemenu1.png"));
			new BufferedImage(2000,2000, BufferedImage.TYPE_INT_ARGB);
				g.drawImage(in, 0, 0, this.getWidth(), this.getHeight(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.setFont(new Font("Arial",Font.PLAIN, this.getWidth()/10));
		g.setFont(titleFont);
		g.setColor(Color.white);
		//g.drawString("Load Game", this.getWidth()/4, this.getHeight()/4);
		
		//Change button font size and bounds when the screen is resized
		one.setFont(main.titleFont);
		two.setFont(main.titleFont);
		three.setFont(main.titleFont);
		four.setFont(main.titleFont);
		
		buttons.setBounds(main.winWidth/4,main.winHeight/3,main.winWidth/2,main.winHeight/2);
	}
	
	public void load(String s){
		judge.setPlayerName(s);
		judge.setFile(new File("data/"+ s + ".ser"));
		main.setLayout("Judge");
		judge.setFocusable(true);
		judge.setState(STATE.GAME);
		main.changeSize();
		main.setLocation(0, 0);
		judge.startTimer();
		main.setLocation(4);
	}
	
}
