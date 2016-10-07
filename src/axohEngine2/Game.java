/******************************************************************************
 * @author Travis R. Dewitt
 * @version 1.1
 * Date: June 14, 2015
 * <p>
 * Title: Axoh Engine
 * Description: This class contains all of the algorithms necessary for constructing a 2D video game.
 * <p>
 * <p>
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ******************************************************************************/

//Packages
package axohEngine2;

import axohEngine2.data.Data;
import axohEngine2.data.Save;
import axohEngine2.entities.AnimatedSprite;
import axohEngine2.entities.Mob;
import axohEngine2.entities.Sprite;
import axohEngine2.map.Tile;
import axohEngine2.project.STATE;
import axohEngine2.util.Point2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Objects;

//Interface setup which implements needed java libraries
public abstract class Game extends JPanel implements Runnable {
    //For serializing(Saving system)
    private static final long serialVersionUID = 1L;
    public AnimatedSprite playerCharacter;
    protected transient Graphics2D g2d;
    protected char currentChar;
    //File variables
    protected Data data;
    protected Save save;
    // Variable that will hold all of the chestTiles within a specific range of the player
	public Tile[] tileWithinRange;
    /*********************
     * Variables
     *********************/

    //Game loop and Thread variable(Transient means it wont be serialized, certain data types cant be serialized)
    private transient Thread gameloop;
    //Game lists to keep track of game specific data as well as their accessible method counterparts
    private LinkedList<AnimatedSprite> _sprites;
    private LinkedList<Tile> _tiles;
    //Set up graphics, synchronizing, screenwidth and height
    protected transient BufferedImage backBuffer;
    private transient Toolkit tk;
    private int screenWidth, screenHeight;
    //Placeholder variable that is updated in your game, it is for saving later
    private STATE state;
    //Mouse variables
    private transient Point2D mousePos = new Point2D(0, 0);
    private boolean mouseButtons[] = new boolean[4];
    //Time and frame rate variables
    private int _frameCount = 0;
    private int _frameRate = 0;
    private int desiredRate;
    private long startTime = System.currentTimeMillis();
    //Pause game state
    private boolean _gamePaused = false;

    /***************************************************************
     * Constructor - Initialize the frame, the backBuffer, the game lists, and any other variables
     ****************************************************************/
    public Game(int frameRate, int width, int height) {
        //Set up the JFrame and initialize variables.
        setUpFrame(frameRate, width, height);
        initVariables();
        tileWithinRange = new Tile[60];
        //Start the game
        gameStartUp();
    }

    public LinkedList<AnimatedSprite> sprites() {
        return _sprites;
    }

    public LinkedList<Tile> tiles() {
        return _tiles;
    }

    public void setGameState() {
        this.state = STATE.GAME;
    }

    public boolean gamePaused() {
        return _gamePaused;
    }

    public void pauseGame() {
        _gamePaused = true;
    }

    public void resumeGame() {
        _gamePaused = false;
    }

    //Game event methods - All of these will be inherited by a child class
    abstract void gameStartUp();

    abstract void gameTimedUpdate();

    abstract void gameRefreshScreen();

    abstract void gameShutDown();

    abstract void gameKeyDown(int keyCode);

    abstract void gameKeyUp(int keyCode);

    abstract void gameMouseDown();

    abstract void gameMouseUp();

    abstract void gameMouseMove();

    abstract void spriteUpdate();

    abstract void spriteDraw();

    abstract void spriteDying();

