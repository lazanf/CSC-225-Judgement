/*
 * Class created by: Jake Cyr, Brian Eisenberg, Diego Holguin, and Frederick Rodriguez
 * 
 * Description: Creates the main JFrame, adds the game panels to the cards
 * panel which uses a CardLayout to change which panel is displayed, and gets the users screen resolution and 
 * sets the frame size accordingly
 * 
 * Date: 12/3/15
 */

package axohEngine2;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import axohEngine2.data.Data;
import axohEngine2.data.Save;
import axohEngine2.menus.ControlMenu;
import axohEngine2.menus.LoadGame;
import axohEngine2.menus.NewGame;
import axohEngine2.menus.TitleMenu;
import axohEngine2.project.STATE;
import axohEngine2.util.OSValidator;

public class Main implements KeyListener{
	
	public JFrame frame = new JFrame("Judgement");
	CardLayout layout;
	JPanel cards;
	
	Judgement judge;
	NewGame newGame;
	LoadGame loadGame;
	TitleMenu title;
	ControlMenu controls;
	
	Dimension screenSize;
	public int winWidth;
	public int winHeight;
	public boolean fullScreen;
	public boolean undec = false;
	public Font titleFont;
	int location = 0;

	Main main = this;
	Data data;
	Save save;

	//Constructor of the Main class
	public Main(){
		 
		//Create CardLayout and cards JPanel to add all
		//of the game panels to
		layout = new CardLayout();
		cards = new JPanel(layout);
		
		//Set dimensions of JFrame based on screen being played on
        setDimensions(); 
        
        //Create a new Data serializable class to save/load game data
        data = new Data();
        save = new Save(data);
        
        //Initialize all of the panels
        judge = new Judgement(140, winWidth, winHeight, save, data);	//Create a new Judgement JPanel object
        title = new TitleMenu(judge, this, frame);
        loadGame = new LoadGame(judge, this,frame);
        newGame = new NewGame(judge,this,frame);
        controls = new ControlMenu(judge,this,frame);
        
        //Initialize the frame
        initFrame();

        //Add all of the game screens to the cards panel which
        //has a CardLayout, in order to switch between them
        cards.add(title, "Title");
        cards.add(newGame, "New");
        cards.add(loadGame, "Load");
        cards.add(judge, "Judge");
        cards.add(controls, "Controls");
        
        layout.show(cards, "Title");
	}
	
	public void loadFonts() {
        try {
            //create the font to use. Specify the size!

            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("res\\fonts\\title.ttf")).deriveFont((float)main.winWidth/30f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            titleFont = customFont;
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res\\fonts\\title.ttf")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(FontFormatException e)
        {
            e.printStackTrace();
        }
	}
	
	//Initializes the main frame and adds the cards panel to it	
		public void initFrame(){
			
			
	        frame.add(cards);	//add the Judgement panel to the JFrame
			frame.setExtendedState(winHeight);	
	        frame.setUndecorated(false);			//Remove screen exit/minimize decorations
			//frame.setExtendedState(winWidth);	
	        frame.setUndecorated(undec);			//Remove screen exit/minimize decorations
	        frame.setResizable(true);
	        frame.setPreferredSize(screenSize); //Prevent the game frame from being resized.
	        frame.setSize(winWidth, winHeight);			//set the size of the frame relative to the size of the player's screen
	        frame.setFocusable(true);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Terminate the program on exit
	        frame.setVisible(true);									//make the frame visible 
	        frame.pack();
	        frame.addKeyListener(this);
	        frame.setMinimumSize(new Dimension(1000, 600));
	        loadFonts();
	        
	        frame.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener(){
				@Override
				public void ancestorMoved(HierarchyEvent arg0) {}
				
				//Is called if the JFrame is resized in order to updated the graphics
				@Override
				public void ancestorResized(HierarchyEvent e) {
					Judgement.SCREENWIDTH = frame.getWidth();
					Judgement.SCREENHEIGHT = frame.getHeight();
					
					winWidth = e.getComponent().getWidth();
					winHeight = e.getComponent().getHeight();
					
					judge.repaint();
					title.repaint();
					newGame.repaint();
					loadGame.repaint();
					controls.repaint();
				}           
	        });
		}
		

		
		public void setDimensions()
		{
			// Check if the system is running on a windows computer
			if(OSValidator.isWindows())
			{
				screenSize = Toolkit.getDefaultToolkit().getScreenSize();	//Get the screen dimensions
		        winWidth = (int) screenSize.getWidth();
		        winHeight = (int) screenSize.getHeight();
			}
			// Check if the system is running on a Mac computer
			else if (OSValidator.isMac())
			{
				GraphicsEnvironment.getLocalGraphicsEnvironment();
				// Get the rectangle bounds of the maximum Window size on the 
				// device taking into account for operating system objects such 
				// as task bar and window bars
				Rectangle windowRect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
				
				// Assign the appropriate variable values using the windowRect
				screenSize = windowRect.getSize();
				winWidth = (int) screenSize.getWidth();
				winHeight = (int) screenSize.getHeight();
				System.out.println("dzfdsfds");

			}
			
			System.out.println("Window Width : " + winWidth);
			System.out.println("Window Height : " + winHeight);

		}
		
