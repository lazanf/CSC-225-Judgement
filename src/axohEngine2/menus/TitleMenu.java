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
import javax.swing.JTextField;

import axohEngine2.Judgement;
import axohEngine2.Main;

/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 * Date: July 5, 2015
 * <p>
 * Title: Title Menu
 * Description: Create a title menu with a graphic and options to load/ssave/delete a file
 * <p>
 * TODO: Create option of deleting a file from the menu
 * <p>
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/

public class TitleMenu extends JPanel {

	private static final long serialVersionUID = 816397745167982L;
	
	
		Judgement judge;
		Main main;
		JFrame frame;
		JTextField field = new JTextField(10);
		JPanel textPanel;
		boolean saveFilesExist = false;
		int numOfSavedFiles = 0;
		JPanel buttons = new JPanel(new GridLayout(2,0,20,20));
		Font font;
		Font customFont;
		JButton newgame = new JButton("New Game");
		JButton loadgame = new JButton("Load Game");
		boolean tryLoad = false;
		Color bg = new Color(81, 122, 166);
		Color fg = Color.white;

		
		public TitleMenu(Judgement j, Main m, JFrame f){
			
			frame = f;
			judge = j;
			main = m;
			

			
			File folder = new File("data/");
			
			if(folder.isDirectory() && folder.listFiles().length > 0){
				System.out.println("Found save files");
				numOfSavedFiles = folder.listFiles().length;
				saveFilesExist = true;
			}
			
			init();
		}
		

		
		public void init(){
			
					
			this.setLayout(null);
			
			buttons.setOpaque(false);
			Font font = new Font("Arial", Font.BOLD, main.winWidth/20);
			newgame.setFont(font);
			loadgame.setFont(font);
			
			newgame.setBackground(bg);
			loadgame.setBackground(bg);
			
			newgame.setForeground(fg);
			loadgame.setForeground(fg);
			
			newgame.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					if(numOfSavedFiles < 4){
						main.setLayout("New");
						main.setLocation(1);
					}
				}
				
			});
			
			loadgame.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					if(saveFilesExist){
					main.setLayout("Load");
					main.setLocation(2);
					}
					else{
						tryLoad = true;
						repaint();
					}
				}
				
			});
			
			//buttons.setBounds((main.winWidth/2)-buttons.getWidth(),(main.winHeight/2)-buttons.getHeight(),main.winWidth/4,100);
			buttons.setBounds((frame.getWidth()-buttons.getWidth())/2,(frame.getHeight()-buttons.getHeight())/2, 500, 200);
			System.out.println(buttons.getBounds().x);
			buttons.add(newgame);
			buttons.add(loadgame);		
			
			this.add(buttons);
		}
		
		public void paintComponent(Graphics g){
			BufferedImage in;
			try {
				in = ImageIO.read(new File("res/menus/titlemenu1.png"));
				new BufferedImage(main.winWidth,main.winHeight, BufferedImage.TYPE_INT_ARGB);
					g.drawImage(in, 0, 0, this.getWidth(), this.getHeight(), null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.setFont(new Font("Arial",Font.PLAIN, this.getWidth()/10));
			g.setColor(Color.yellow);

			//g.drawString("The Judgement", this.getWidth()/6, this.getHeight()/3);
			
			font = new Font("Arial", Font.BOLD, main.winWidth/30);
			
			buttons.setBounds((frame.getWidth()-buttons.getWidth())/2,(frame.getHeight()-buttons.getHeight())/2, 500, 200);
			//buttons.setBounds((main.winWidth/2), (main.winHeight/2)-buttons.getHeight(),main.winWidth/4,100);
			newgame.setFont(main.titleFont);
			loadgame.setFont(main.titleFont);
			
			if(tryLoad){
				String load = "No save files exist!";
				g.setColor(Color.yellow);
				g.setFont(main.titleFont);
				g.drawString("No save files exist!", (this.getWidth()-(g.getFontMetrics().stringWidth(load)))/2, (int) (this.getHeight()/3));
				tryLoad = false;

			}
			
		}
		
		public void removeStuff(){
			this.remove(buttons);
		}
}