    abstract void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2, int hitDir, int hitDir2);

    abstract void tileCollision(AnimatedSprite spr, Tile tile);

    private void initVariables() {
        //Set up backbuffer and graphics and synchronization
        backBuffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        g2d = backBuffer.createGraphics();
        tk = Toolkit.getDefaultToolkit();

        state = null;

        //Create the internal lists
        _sprites = new LinkedList<>();
        _tiles = new LinkedList<>();
    }

    private void setUpFrame(int frameRate, int width, int height) {
        //Set up JFrame window
        //Store parameters in a variables
        desiredRate = frameRate;
        screenWidth = width;
        screenHeight = height;
    }

    /********************************************************
     * Get the graphics used in-game for use in child-classes
     *
     * @return Graphics2D - Graphics object
     ********************************************************/
    public Graphics2D graphics() {
        return g2d;
    }

    /*******************************************************
     * @return frame rate - An Int pertaining to your games framerate
     ******************************************************/
    public int frameRate() {
        return _frameRate;
    }

    //Mouse events

    /*******************************************************
     * @param btn - Each mouse button is labeled with an Int, this number picks that button
     * @return boolean - Is the mouse button specified being pressed
     *******************************************************/
    public boolean mouseButton(int btn) {
        return mouseButtons[btn];
    }

    /*******************************************************
     * @return Point2D - Retrive an x and y datatype of the mouse position
     * <p>
     * Currently may not work, using built in java methods may work better.
     * Unused
     ******************************************************/
    //TODO: all getMousePos() does is return mousePos.
    // Kinda redundant, but might be annoying to change
    public Point2D mousePosition() {
        return getMousePos();
    }

    /******************************************************
     * @param g - Graphics used to render objects
     *          Override the JFrames update method and insert custom updating methods
     ******************************************************/
    public void update(Graphics g) {
        //Make sure the game renders as fast as possible but only runs the framerate amount of updates per second
        _frameCount++;
        if (System.currentTimeMillis() > startTime + 1000) {
            startTime = System.currentTimeMillis();
            _frameRate = _frameCount;
            _frameCount = 0;

            purgeSprites();
        }
        drawSprites();
        paintComponent(g);
        gameRefreshScreen();
    }

    /******************************************************
     * @param g - The Systems Graphics object
     *          <p>
     *          Override the frames Paint method, draw the backBuffer and sync with the system
     *          The purpose of this is to solve any strange rendering glitches, doing it this way
     *          allows for an image to be designed in the background and then brought forward all at once.
     ******************************************************/
    public void paintComponent(Graphics g) {
        g.drawImage(backBuffer, 0, 0, this);
        tk.sync();
    }

    //Start the game loop - initialize the Thread
    public void start() {
        gameloop = new Thread(this);
        gameloop.start();
    }

    //Using Runnable, run a loop which calls update methods for specific actions including graphics and collisions
    public void run() {
        Thread t = Thread.currentThread();
        //Basically - While this new thread is equal to the thread we make at startup, repeat
        while (t == gameloop) {
            try {
                Thread.sleep(1000 / desiredRate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //If the game is not paused, run specific update methods
            if (!gamePaused()) {
                gameTimedUpdate();
                updateSprites();
                spriteCollision();
                tileCollision();
				globalTileCollision();
				performCollision();
                //System.out.println("Number of sprites is : " + _sprites.size() + "Name of sprite 1 " +  _sprites.getFirst()._name + " sprite 2 " + _sprites.get(1)._name + " Sprite 3 " + _sprites.getLast()._name);
            }

            //Render the graphics
            update(graphics());
            repaint();
            StartTileChecks();
        }
    }

    //End the game with this method call
    public void stop() {
        gameloop = null;
        gameShutDown();
    }

    public void StartTileChecks()
    {
        // Will count the number of tiles added to the linkedList        
        int arrayIncrementer = 0;
       
        // For each chest tile on the map...
        for(int k = 0; k < _tiles.size(); k++)
        {
            // If the tile is within range
            if(_tiles.get(k).DistanceFrom(playerCharacter, _tiles.get(k)))
            {                                
                // Draw the tile that is within range color
                //_tiles.get(k).drawBounds(Color.RED);
                                
                // Add this tile into the appropriete array location
                tileWithinRange[arrayIncrementer] = _tiles.get(k);
                
                // Increment array variable adder
                arrayIncrementer++;
            }
        }
    }

    /**********************************************************************
     * @return Data
     * Get the current 'Data.java' class instance
     *********************************************************************/
    public Data data() {
        return data;
    }

    /**********************************************************************
     * Mouse Listener events
     * Inherited Method
     *
     * @param e - A MouseEvent action which will change a number that coordinates with having pressed that button
     *********************************************************************/
    void checkButtons(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                mouseButtons[1] = true;
                mouseButtons[2] = false;
                mouseButtons[3] = false;
                break;
            case MouseEvent.BUTTON2:
                mouseButtons[1] = false;
                mouseButtons[2] = true;
                mouseButtons[3] = false;
                break;
            case MouseEvent.BUTTON3:
                mouseButtons[1] = false;
                mouseButtons[2] = false;
                mouseButtons[3] = true;
                break;
        }
    }

    /**********************************************************************
     * @param e -A MouseEvent that updates a mouses position after being pressed
     *          Inherited Method
     *********************************************************************/
   
    /**********************************************************************
     * @param index - An int 1, 2, or 3
     * @return boolean - If that mouse button is being pressed, return true
     *********************************************************************/
    public boolean getMouseButtons(int index) {
        return mouseButtons[index];
    }

    /**********************************************************************
     * Set the current key being pressed to the currentChar variable
     * The purpose of this if for typeing on screen. Once the keycode's
     * char is passed in, that currentChar variable can be accessed at that
     * time for typing, instead of accessing hundreds of keycodes and chars.
     *
     * @param keyChar - A char of what is being pressed down
     *********************************************************************/
    public void setKeyChar(char keyChar) {
        currentChar = keyChar;
    }

    /**********************************************************************
     * Return an angle of X or Y value based on a degree and return it in radians
     *
     * @param angle - A Double from 0 to 360
     * @return - A Double defining an angle in Radians
     *********************************************************************/
    protected double calcAngleMoveX(double angle) {
        return Math.cos(angle * Math.PI / 180);
    }

    protected double calcAngleMoveY(double angle) {
        return Math.sin(angle * Math.PI / 180);
    }

    //update all the sprites in the current list if they are alive
    protected void updateSprites() {
        for (AnimatedSprite spr : _sprites) {
            if (spr.alive()) {
                spriteUpdate();
                if (state == STATE.GAME)
                    if (spr instanceof Mob) ((Mob) spr).updateMob(); //When the game is running, update Mobs
            }
            spriteDying();
        }
    }

    /***********************************************************************
     * Detect when a sprite intersects a sprite and call a handling method
     * currently only rectangles are used for detection.
     * <p>
     * spr1 - The first sprite (Most of the time the player)
     * spr2 - The second sprite (Most of the time a random npc or enemy)
     * hitDir - The bound which is being intersected on spr1
     * hitDir2 - The bound which is being intersected on spr2
     **************************************************************************/
    //TODO: Definitely needs to be remade to be more easily read and to make it more consistent.
    protected void spriteCollision() {
    	//questioning the use of a for loop here
        for (int i = 0; i < _sprites.size(); i++) {
            if (Objects.equals(_sprites.get(i)._name, "mainC")) playerCharacter = _sprites.get(i);

            AnimatedSprite spr1 = _sprites.get(i);

            for (AnimatedSprite _sprite : _sprites) {
                // If it is the first sprite within the linkedlist of sprites
                if (_sprite == spr1) continue;

                //hasMultBounds - Set to true if there is more than one bound being used

                // Checks whether sprite 1 & sprite 2 bounds are not being used
                boolean spr1hasMultBounds = spr1.hasMultBounds();
                boolean spr2hasMultBounds = _sprite.hasMultBounds();

                Rectangle spr1GetBounds = spr1.getBounds();

                Rectangle spr2GetBounds = _sprite.getBounds();
                Rectangle spr2RightBound = _sprite.getRightBound();
                Rectangle sp2LeftBound = _sprite.getLeftBound();
                Rectangle spr2HeadBound = _sprite.getHeadBound();
                Rectangle spr2LegBound = _sprite.getLegBound();
                
               
                if (!spr1hasMultBounds && !spr2hasMultBounds) {
                    // Determines whether sprite 1 is currently intersecting with sprite 2
                    if (spr1GetBounds.intersects(spr2GetBounds))
                        spriteCollision(spr1, _sprite, -1, -1); //spr1 and spr2 have one bound
                } else {
                    // If sprite 1 has more then one bound being used, but no bounds are being used on sprite 2
                    if (spr1hasMultBounds && !spr2hasMultBounds) {
                        // Checks if the leftBound is being intersected by sprite 2
                        if (spr1.checkLeftBound(spr2GetBounds)) spriteCollision(spr1, _sprite, 0, -1);

                        // Checks if the leftBound is being intersected by sprite 2
                        if (spr1.checkRightBound(spr2GetBounds)) spriteCollision(spr1, _sprite, 1, -1);

                        // Checks if the leftBound is being intersected by sprite 2
                        if (spr1.checkHeadBound(spr2GetBounds)) spriteCollision(spr1, _sprite, 2, -1);

                        // Checks if the leftBound is being intersected by sprite 2
                        if (spr1.checkLegBound(spr2GetBounds)) spriteCollision(spr1, _sprite, 3, -1);
                    }

                    // If sprite 2 has more then one bound being used, but no bounds are being used for sprite 1
                    if (spr2hasMultBounds && !spr1hasMultBounds) {
                        //spr2 has multiple bounds but not spr1
                        if (_sprite.checkLeftBound(spr1GetBounds)) spriteCollision(spr1, _sprite, -1, 0);

                        if (_sprite.checkRightBound(spr1GetBounds)) spriteCollision(spr1, _sprite, -1, 1);

                        if (_sprite.checkHeadBound(spr1GetBounds)) spriteCollision(spr1, _sprite, -1, 2);

                        if (_sprite.checkLegBound(spr1GetBounds)) spriteCollision(spr1, _sprite, -1, 3);

                    }
                    // this is ridiculous that someone wrote all these out.
                    if (spr2hasMultBounds && spr1hasMultBounds) { //spr2 has multiple bounds as well as spr1
                        if (spr1.checkLeftBound(sp2LeftBound))
                        	spriteCollision(spr1, _sprite, 0, 0);

                        if (spr1.checkLeftBound(spr2RightBound)) 
                        	spriteCollision(spr1, _sprite, 0, 1);

                        if (spr1.checkLeftBound(spr2HeadBound)) 
                        	spriteCollision(spr1, _sprite, 0, 2);

                        if (spr1.checkLeftBound(spr2LegBound)) 
                        	spriteCollision(spr1, _sprite, 0, 3);

                        if (spr1.checkRightBound(sp2LeftBound)) 
                        	spriteCollision(spr1, _sprite, 1, 0);

                        if (spr1.checkRightBound(spr2RightBound)) 
                        	spriteCollision(spr1, _sprite, 1, 1);

                        if (spr1.checkRightBound(spr2HeadBound)) 
                        	spriteCollision(spr1, _sprite, 1, 2);

                        if (spr1.checkRightBound(spr2LegBound)) 
                        	spriteCollision(spr1, _sprite, 1, 3);

                        if (spr1.checkHeadBound(sp2LeftBound)) 
                        	spriteCollision(spr1, _sprite, 2, 0);

                        if (spr1.checkHeadBound(spr2RightBound)) 
                        	spriteCollision(spr1, _sprite, 2, 1);

                        if (spr1.checkHeadBound(spr2HeadBound)) 
                        	spriteCollision(spr1, _sprite, 2, 2);

                        if (spr1.checkHeadBound(spr2LegBound)) 
                        	spriteCollision(spr1, _sprite, 2, 3);

                        if (spr1.checkLegBound(sp2LeftBound))
                        	spriteCollision(spr1, _sprite, 3, 0);

                        if (spr1.checkLegBound(spr2RightBound)) 
                        	spriteCollision(spr1, _sprite, 3, 1);

                        if (spr1.checkLegBound(spr2HeadBound)) 
                        	spriteCollision(spr1, _sprite, 3, 2);

                        if (spr1.checkLegBound(spr2LegBound)) 
                        	spriteCollision(spr1, _sprite, 3, 3);
                    }
                }//end multi bounds checks
            }//end inner for
        }//end outer for
    }

    /**********************************************************************
     * Same as the above spriteCollision() method but instead the collision is between
     * a sprite and a Tile. Also, currently only with rectangles.
     * <p>
     * The method gets a sprite and then gets each tile, if either objects intersects
     * any bounds made for either object the method calls a handling method
     * for dealing with very specific properties that are relative to each game
     ***********************************************************************/
    
    public boolean tileCollision() {
    	AnimatedSprite player = findPlayer();
        for(int j = 0; j < tileWithinRange.length; j++) {
        	Tile tile = tileWithinRange[j];  
            if(tile != null) {
                if(tile.getTileBounds().intersects(player.getLeftBound()) 
                || tile.getTileBounds().intersects(player.getTopBound())
                || tile.getTileBounds().intersects(player.getRightBound()) 
                || tile.getTileBounds().intersects(player.getBottomBound())) {
                	return true;
                }
            }
        }
		return false;
    }
    
	 public AnimatedSprite findPlayer() {
		 AnimatedSprite temp;
		 temp = null;
		 for(int i=0; i<_sprites.size(); i++) {
			 if(_sprites.get(i)._name == "mainC") {
				 temp = _sprites.get(i);
			 }
		 }
		 return temp;
	 }

    
    public void performCollision() {
    	if(tileCollision() == true) {
        	AnimatedSprite player = findPlayer();
            for(int j = 0; j < tileWithinRange.length; j++) {
            	Tile tile = tileWithinRange[j];  
                if(tile != null) {
                	tileCollision(player, tile);
                }
            }
    	}
    }
    public void globalTileCollision() {
    	AnimatedSprite player = findPlayer();
    	player.setCollided(tileCollision());
    }
   

    //Draw animated sprites automatically, they must be in the list (Includes tiles)
    protected void drawSprites() {
        _sprites.stream().filter(Sprite::alive).forEach(spr -> {
            spr.updateFrame();
            spriteDraw();
        });
        _tiles.forEach(axohEngine2.map.Tile::updateFrame);
    }

    //Delete the sprite that has been killed from the system
    private void purgeSprites() {
        for (int i = 0; i < _sprites.size(); i++) {
            AnimatedSprite spr = _sprites.get(i);
            if (!spr.alive()) {
                _sprites.remove(i);
            }
        }
    }

    /*********************************************************************
     * @param tile - A Tile to be added in to the system
     *             <p>
     *             Instead of just adding all of the tiles in a Map to the system for updating,
     *             use this method to add a layer of choice(filter). This method currently only
     *             allows Tiles which have properties - solid, event, breakable, etc..
     *             <p>
     *             The purpose of this is because there could be thousands of tiles in a single map
     *             not all of these tiles have properties that need updateing like animations.
     *             This allows for a much faster, smoother game experience as well as larger maps.
     *********************************************************************/
    void addTile(Tile tile) {
        if (tile.hasProperty()) tiles().add(tile);

    }

    /**********************************************************************
     * @param g2d  - Gaphics used to display to the JFrame
     * @param text - The String of text to alter
     * @param x    - An Int position relating to the X position the text will be rendered on screen
     **********************************************************************/
    void drawString(Graphics2D g2d, String text, int x) {
        for (String line : text.split("\n")) g2d.drawString(line, x, x += g2d.getFontMetrics().getHeight());

    }

	/**
	 * @return the mousePos
	 */
	public Point2D getMousePos() {
		return mousePos;
	}

	/**
	 * @param i the mousePos to set
	 */
	public void setMousePosX(int i) {
		mousePos.setX(i);
	}
	public void setMousePosY(int i) {
		mousePos.setY(i);
	}
}