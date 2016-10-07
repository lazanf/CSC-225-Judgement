/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 0.53
 * Date: June 14, 2015
 * Title: Judgement(The Game)
 * Description: This class extends 'Game.java' in order to run a 2D game with specifically defined
 * sprites, animations, and actions.
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/
//Package name
package axohEngine2;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

import javax.swing.Timer;

import axohEngine2.data.Data;
import axohEngine2.data.Save;
import axohEngine2.entities.AnimatedSprite;
import axohEngine2.entities.Attack;
import axohEngine2.entities.ImageEntity;
import axohEngine2.entities.Mob;
import axohEngine2.entities.SpriteSheet;
import axohEngine2.map.Map;
import axohEngine2.map.Tile;
import axohEngine2.menus.InGameMenu;
import axohEngine2.project.MapDatabase;
import axohEngine2.project.OPTION;
import axohEngine2.project.STATE;
import axohEngine2.project.TYPE;
import axohEngine2.util.OSValidator;
import axohEngine2.sound.*;


//Start class by also extending the 'Game.java' engine interface
public class Judgement extends Game implements ActionListener {
    //For serializing (The saving system)
    private static final long serialVersionUID = 1L;

    /******************
     * Variables
     **********************/
    /*--------- Screen ---------
     * SCREENWIDTH - Game window width
	 * SCREENHEIGHT - Game window height
	 * CENTERX/CENTERY - Center of the game window's x/y
	 */
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int width = (int) screenSize.getWidth();
    private static final int height = (int) screenSize.getHeight();
    
    public static String lastCollided;

    public static int SCREENWIDTH = width;
    public static int SCREENHEIGHT = height;
    private static int CENTERX = getScreenwidth() / 2;
    private static int CENTERY = getScreenheight() / 2;
    private static boolean left,right,up,down;
    /*--------- Miscellaneous ---------
     *booleans - A way of detecting a pushed key in game
     *random - Use this to generate a random number
     *state - Game states used to show specific info ie. pause/running
     *option - In game common choices at given times
     *Fonts - Various font sizes in the Arial style for different in game text
     *
     */
    private final Font simple = new Font("Arial", Font.PLAIN, 72);
	private Font menuSmall = new Font("Arial", Font.BOLD, 32);
    /*----------- Menus ----------------
     * inX/inY - In Game Menu starting location for default choice highlight
     * inLocation - Current choice in the in game menu represented by a number, 0 is the top
     * sectionLoc - Current position the player could choose after the first choice has been made in the in game menu(Items -> potion), 0 is the top.
     * titleX, titleY, titleX2, titleY2 - Positions for specific moveable sprites at the title screen (arrow/highlight).
     * titleLocation - Current position the player is choosing in the title screen(File 1, 2, 3) 0 is top
     * currentFile - Name of the currently loaded file
     * wasSaving/wait/waitOn - Various waiting variables to give the player time to react to whats happening on screen
     */
    private final int inX = getScreenwidth() / 10 - 50;
    Random random = new Random();
    Mob randomNPC;
    private String playerName = null;
	private boolean keyLeft, keyRun, keyRight,keyUp, keyDown, keyAction, keyBack, keyEnter, 
    				keySpace, keyEscape, keyAttack, keyHealthUp, keyHealthDown, keyMagicUp, keyMagicDown, keyFPS, keyUI;
    private STATE state;
    private OPTION option;
    /*--------- Player and scale ---------
     * scale - All in game art is 16 x 16 pixels, the scale is the multiplier to enlarge the art and give it the pixelated look
     * mapX/mapY - Location of the camera on the map
     * playerX/playerY - Location of the player on the map
     * startPosX/startPosY - Starting position of the player in the map
     * playerSpeed - How many pixels the player moves in a direction each update when told to
     * */
    private int scale;
    private int mapX;
    private int mapY;
    private int playerX;
    private int playerY;
    private int playerSpeed;
    private boolean showingFPS;
    private double shiftX = 0;
    private double shiftY = 0;

    private boolean uiToggle;
    
    private MidiSequence songManager;

    private boolean fullScreen;
	public boolean canMoveLeft;
	public boolean canMoveRight;
	public boolean canMoveUp;
	public boolean canMoveDown;
	private float damageTimer;
	private int lastDamage;
	private float trueX;
	private float trueY;
	private int npcMaxHealth;
	private int npcHealth;
	public boolean swordOut;
	float Xoffset;
	float Yoffset;
	Float newX;
	Float newY;
	int perfectX;
	int perfectY;
	private int npcX; 
	private int npcY;
	private boolean drawingDamage = false;
	static int temp = 0;
    /*----------- Map and input --------
    * currentMap - The currently displayed map the player can explore
    * currentOverlay - The current overlay which usually contains houses, trees, pots, etc.
    * mapBase - The database which contains all variables which pertain to specific maps(NPCs, monsters, chests...)
    * inputWait - How long the system waits for after an action is done on the keyboard
    * confirmUse - After some decisions are made, a second question pops up, true equals continue action from before.
    */
    private Map currentMap;
    private Map currentOverlay;
    private MapDatabase mapBase;
    private int inputWait = 5;
    private boolean confirmUse = false;
    private int inY = 120;
    private int inLocation;
    private int sectionLoc;
    //Menu classes
    private InGameMenu inMenu;
    //Player and NPCs
    private Mob playerMob;
    private Data data;
    private File file;
    Timer time = new Timer(30000, this);