		//The main method that executes the program
		public static void main(String [] args){
			new Main();
		}
		
		//Load from the data.ser file
		//Receives the data class object that was serialized
		public void loadState(File file){
			if(file.exists()){
			try {
				FileInputStream fis;
				fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				data = (Data)ois.readObject();

				judge.setMapX(data.getMapX());
				judge.setMapY(data.getMapY());
				judge.setPlayerName(data.getName());
				judge.setPlayerX(data.getPlayerX());
				judge.setPlayerY(data.getPlayerY());
				judge.setMagic(data.getPlayerMagic());
				judge.setHealth(data.getPlayerHealth());
				
				layout.show(cards, "Judge");
				judge.setState(STATE.GAME);
				
				System.out.println("Game Loaded Successfully");
				
				ois.close();			
			} catch (FileNotFoundException e) {
				System.out.println("No file found");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			}
			else{
				System.out.println("No save file found. Opening NewGame screen.");
			}
		}

		
		//Called when key is pressed
		//If on main game screen, will send the key pressed to the Judgement class
		@Override
		public void keyPressed(KeyEvent e) {
			
			//Location 3 = ControlsMenu
			if(location  == 3){
				layout.show(cards, "Judge");
				judge.setState(STATE.GAME);
				layout.show(cards, "Judge");
				location = 4;
			}
			
			judge.gameKeyDown(e.getKeyCode());
			
			//Toggle Full-screen
			if(e.getKeyCode() == KeyEvent.VK_F11){
				frame.dispose();					 //Gets rid of old JFrame
				frame = new JFrame("The Judgement"); //Initialize new JFrame
				undec = !undec;						//Switches the undecorated variable
				initFrame();						//Creates a new JFrame with the new undecorated variable
			}
			
			//Exit the game on ESCAPE press
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				if(location == 4){
					save.saveState(new File("data/" + data.getName() + ".ser"));
				}
				System.exit(1);
			}
			
			
			if(e.getKeyCode() == KeyEvent.VK_P){
				if(judge.getState() == STATE.INGAMEMENU){
					judge.setState(STATE.GAME);
				}
				else if(judge.getState() == STATE.GAME){
				judge.setState(STATE.INGAMEMENU);
				}
			}
			
			/**Sets the current card being displayed by number
			 * 0 = TitleMenu 1= NewGame Menu  2= LoadGame Menu  3= ControlsMenu**/
			
			if(location == 1 && (Character.isAlphabetic(e.getKeyChar()) | Character.isDigit(e.getKeyChar()))){
				newGame.addKey(e.getKeyChar());
				newGame.repaint();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
				if(location == 2){
					layout.show(cards, "Title");
				}
				else if(location == 1 && newGame.getName().length() ==0){
					layout.show(cards, "Title");
				}
				else if(location == 1 && newGame.getName().length()> 0){
					newGame.removeKey();
					newGame.repaint();
				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				if(newGame.getName().length() > 1){
					judge.setPlayerName(newGame.getName());
					judge.setFile(new File("data/" + newGame.getName() + ".ser"));
					
					data.setName(newGame.getName());
					newGame.clearName();
					layout.show(cards, "Controls");
					location = 3;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent k) {
			   judge.gameKeyUp(k.getKeyCode());			
		}

		@Override
		public void keyTyped(KeyEvent k) {
			judge.setKeyChar(k.getKeyChar());
		}
		
		//Change which panel the CardLayout is displaying
		public void setLayout(String s){
			layout.show(cards, s);
		}
		
		public Dimension getSize(){
			return new Dimension(frame.getWidth(),frame.getHeight());
		}
		
		//Changes the size of the frame
		public void changeSize(){
			frame.setSize(winWidth, winHeight);
		}
		
		//Sets the location of the frame
		public void setLocation(int x, int y){
			frame.setLocation(x,y);
		}
		
		public void setLocation(int i){
			location = i;
		}
}
