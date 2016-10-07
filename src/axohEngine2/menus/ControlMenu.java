package axohEngine2.menus;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import axohEngine2.Judgement;
import axohEngine2.Main;

@SuppressWarnings("serial")
public class ControlMenu extends JPanel {
		
	Judgement judge;
	Main main;
	JFrame frame;

	public ControlMenu(Judgement j, Main m, JFrame f){
		this.setFocusable(true);
		frame = f;
		judge = j;
		main = m;		
	}
	
	public void paintComponent(Graphics g){
		BufferedImage in;
		try {
			in = ImageIO.read(new File("res/menus/controlBackground.png"));
			new BufferedImage(2000,2000, BufferedImage.TYPE_INT_ARGB);
				g.drawImage(in, 0, 0, this.getWidth(), this.getHeight(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