    /***********************************************************************
     * Constructor
     * <p>
     * Set up the super class Game and set the window to appear
     **********************************************************************/
    Judgement(int frameRate, int width, int height, Save s,Data d) {
    	super(140,width, height);
    	SCREENWIDTH = width;
    	SCREENHEIGHT = height;
        setVisible(true);
        
        data = d;
        save = s;
    
        setFocusable(true);
    }

    public static int getScreenwidth() {
        return SCREENWIDTH;
    }

    public static int getScreenheight() {
        return SCREENHEIGHT;
    }
    
    public void startTimer(){
    	time.start();
    }
    
    public void setFile(File f){
    	file = f;
    }

    /****************************************************************************
     * Inherited Method
     * This method is called only once by the 'Game.java' class, for startup
     * Initialize all non-int variables here
     *****************************************************************************/
    void gameStartUp() {
    	
        initVariables();
        mapBase = new MapDatabase(this, graphics(), scale);
        
		for(int i = 0; i < mapBase.maps.length; i++)
		{
			// Identifies the rendering process
			if(i < 4)
			{
				System.out.println("Rendering map " + mapBase.getMap(i)._name);
			}
			
			// If the map equals nothing 
			if(mapBase.getMap(i) == null)
			{
				// Just continue on ahead
				continue;
			}
			// If the map database has the map name "city".. 
			if(mapBase.getMap(i).mapName() == "city")
			{
				// Set the map pointer to hold the correct map object
				currentMap = mapBase.getMap(i);
			}
			// If the map database has the map name "city0"...
			if(mapBase.getMap(i).mapName() == "cityO")
			{
				// Set the map pointer to hold the correct map object
				currentOverlay = mapBase.getMap(i);
			}
		}
		//Add the tiles from the map to be updated each system cycle
		for(int i = 0; i < currentMap.getHeight() * currentMap.getHeight(); i++)
		{
			temp = i;
			// Add a tile to the map based upon the index of i
			addTile(currentMap.accessTile(i));
			
			// Add a tile to the currentOverlay map based upon the index of i 
			addTile(currentOverlay.accessTile(i));
			
			// Check if the current map has a mob object at that tile location
			if(currentMap.accessTile(i).hasMob()) 
			{
				// If so, 
				sprites().add(currentMap.accessTile(i).mob());
			}
			// Check if the currentMap overlay has a mob object at that tile location
			if(currentOverlay.accessTile(i).hasMob()) 
			{
				sprites().add(currentOverlay.accessTile(i).mob());
			}
			
			currentMap.accessTile(i).getEntity().setX(-300);
			
			currentOverlay.accessTile(i).getEntity().setX(-300);
		}
        requestFocus(); 
        start(); 
    }

    private void initVariables() {
    	
    	left = true;
    	right = true;
    	up = true;
    	down = true;
    	
        state = STATE.TITLE;
        option = OPTION.NONE;

        
        scale = SCREENWIDTH/480;
        playerSpeed = 5;
        
        //****Initialize spriteSheets*********************************************************************
        SpriteSheet extras1 = new SpriteSheet("/textures/extras/extras1.png", 8, 8, 32, scale);
        SpriteSheet mainCharacter = new SpriteSheet("/textures/characters/mainCharacter.png", 8, 8, 32, scale);

        //****Initialize and setup AnimatedSprites*********************************************************
        AnimatedSprite titleArrow = new AnimatedSprite(this, graphics(), extras1, 0, "arrow");
        titleArrow.loadAnim(4, 10);
        sprites().add(titleArrow);

        //****Initialize and setup image entities**********************************************************
        ImageEntity inGameMenu = new ImageEntity(this);
        ImageEntity titleMenu = new ImageEntity(this);
        ImageEntity titleMenu2 = new ImageEntity(this);
        inGameMenu.load("/menus/ingamemenu.png");
        titleMenu.load("/menus/titlemenu1.png");
        titleMenu2.load("/menus/titlemenu2.png");

        //*****Initialize Menus***************************************************************************
        inMenu = new InGameMenu(inGameMenu);

        //****Initialize and setup Mobs*********************************************************************
        playerMob = new Mob(this, graphics(), mainCharacter, 40, TYPE.PLAYER, "mainC", true);
        //playerMob.setBounds(15, 30, 60);
        
        OSValidator currOS = new OSValidator();
        
        if(currOS.isWindows())
        {
		playerMob.setLeftBound(30, 74, 14, 32);
		playerMob.setRightBound(78, 74, 14, 32);
		playerMob.setTopBound(44, 60, 32, 14);
		playerMob.setBottomBound(44, 110, 32, 14);
        }
        else if (currOS.isMac())
        {
    		playerMob.setLeftBound(25, 52, 7, 26);
    		playerMob.setRightBound(63, 52, 7, 26);
    		playerMob.setTopBound(34, 46, 26, 10);
    		playerMob.setBottomBound(34, 80, 26, 10);
        }
		//playerMob.setSwordBound(25, 40, 72, 48);
		playerMob.setMoveAnim(32, 48, 40, 56, 3, 10);
        playerMob.addAttack("sword", 0, 5);
        Attack attack = playerMob.getAttack("sword");
        attack.addMovingAnim();
        attack.addAttackAnim();
        attack.addInOutAnim();
        playerMob.setCurrentAttack("sword"); //Starting attack
        
        //TODO: Don't forget this
        playerMob.setHealth(35); //If you change the starting max health, dont forget to change it in inGameMenu.java max health also
        sprites().add(playerMob);
        uiToggle = true;
        
        songManager = new MidiSequence("EspanjaTango");
        songManager.setLooping(true);
        songManager.play();
        
    }

