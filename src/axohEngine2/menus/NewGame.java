package axohEngine2.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import axohEngine2.Judgement;
import axohEngine2.Main;

public class NewGame extends JPanel {
	
	private static final long serialVersionUID = -7767308362370253035L;
	
	Judgement judge;
	Main main;
	JFrame frame;
	String name = "";
	
	public NewGame(Judgement j, Main m, JFrame f){	
		frame = f;
		judge = j;
		main = m;
	}
	
	
	public void paintComponent(Graphics g){
		BufferedImage in;
		try {
			in = ImageIO.read(new File("res/menus/newGameMenu.png"));
			new BufferedImage(1920,1080, BufferedImage.TYPE_INT_ARGB);
				g.drawImage(in, 0, 0, this.getWidth(), this.getHeight(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.setFont(main.titleFont);
		g.setColor(Color.white);
		g.drawString(name, main.winWidth/4, main.winHeight/2+main.winHeight/10);
		}
	
	public void addKey(char c){
		if(name.length()<10){
			name = name + c;
		}
	}
	
	public String getName(){
		return name;
	}
	
	public void removeKey(){
		name = name.substring(0,name.length()-1);
	}
	
	public void clearName(){
		name = "";
	}
}