    /****************************************************************************
     * Inherited Method
     * Method that updates with the default 'Game.java' loop method
     * Add game specific elements that need updating here
     *****************************************************************************/
    void gameTimedUpdate() {
        // Checks for the user's input
        checkInput();

        //Update certain specifics based on certain game states

        if (state == STATE.INGAMEMENU) {
            inMenu.update(option, sectionLoc, playerMob.health()); //In Game Menu update
        }
    }

    /**
     * Inherited Method
     * Obtain the 'graphics' passed down by the super class 'Game.java' and render objects on the screen
     */
    
    void gameRefreshScreen() {

        g2d.clearRect(0, 0, getScreenwidth(), getScreenheight());
        g2d.setFont(simple);
        int maxHealth = inMenu.getHealth();
        refreshGameScreen(maxHealth);
        
        if (state == STATE.INGAMEMENU) {
            inMenu.render(this, g2d, inX, inY);
            
            g2d.setColor(Color.red);
            if (confirmUse) g2d.drawString("Use this?", CENTERX, CENTERY);
        }
        if (option == OPTION.SAVE) {
            save.saveState(file);
            g2d.setFont(new Font("TimesRoman", Font.BOLD, 110));
            g2d.drawString("Game saved", SCREENWIDTH/2, SCREENHEIGHT/2);
        }
    }
    
    public double screenWidthRatio () {
    	double ratio;
    	double maxWidth;
    	double currentWidth;
    	maxWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    	currentWidth = SCREENWIDTH;
    	ratio = maxWidth/currentWidth;
		return ratio;
    }
    
    public double screenHeightRatio () {
    	double ratio;
    	double maxHeight;
    	double currentHeight;
    	maxHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    	currentHeight = SCREENHEIGHT;
    	ratio = maxHeight/currentHeight;
		return ratio;
    }
    
    public boolean swordCollision () {
		for(int i=0; i < currentOverlay.currentMobs.length; i++) {
			if(currentOverlay.currentMobs[i]!=null) {
				Mob npc = currentOverlay.currentMobs[i];
				if(playerMob.getLeftBound().intersects(npc.getBounds())
				|| playerMob.getRightBound().intersects(npc.getBounds())
				|| playerMob.getTopBound().intersects(npc.getBounds())
				|| playerMob.getBottomBound().intersects(npc.getBounds()))
				return true;
			}

		}
		return false;
    	
    }
   

    private void refreshGameScreen(int maxHealth) {
        if (state == STATE.GAME) {

            //Render the map, the player, any NPCs or Monsters and the player health or status
            CENTERX = SCREENWIDTH/2;
            CENTERY = SCREENHEIGHT/2;
            System.out.println(swordCollision());
            
            currentMap.render(this, g2d, getMapX(), getMapY());
            currentOverlay.render(this, g2d, getMapX(), getMapY());
			trueX = (float)((mapX+(float)playerX)/-64)+16;
			trueY = (float)((mapY+(float)playerY)/-64)+9.875f;
			Xoffset = trueX - (int)trueX;
			Yoffset = trueY - (int)trueY;
			newX = trueX - Xoffset;
			newY = trueY - Yoffset;
			perfectX = newX.intValue();
			perfectY = newY.intValue();
			
            playerMob.renderMob(CENTERX - playerX, CENTERY - playerY);
            inMenu.fixHealth();
            inMenu.fixMagic();
            Color DARKBLUE = new Color(10, 10, 60);
            Color LIGHTBLUE = new Color(19, 60, 217);
	        Color MIDBLUE = new Color (19, 30, 110);
	        Color darkGreen = new Color (0, 150, 0);
	        Color lightGreen = new Color (50, 255, 50);
			GradientPaint healthGradient = new GradientPaint(5, 25, darkGreen, 5, 5, lightGreen, true);
			GradientPaint manaGradient = new GradientPaint(5, 25, MIDBLUE, 5, 5, LIGHTBLUE, true);
            
	        inMenu.fixHealth();
	        inMenu.fixMagic();
	        
			if(showingFPS) {

	            g2d.setFont(new Font("Arial", Font.BOLD, SCREENWIDTH/30));
				g2d.setColor(Color.BLACK);
				g2d.drawString(""+frameRate(), (int) (CENTERX*1.8), (int) (CENTERY/4.9));
				g2d.setColor(Color.YELLOW);
				g2d.drawString(""+frameRate(), (int) (CENTERX*1.8), (int) (CENTERY/4.9));
			} else if (!showingFPS) {
				//g2d.clearRect(850, -482, 100, 100);
			}

	        if(uiToggle == true) {
	        //System.out.println("Ratio:"+screenWidthRatio());
	       // System.out.println("Screen Width:"+SCREENWIDTH);
	        //System.out.println("Max Width:"+(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth());
			//GradientPaint gp2 = new GradientPaint(5, 50, Color.DARK_GRAY, 5, 5, Color.WHITE, true);
			Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1-damageTimer*.015f);
			Font damage = new Font("Arial", Font.BOLD, 32);
			Composite d = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
	        g2d.setFont(menuSmall);
	        
	        // making this into a function would make it much easier to change and handle.
	        //TODO: take a better look at this.
            g2d.setFont(new Font("Arial", Font.BOLD, SCREENWIDTH/60));
            g2d.setColor(Color.black);
            g2d.fillRect((int) (CENTERX/8.42), (int) (CENTERY/12.27), (int) (SCREENWIDTH/10.97), SCREENHEIGHT/36);
            g2d.setColor(Color.RED);
            g2d.fillRect((int) (CENTERX/8.42), (int) (CENTERY/13.5), (int) (SCREENWIDTH/10.97), SCREENHEIGHT/36);
            g2d.setPaint(healthGradient);
            g2d.fillRect((int) (CENTERX/8.42), (int) (CENTERY/13.5), SCREENWIDTH*maxHealth/384, SCREENHEIGHT/36);
            g2d.setColor(Color.BLACK);
            g2d.drawString("HP: " + maxHealth, (int) (CENTERX/18.5), (int) (CENTERY/7.9));
            g2d.drawString("/" + maxHealth, (int) (CENTERX/6.4),(int) (CENTERY/7.9));
            g2d.setColor(Color.WHITE);
            g2d.drawString("HP: " + maxHealth, (int) (CENTERX/18.1), (int) (CENTERY/7.9));
            g2d.drawString("/" + maxHealth, (int)(CENTERX/6.3), (int)(CENTERY/7.9));
            g2d.setColor(Color.BLACK);
            g2d.fillRect((int) (CENTERX/5.4), (int) (CENTERY/6.1), (int) (SCREENWIDTH/10.7), SCREENHEIGHT/36);
            g2d.setColor(DARKBLUE);
            g2d.fillRect((int) (CENTERX/5.49), (int) (CENTERY/6.35), (int) (SCREENWIDTH/10.7), SCREENHEIGHT/36);
            g2d.setPaint(manaGradient);
            g2d.fillRect((int) (CENTERX/5.49), (int) (CENTERY/6.35), SCREENWIDTH*inMenu.getMagic()/384, SCREENHEIGHT/36);
            g2d.setColor(Color.BLACK);
            g2d.drawString("MP: " + inMenu.getMagic(), (int) (CENTERX/8.35), (int) (CENTERY/4.78));
            g2d.drawString("/" + inMenu.getMaxMagic(), (int) (CENTERX/4.42), (int) (CENTERY/4.78));
            g2d.setColor(Color.WHITE);
            g2d.drawString("MP: " + inMenu.getMagic(), (int) (CENTERX/8.50), (int) (CENTERY/4.86));
            g2d.drawString("/" + inMenu.getMaxMagic(), (int) (CENTERX/4.47), (int) (CENTERY/4.86));
            g2d.setFont(new Font("Arial", Font.BOLD, SCREENWIDTH/30));
            //g2d.drawString(""+lastCollided, CENTERX, CENTERY+300);
			
			
			/*g2d.setColor(Color.BLACK);
			g2d.drawString("Player: " + playerName, CENTERX - (int)(898/screenWidthRatio()), CENTERY +(int)(484/screenHeightRatio()));
			g2d.setColor(Color.WHITE);
			g2d.drawString("Player: " + playerName, CENTERX - (int)(900/screenWidthRatio()), CENTERY +(int)(482/screenHeightRatio()));
			g2d.drawString("X:" + perfectX, CENTERX + (int)(600/screenWidthRatio()), CENTERY +(int)(482/screenHeightRatio()));
			g2d.drawString("Y:" + perfectY, CENTERX + (int)(750/screenWidthRatio()), CENTERY +(int)(482/screenHeightRatio()));
			g2d.setColor(Color.CYAN);*/
			//g2d.drawRect(playerMob.getBounds().x, playerMob.getBounds().y, playerMob.getBounds().width, playerMob.getBounds().height);
			//g2d.setColor(Color.PINK);
			/*g2d.drawRect(playerMob.getLeftBound().x, playerMob.getLeftBound().y, playerMob.getLeftBound().width, playerMob.getLeftBound().height);
			g2d.setColor(Color.MAGENTA);
			g2d.drawRect(playerMob.getRightBound().x, playerMob.getRightBound().y, playerMob.getRightBound().width, playerMob.getRightBound().height);
			g2d.setColor(Color.RED);
			g2d.drawRect(playerMob.getTopBound().x, playerMob.getTopBound().y, playerMob.getTopBound().width, playerMob.getTopBound().height);
			g2d.setColor(Color.YELLOW);
			g2d.drawRect(playerMob.getBottomBound().x, playerMob.getBottomBound().y, playerMob.getBottomBound().width, playerMob.getBottomBound().height);*/
			//g2d.setColor(Color.WHITE);
			//g2d.drawRect(playerMob.getSwordBound().x, playerMob.getSwordBound().y, playerMob.getSwordBound().width, playerMob.getSwordBound().height);
			g2d.setColor(Color.WHITE);
			//g2d.drawString("," +currentOverlay.currentMobs[0].getYLoc(), CENTERX + 870, CENTERY +450);
			
			if(drawingDamage == true) {
				damageTimer += 1;
			}
						
			//NPC HEALTH BARS
			for(int i=0; i < currentOverlay.currentMobs.length; i++) {
				if(currentOverlay.currentMobs[i]!=null) {
					Mob npc = currentOverlay.currentMobs[i];
					//currentOverlay.currentMobs[0].move(1, 0);
					npcX = (int)npc.getXLoc();
					npcY = (int)npc.getYLoc();
					npcMaxHealth = npc.maxHealth();
					npcHealth = npc.health();

					npcX = (int)currentOverlay.currentMobs[i].getXLoc();
					npcY = (int)currentOverlay.currentMobs[i].getYLoc();
					setNpcMaxHealth(currentOverlay.currentMobs[i].maxHealth());
					npcHealth = currentOverlay.currentMobs[i].health();
				
					Font health = new Font("Arial", Font.BOLD, 22);
					g2d.setFont(health);
					g2d.setColor(Color.BLACK);
					g2d.drawString(""+npcHealth, npcX+16, npcY-20);
					g2d.setColor(Color.WHITE);
					g2d.drawString(""+npcHealth, npcX+18, npcY-22);
					g2d.setColor(Color.BLACK);
					g2d.fillRect(npcX+28, npcY-15, (int)(80*0.9), 12);
					g2d.setColor(Color.RED);
					g2d.fillRect(npcX+26, npcY-17, (int)(80*0.9), 12);
					g2d.setColor(Color.GREEN);
					g2d.fillRect(npcX+26, npcY-17, (int)(npc.fixHealthBar(npcHealth)*0.9), 12);
					//g2d.drawRect(npc.getBounds().x, npc.getBounds().y, npc.getBounds().width, npc.getBounds().height);
					if(drawingDamage == true) {
						g2d.setComposite(c);
						g2d.setFont(damage);
						if(keyAttack == true && damageTimer < 2) {
							currentOverlay.currentMobs[i].takeDamage(lastDamage);
						}
						if(lastDamage < 10) {
						g2d.setColor(Color.BLACK);
						g2d.drawString(""+lastDamage, (int)npc.getXLoc()+54, (int)npc.getYLoc()+52-damageTimer);
						g2d.setColor(Color.WHITE);
						g2d.drawString(""+lastDamage, (int)npc.getXLoc()+52, (int)npc.getYLoc()+50-damageTimer);
						} else if(lastDamage >= 10 && lastDamage < 100) {
							g2d.setColor(Color.BLACK);
							g2d.drawString(""+lastDamage, (int)npc.getXLoc()+44, (int)npc.getYLoc()+52-damageTimer);
							g2d.setColor(Color.WHITE);
							g2d.drawString(""+lastDamage, (int)npc.getXLoc()+42, (int)npc.getYLoc()+50-damageTimer);
						}
						g2d.setComposite(d);
					}
					
					if(npcHealth <= 0) {
						currentOverlay.currentMobs[i] = null;
						currentOverlay.arrayInt-=1;
					}
				}
			}
			if(playerMob.animating()) {
				keyAttack = false;
			}

			if(damageTimer >= 40) {
				damageTimer = 0;
				drawingDamage = false;

				//g2d.clearRect(50, 50, (int)currentOverlay.accessTile(300).mob().getXLoc(), (int)currentOverlay.accessTile(300).mob().getYLoc());
			}
		}
        }
    }

    /*******************************************************************
     * The next four methods are inherited
     * Currently these methods are not being used, but they have
     * been set up to go off at specific times in a game as events.
     * Actions that need to be done during these times can be added here.
     ******************************************************************/
    void gameShutDown() {
    }
    void spriteUpdate() {
    }

    void spriteDraw() {
    }

    void spriteDying() {
    }

    /*************************************************************************
     *****************************************************************************/
    void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2, int hitDir, int hitDir2) {
        // Only check for sprite collision if the user is not in the title screen
        if (state != STATE.TITLE) {
            /*System.out.println("Sprite Collision Being Called");

            //Get the smallest possible overlap between the two problem sprites
            int sp1BoundSize = spr1.getBoundSize();
            double spr1BoundX = spr1.getBoundX(hitDir);
            double spr1BoundY = spr1.getBoundY(hitDir);

            int sp2BoundSize = spr2.getBoundSize();
            double spr2BoundX = spr2.getBoundX(hitDir2);
            double spr2BoundY = spr2.getBoundY(hitDir2);

            double leftOverlap = (spr1BoundX + sp1BoundSize - spr2BoundX);
            double rightOverlap = (spr2BoundX + sp2BoundSize - spr1BoundX);
            double topOverlap = (spr1BoundY + sp1BoundSize - spr2BoundY);
            double botOverlap = (spr2BoundY + sp2BoundSize - spr1BoundY);
            double smallestOverlap = Double.MAX_VALUE;
            shiftX = 0;
            shiftY = 0;

            //System.out.println("Smallest Overlap : " + smallestOverlap);
            //System.out.println("Right Overlap : " + rightOverlap);
            //System.out.println("Top Overlap : " + topOverlap);
            //System.out.println("Bottom Overlap : " + botOverlap);

            checkSpriteSpriteOverlap(leftOverlap, rightOverlap, topOverlap, botOverlap, smallestOverlap);

            //Handling very specific collisions
            if (spr1.spriteType() == TYPE.PLAYER && state == STATE.GAME) {
                if (spr2 instanceof Mob) ((Mob) spr2).stop();*/

                //This piece of code is commented out because I still need the capability of getting a tile from an xand y position
                /*if(((Mob) spr1).attacking() && currentOverlay.getFrontTile((Mob) spr1, playerX, playerY, CENTERX, CENTERY).getBounds().intersects(spr2.getBounds())){
                    ((Mob) spr2).takeDamage(25);
					//
				}*/

                //Handle simple push back collision
                /*if (playerX != 0) playerX -= shiftX;
                if (playerY != 0) playerY -= shiftY;
                if (playerX == 0) setMapX((int) (getMapX() - shiftX));
                if (playerY == 0) setMapY((int) (getMapY() - shiftY));
            }*/
        }
    }
    //there's no way this is the best way to do this.
    //TODO:
    
    private void checkSpriteSpriteOverlap(double leftOverlap, double rightOverlap, double topOverlap, double botOverlap,
                                          double smallestOverlap) {
        if (leftOverlap < smallestOverlap) { //Left
            smallestOverlap = leftOverlap;
            shiftX -= leftOverlap;
            //System.out.println("Left Overlap");
            shiftY = 0;
        }
        if (rightOverlap < smallestOverlap) { //right
            smallestOverlap = rightOverlap;
            //System.out.println("Right Overlap");
            shiftX = rightOverlap;
            shiftY = 0;
        }
        if (topOverlap < smallestOverlap) { //up
            smallestOverlap = topOverlap;
            //System.out.println("Top Overlap");
            shiftX = 0;
            shiftY -= topOverlap;
        }
        if (botOverlap < smallestOverlap) { //down
            //System.out.println("Bottom Overlap");
            shiftX = 0;
            shiftY = botOverlap;
        }
    }

    /***********************************************************************
     *************************************************************************/
	void tileCollision(AnimatedSprite spr, Tile tile) 
	{	
		// Only check for sprite collision if the user is not in the title screen
		if(state != STATE.TITLE && spr.collided() == true)
		{	


				if(spr.getLeftBound().intersects(tile.getTileBounds())) {
					canMoveLeft = false;
					System.out.println("left collision");
					lastCollided = "left collision";
				}
				if(spr.getRightBound().intersects(tile.getTileBounds())) {
					canMoveRight = false;
					System.out.println("right collision");
					lastCollided = "right collision";
				}
				if(spr.getBottomBound().intersects(tile.getTileBounds())) {
					canMoveDown = false;
					System.out.println("bottom collision");
					lastCollided = "bottom collision";
				}
				if(spr.getTopBound().intersects(tile.getTileBounds())) {
					canMoveUp = false;
					System.out.println("top collision");
					lastCollided = "top collision";
				}				
				if(spr.getTopBound().intersects(tile.getTileBounds())) {
						if(keyAction == true && (tile._name).equals("chest")) {
							tile.setFrame(tile.getSpriteNumber() + 1); //Chests should have opened and closed version next to each other
							//.addItem(tile.event().getItem()); //Add item to inventory
						}
				}
			}
	}

    /**********************************************************
     * The Depths of Judgement Lies Below
     * <p>
     * Key events - Mouse events
     ***********************************************************/

	void movePlayer(int xa, int ya) {
		if(!playerMob.collided()) {
			canMoveRight = true;
			canMoveLeft = true;
			canMoveUp = true;
			canMoveDown = true;
			lastCollided = "no collision";
		}
		
		if(canMoveLeft) {
			if(xa > 0) {
				if(mapX + xa < currentMap.getMinX() && playerX < playerSpeed && playerX > -playerSpeed) {
					canMoveRight = true;
					mapX += xa;
				} else {
					playerX += xa; //left +#
					canMoveRight = true;
				}
			}
		}
		if(canMoveRight) {
			if(xa < 0) {
				if(mapX + xa > currentMap.getMaxX(SCREENWIDTH) && playerX < playerSpeed && playerX > -playerSpeed){
					canMoveLeft = true;
					mapX += xa;
				} else {
					playerX += xa;
					canMoveLeft = true;
				}
			}
		}
		if(canMoveUp) {
			if(ya > 0) {
				if(mapY + ya < currentMap.getMinY() && playerY < playerSpeed && playerY > -playerSpeed) {
					mapY += ya;
					canMoveDown = true;
				} else {
					playerY += ya; //up +#
					canMoveDown = true;
				}
			}
		}
		if(canMoveDown) {
			if(ya < 0) {
				if(mapY + ya > currentMap.getMaxY(SCREENHEIGHT) && playerY < playerSpeed && playerY > -playerSpeed) {
					mapY += ya;
					canMoveUp = true;
				} else {
					playerY += ya; //down -#
					canMoveUp = true;
				}
			}
		}
	}

    /****************************************************************
     * Check specifically defined key presses which do various things
     ****************************************************************/
    private void checkInput() {
        if (state == STATE.GAME && inputWait < 0) checkGame(0, 0);
        if (state == STATE.INGAMEMENU && inputWait < 0) checkMenu();
        inputWait--;
    }

    private void checkGame(int xa, int ya) {
        /********************************************
         * Special actions for In Game
         *******************************************/
        //A or left arrow(move left)
        if (keyLeft && !keyRight && left) {
        	right = true;
            xa = playerSpeed;
            playerMob.updatePlayer(true, keyRight, keyUp, keyDown);
        }
        //D or right arrow(move right)
        if (keyRight && !keyLeft && right) {
        		left = true;
        		xa = -playerSpeed;
        		playerMob.updatePlayer(keyLeft, true, keyUp, keyDown);
        }
        //W or up arrow(move up)
        if (keyUp && !keyDown && up) {
        		down = true;
                ya = playerSpeed;
                playerMob.updatePlayer(keyLeft, keyRight, true, keyDown);	
        }
        //S or down arrow(move down)
        if (keyDown && !keyUp && down) {
        		up = true;
                ya = -playerSpeed;
                playerMob.updatePlayer(keyLeft, keyRight, keyUp, true);
        }
        if (keyEscape) {
        	updateData();
            save.saveState(file);
            System.out.println(file.getName());
            System.exit(0);
        }
        if (keyRun) {
            playerSpeed = 10;
            //System.out.println("Speed = " + playerSpeed);
            playerMob.setSpeed(playerSpeed);
        } else {
            playerSpeed = 6;
        }
		if(keyAttack && damageTimer <= 0 && swordOut){
			inputWait = 10;
			playerMob.attack();
			lastDamage = (int)(Math.random()*12+1);
    		drawingDamage = true;
    		damageTimer = 0;
		}
        //No keys are pressed
        if (!keyLeft && !keyRight && !keyUp && !keyDown) {
            playerMob.updatePlayer(keyLeft, keyRight, keyUp, keyDown);
        }
        movePlayer(xa, ya);

        //1 (+5 Health debug command)
        if (keyHealthUp) {
            inMenu.healthPlus();
            //inputWait = 1;
            keyHealthUp = false;
        }

        //2 (-5 Health debug command)
        if (keyHealthDown) {
            inMenu.healthMinus();
            //inputWait = 1;
            keyHealthDown = false;
        }

        //3 (-5 Magic debug command)
		if(keyMagicUp) {
			inMenu.magicPlus();
			inputWait = 1;
			keyMagicUp = false;
		}
        //4 (-5 Magic debug command)
        if (keyMagicDown) {
            inMenu.magicMinus();
            //inputWait = 1;
            keyMagicDown = false;
        }

        if (keyFPS) {
            if (!showingFPS) {
                showingFPS = true;
                keyFPS = false;
            } else {
                showingFPS = false;
                keyFPS = false;
            }
        }
        
        if (keyUI) {
            if (!uiToggle) {
                uiToggle = true;
                keyUI = false;
            } else {
                uiToggle = false;
                keyUI = false;
            }
        }
        //4 (-5 Magic debug command)

        //SpaceBar(action button)
		if(keySpace && !swordOut) {
			playerMob.inOutItem();
			inputWait = 10;
			swordOut = true;
		} else if(keySpace && swordOut) {
			playerMob.inOutItem();
			inputWait = 10;
			swordOut = false;
		}
	}//end in game choices

    
    private void checkMenu() {
        /******************************************
         * Special actions for In Game Menu
         ******************************************/
        //I(Close )

        int boxChangeY = 130;
        //No option is chosen yet
        if (option == OPTION.NONE) {
            //W or up arrow(Move selection)
            if (keyUp) {
                if (inLocation > 0) {
                    inY -= boxChangeY;
                    inLocation--;
                    inputWait = 10;
                }
            }
            //S or down arrow(move selection)
            if (keyDown) {
                if (inLocation < 4) {
                    inY += boxChangeY;
                    inLocation++;
                    inputWait = 10;
                }
            }
            //Enter key(Make a choice)
            if (keyEnter) {
                if (inLocation == 0) {
                    option = OPTION.ITEMS;
                    inputWait = 5;
                }
                if (inLocation == 1) {
                    option = OPTION.EQUIPMENT;
                    inputWait = 5;
                }
                if (inLocation == 2) {
                    option = OPTION.MAGIC;
                    inputWait = 5;
                }
                if (inLocation == 3) {
                    option = OPTION.STATUS;
                    inputWait = 5;
                }
                if (inLocation == 4) {
                    option = OPTION.SAVE;
                    inputWait = 20;
                }
                keyEnter = false;
            }
        }

        //Set actions for specific choices in the menu
        //Items
        if (option == OPTION.ITEMS) {
            //W or up arrow(move selection)
            if (keyUp) {
                if (sectionLoc == 0) inMenu.loadOldItems();
                if (sectionLoc - 1 != -1) sectionLoc--;
                inputWait = 8;
            }
            //S or down arrow(move selection)
            if (keyDown) {
                if (sectionLoc == 3) inMenu.loadNextItems();
                if (inMenu.getTotalItems() > sectionLoc + 1 && sectionLoc < 3) sectionLoc++;
                inputWait = 8;
            }
            //Enter key(Make a choice)
            if (keyEnter) {
                if (confirmUse) {
                    inMenu.useItem(); //then use item
                    confirmUse = false;
                    keyEnter = false;
                }
                if (inMenu.checkCount() > 0 && keyEnter) confirmUse = true;
                inputWait = 10;
            }
            //Back space(Go back on your last choice)
            if (keyBack) confirmUse = false;
        }

        //Equipment
        if (option == OPTION.EQUIPMENT) {
            //W or up arrow(move selection)
            if (keyUp) {
                if (sectionLoc == 0) inMenu.loadOldItems();
                if (sectionLoc - 1 != -1) sectionLoc--;
                inputWait = 8;
            }
            //S or down arrow(move selection)
            if (keyDown) {
                if (sectionLoc == 3) inMenu.loadNextEquipment();
                if (inMenu.getTotalEquipment() > sectionLoc + 1 && sectionLoc < 3) sectionLoc++;
                inputWait = 8;
            }
        }

        //Saving
        if (option == OPTION.SAVE) {
            //Key enter(Save the file)
            if (keyEnter) {
                save.saveState(file);
                inputWait = 20;
                option = OPTION.NONE;
            }
        }

        //Backspace(if a choice has been made, this backs out of it)
        if (keyBack && option != OPTION.NONE) {
            option = OPTION.NONE;
            inMenu.setItemLoc();
            sectionLoc = 0;
            inputWait = 8;
            //inY = 0;
            keyBack = false;
        }
        //Backspace(if a choice has not been made, this closes the inventory)
        if (keyBack && option == OPTION.NONE) {
            state = STATE.GAME;
            option = OPTION.NONE;
            inLocation = 0;
            sectionLoc = 0;
            inY = 130;
            inputWait = 8;
        }
    }

    /*public void RenderBoundsInGame() {
        //Add the tiles from the map to be updated each system cycle
        for (int i = 0; i < currentMap.getHeight() * currentMap.getHeight(); i++) {

            Tile accessTile = currentMap.accessTile(i);
            if (accessTile.isSolid()) {
                accessTile.drawBounds(Color.red);
            }
            // Check if the currentMap overlay has a mob object at that tile location

            if (currentOverlay.accessTile(i).isSolid()) {
                currentOverlay.accessTile(i).drawBounds(Color.blue);
            }
        }
    }*/

    /**
     * Inherited method
     *
     * @param keyCode Set keys for a new game action here using a switch statement
     *                Don't forget gameKeyUp
     */
    void gameKeyDown(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                keyLeft = true;
                break;
            case KeyEvent.VK_A:
                keyAttack = true;
                break;
            case KeyEvent.VK_RIGHT:
                keyRight = true;
                break;
            case KeyEvent.VK_UP:
                keyUp = true;
                break;
            case KeyEvent.VK_R:
                keyRun = true;
                break;
            case KeyEvent.VK_DOWN:
                keyDown = true;
                break;
            case KeyEvent.VK_F:
                keyAction = true;
                break;
            case KeyEvent.VK_ENTER:
                keyEnter = true;
                break;
            case KeyEvent.VK_BACK_SPACE:
                keyBack = true;
                break;
            case KeyEvent.VK_SPACE:
                keySpace = true;
                break;
            case KeyEvent.VK_ESCAPE:
                keyEscape = true;
                break;
            case KeyEvent.VK_2:
                keyHealthUp = true;
                break;
            case KeyEvent.VK_1:
                keyHealthDown = true;
                break;
            case KeyEvent.VK_4:
                keyMagicUp = true;
                break;
            case KeyEvent.VK_3:
                keyMagicDown = true;
                break;
            case KeyEvent.VK_F1:
                keyFPS = true;
                break;
            case KeyEvent.VK_F2:
                keyUI = true;
                break;
        }
    }

    /**
     * Inherited method
     *
     * @param keyCode Set keys for a new game action here using a switch statement
     *                Dont forget gameKeyDown
     */
    void gameKeyUp(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                keyLeft = false;
                break;
            case KeyEvent.VK_A:
                keyAttack = false;
                break;
            case KeyEvent.VK_RIGHT:
                keyRight = false;
                break;
            case KeyEvent.VK_D:
                keyRight = false;
                break;
            case KeyEvent.VK_UP:
                keyUp = false;
                break;
            case KeyEvent.VK_R:
                keyRun = false;
                break;
            case KeyEvent.VK_DOWN:
                keyDown = false;
                break;
            case KeyEvent.VK_F:
                keyAction = false;
                break;
            case KeyEvent.VK_ENTER:
                keyEnter = false;
                break;
            case KeyEvent.VK_BACK_SPACE:
                keyBack = false;
                break;
            case KeyEvent.VK_SPACE:
                keySpace = false;
                break;
            case KeyEvent.VK_ESCAPE:
                keyEscape = false;
                break;
        }
    }

    /**
     * Inherited method
     * Currently unused
     */
    void gameMouseDown() {
    }

    //From the title screen, load a game file by having the super class get the data,
    // then handling where the pieces of the data will be assigned here.

    /**
     * Inherited method
     * Currently if the game is running and the sword is out, the player attacks with it
     */
    void gameMouseUp() {

    }

    /**
     * Inherited Method
     * Currently unused
     */
    void gameMouseMove() {
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    /*
     * Used by the Timer every 30 seconds
     * to update the Data class with the current variables
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    	updateData();
        save.saveState(file);
        System.out.println("AutoSave");
    }

    public void setHealth(int health){
    	inMenu.setHealth(health);
    }
    
    public void setMagic(int magic){
    	inMenu.setMagic(magic);
    }

	/**
	 * @return the playerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param playerName the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * @return the mapX
	 */
	public int getMapX() {
		return mapX;
	}

	/**
	 * @param mapX the mapX to set
	 */
	public void setMapX(int mapX) {
		this.mapX = mapX;
	}

	/**
	 * @return the mapY
	 */
	public int getMapY() {
		return mapY;
	}

	public void setPlayerX(int x){
		playerX = x;
	}
	
	public void setPlayerY(int y){
		playerY= y;
	}
	
	public void setState(STATE s){
		state = s;
	}
	public STATE getState(){
		return state;
	}
	/**
	 * @param mapY the mapY to set
	 */
	public void setMapY(int mapY) {
		this.mapY = mapY;
	}
	
	//Updates the Data class with the current variables 
	public void updateData(){
		data.setHeight(SCREENHEIGHT);
		data.setWidth(SCREENWIDTH);
		data.setMapLocation(mapX, mapY);
		data.setPlayerLocation(perfectX, perfectY);
		data.setName(playerName);	
		data.setPlayerHealth(inMenu.getHealth());
		data.setPlayerMagic(inMenu.getMagic());
		}

	public boolean isFullScreen() {
		return fullScreen;
	}

	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

	/**
	 * @return the npcMaxHealth
	 */
	public int getNpcMaxHealth() {
		return npcMaxHealth;
	}

	/**
	 * @param npcMaxHealth the npcMaxHealth to set
	 */
	public void setNpcMaxHealth(int npcMaxHealth) {
		this.npcMaxHealth = npcMaxHealth;
	}

	}

