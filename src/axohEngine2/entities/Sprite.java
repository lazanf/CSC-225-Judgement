/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 1.2
 * Date: July 5, 2015
 * <p>
 * Title: Sprite
 * Description: Define a more complex image object with bounding boxes and lifespans
 * <p>
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/
//Package
package axohEngine2.entities;

//Imports

import axohEngine2.project.TYPE;

import javax.swing.*;
import java.awt.*;

public class Sprite {

    /***************
     * Variables
     ***************/
    // entity - The class which contains location data about the sprite
    // image - The actual pixel data of an image
    // sheet - The spriteSheet the sprite image can be found on
    // spriteNumber - The position on the spriteSheet the image is found
    // scale - The number the sprite width and height will be multiplied with
    // when displaying the image
    protected ImageEntity entity;
    protected int spriteNumber;
    protected SpriteSheet sheet;
    protected int scale;
    // currentState - Int which defines many possible states(This could be
    // changed to an enum in the future for better readability)
    // sprType - The Type of the sprite, warping, npc, player, ai etc.
    // (Reference Type.java class)
    // _collided - boolean defining if a sprite has recently hit something solid
    /*
     * _lifespan, _lifeage - lifeage always starts 0 while lifespan can be set
	 * to define a length of time for a sprite to exist in the sytem before it
	 * is taken out. Each update, the age goes up one, if it becomes greater
	 * than the span, the sprite is taken out of the system.
	 */
    protected int currentState;
    protected TYPE sprType;
    protected boolean _collided;
    protected int _lifespan, _lifeage;
    // spriteSize - pixel size of the sprite, width * height
    // boundLeg/Left/Right/HeadX/Y - X and Y ints defining Bounding boxes on the
    // sprite used for collision (Hit boxes)
    // left,right,head,leg - Boolean defining if a bound is being used
    // hasMultBounds - Set to true if there is more than one bound being used
    // boundSize - Width * height of the hit boxes on the sprite
    protected int spriteSize;
    protected int _boundLegX, _boundLegY;
    protected int _boundLeftX, _boundLeftY;
    protected int _boundRightX, _boundRightY;
    protected int _boundHeadX, _boundHeadY;
	protected boolean left = false, right = false, head = false, leg = false;
	protected int leftX, rightX, topX, bottomX, swordX;
	protected int leftY, rightY, topY, bottomY, swordY;
	protected int leftWidth, rightWidth, topWidth, bottomWidth, swordWidth;
	protected int leftHeight, rightHeight, topHeight, bottomHeight, swordHeight;

    protected boolean hasMultBounds = false;
    protected int boundSize;
    // nx,ny - Offset x value from the x or y position of the sprite to the hit
    // box
    // solid - boolean to check for a colission of a sprite
    protected int nx, ny;
    private Image image;
    private boolean solid = false;

    /*******************************************
     * Constructor - Set all default parameters
     *
     * @param frame - JFrame Window the sprite will be displayed in
     * @param g2d   - Graphics needed to display the image
     ******************************************/
    public Sprite(JPanel frame, Graphics2D g2d) {
        entity = new ImageEntity(frame);
        image = null;
        entity.setGraphics(g2d);
        entity.setAlive(true);
        currentState = 0;
        _collided = false;
        _lifespan = 0;
        _lifeage = 0;
    }

    /*********************************************
     * Set an image for the sprite or change the current one
     *
     * @param sheet        - spriteSheet which contains the image for the sprite
     * @param spriteNumber - Int location on the spriteSheet the image is located on
     * @return - The Image object that is now the sprite
     ***********************************************/
    public Image setSprite(SpriteSheet sheet, int spriteNumber) {
        image = sheet.getSprite(spriteNumber);
        entity.setImage(image);
        if (spriteSize == 0)
            spriteSize = sheet.getSpriteSize() * sheet.getScale();
        return image;
    }

    /***********************************************
     * Set the default bounding box on the sprite
     *
     * @param BoundSize - Width * height of the bound
     * @param x         - int x location of the box based on the top left corner of
     *                  the sprite
     * @param y         - int y location of the box based on the top left corner of
     *                  the sprite
     ***********************************************/
    public void setBounds(int BoundSize, int x, int y) {
        boundSize = BoundSize * scale;
        nx = x;
        ny = y;

        _boundLegX = x;
        _boundLegY = y;
    }
    
	public void setLeftBound(int leftX, int leftY, int leftWidth, int leftHeight) {
		this.leftWidth = leftWidth;
		this.leftHeight = leftHeight;
		this.leftX = leftX;
		this.leftY = leftY;
	}
	
	public void setRightBound(int rightX, int rightY, int rightWidth, int rightHeight) {
		this.rightWidth = rightWidth;
		this.rightHeight = rightHeight;
		this.rightX = rightX;
		this.rightY = rightY;
	}
	
	public void setTopBound(int topX, int topY, int topWidth, int topHeight) {
		this.topWidth = topWidth;
		this.topHeight = topHeight;
		this.topX = topX;
		this.topY = topY;
	}
	public void setBottomBound(int bottomX, int bottomY, int bottomWidth, int bottomHeight) {
		this.bottomWidth = bottomWidth;
		this.bottomHeight = bottomHeight;
		this.bottomX = bottomX;
		this.bottomY = bottomY;
	}
	public void setSwordBound(int swordX, int swordY, int swordWidth, int swordHeight) {
		this.swordWidth = swordWidth;
		this.swordHeight = swordHeight;
		this.swordX = swordX;
		this.swordY = swordY;
	}

    /*******************************************************************************
     * Set up 5 different bounds on the sprite In the future, this should be a
     * very different method when bounds become their own seperate class with
     * which contains all of these many variables in simpler form. Instead, this
     * method will be instantiating and adding another bound to the list of
     * bounds on a sprite.
     * <p>
     * Initially, the legBoundX and legBoundY is the currently selected bound
     * for the sprite to use in collision
     *
     * @param BoundSize   - width * height of the bound
     * @param boundLegX   - x position of this bound
     * @param boundLegY   - y position of this bound
     * @param boundLeftX  - x position of this bound
     * @param boundLeftY  - y position of this bound
     * @param boundRightX - x position of this bound
     * @param boundRightY - y position of this bound
     * @param boundHeadX  - x position of this bound
     * @param boundHeadY  - y position of this bound
     ******************************************************************************/

    public void setMultBounds(int BoundSize, int boundLegX, int boundLegY, int boundLeftX, int boundLeftY,
                              int boundRightX, int boundRightY, int boundHeadX, int boundHeadY) {
        // Determines the overall size of all bounds
        boundSize = BoundSize * scale;

        // Updates the Offset valuex from the x or y position of the sprite to
        // the hit box
        nx = boundLegX;
        ny = boundLegY;

        // Sets the integer value of the Leg bound x,y coordinate
        _boundLegX = boundLegX;
        _boundLegY = boundLegY;

        // Update variable to indicate that this sprite has a leg bound collider
        leg = true;

        // Sets the integer value of the Left bound x,y coordinate
        _boundLeftX = boundLeftX;
        _boundLeftY = boundLeftY;

        // Update variable to indicate that this sprite has a left bound
        // collider
        left = true;

        // Sets the integer value of the Right bound x,y coordinate
        _boundRightX = boundRightX;
        _boundRightY = boundRightY;

        // Update variable to indicate that this sprite has a right bound
        // collider
        right = true;

        // Sets the integer value of the Head bound x,y coordinate
        _boundHeadX = boundHeadX;
        _boundHeadY = boundHeadY;

        // Update variable to indicate that this sprite has a head bound
        // collider
        head = true;

        // Update variable to indicate that this sprite has mutliple bounds
        hasMultBounds = true;

    }

    // Turn on or off possible bounds(Toggling)
    public void toggleLeg(boolean change) {
        leg = change;
    }

    public void toggleLeft(boolean change) {
        left = change;
    }

    public void toggleRight(boolean change) {
        right = change;
    }

    public void toggleHead(boolean change) {
        head = change;
    }

    // Turn all bounds off
    public void allBoundsOff() {
        leg = false;
        head = false;
        left = false;
        right = false;
    }

    /**************************************************************
     * Check each bound for intersection using another rectangle
     *
     * @param r - Rectangle to check for intersection
     * @return - Boolean for if the intersection is happening
     ***************************************************************/
    public boolean checkLeftBound(Rectangle r) {
        // If the left bound of the the Rectangle is being used..
        if (left) {
            // Checks if the rectangle on the left side is intersecting with
            // something
            if (r.intersects(entity.getBounds(boundSize, _boundLeftX, _boundLeftY))) {
                // returns that the left bound side of the Rectangle is
                // intersecting something
                return true;
            }
        }

        // In the case where the programmer checks the left bound,
        // even if it's false, just return false
        return false;
    }

    public boolean checkRightBound(Rectangle r) {
        if (right) {
            if (r.intersects(entity.getBounds(boundSize, _boundRightX, _boundRightY)))
                return true;
        }
        return false;
    }

    public boolean checkHeadBound(Rectangle r) {
        if (head) {
            if (r.intersects(entity.getBounds(boundSize, _boundHeadX, _boundHeadY)))
                return true;
        }
        return false;
    }

    public boolean checkLegBound(Rectangle r) {
        if (leg) {
            if (r.intersects(entity.getBounds(boundSize, _boundLegX, _boundLegY)))
                return true;
        }
        return false;
    }

    // Getters for image, solid, boundSize and spriteSize
    public Image getImage() {
        return image;
    }

    // Set the imageEntity image variable
    public void setImage(Image image) {
        entity.setImage(image);
    }

    public boolean solid() {
        return solid;
    }

    public int getBoundSize() {
        return boundSize;
    }

    public int getSpriteSize() {
        return spriteSize;
    }

    /*******************************************
     * Set the sprite to be solid or not
     *
     * @param solid - The new solid modifier
     *******************************************/
    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    /*******************************************************************
     * Get a bound x or y position based on the direction a sprite is hitting
     * another 0 - left 1 - right 2 - head 3 - leg
     *
     * @param hitDir - Described above as direction which is being collided from
     * @return - x or y position of the bound being collided
     *******************************************************************/
    public double getBoundX(int hitDir) {
        if (hitDir == 0)
            return entity.getX() + _boundLeftX;
        if (hitDir == 1)
            return entity.getX() + _boundRightX;
        if (hitDir == 2)
            return entity.getX() + _boundHeadX;
        if (hitDir == 3)
            return entity.getX() + _boundLegX;
        return entity.getX() + nx;
    }

    public double getBoundY(int hitDir) {
        if (hitDir == 0)
            return entity.getY() + _boundLeftY;
        if (hitDir == 1)
            return entity.getY() + _boundRightY;
        if (hitDir == 2)
            return entity.getY() + _boundHeadY;
        if (hitDir == 3)
            return entity.getY() + _boundLegY;
        return entity.getY() + ny;
    }

    /********************************************************
     * Getters for specific bound x and y positions, fact of multiple bounds and
     * for the rectangles pertaining to those bounds
     *
     * @return - Various bounding box data
     *********************************************************/

	public boolean hasMultBounds() {
		return hasMultBounds;
	}

	public Rectangle getHeadBound() {
		return entity.getBounds(boundSize, _boundHeadX, _boundHeadY);
	}

	public Rectangle getLegBound() {
		return entity.getBounds(boundSize, _boundLegX, _boundLegY);
	}
	
	public Rectangle getLeftBound() {
		return entity.getBounds(leftX, leftY, leftWidth, leftHeight);
	}
	
	public Rectangle getRightBound() {
		return entity.getBounds(rightX, rightY, rightWidth, rightHeight);
	}
	
	public Rectangle getTopBound() {
		return entity.getBounds(topX, topY, topWidth, topHeight);
	}
	
	public Rectangle getBottomBound() {
		return entity.getBounds(bottomX, bottomY, bottomWidth, bottomHeight);
	}
	
	public Rectangle getSwordBound() {
		return entity.getBounds(swordX, swordY, swordWidth, swordHeight);
	}

    /*******************************************************************
     * load bitmap file for individual sprites
     *
     * @param filename - String directory location the sprite image is located in
     ********************************************************************/
    public void load(String filename) {
        entity.load(filename);
    }

    /***********************************************************
     * Draw bounding rectangle around sprite bounds for debugging
     *
     * @param c - Color for the box to be
     ***********************************************************/
	public void drawBounds(Color c) {
		entity.g2d.setColor(c);
		if (!hasMultBounds) {
			entity.g2d.draw(getBounds());
		}
	}


    /**************************************************************
     * Getter and setter for the generic sprite state variable. (alive, dead,
     * collided, etc) Right now this is just an int but should in the future
     * become an enum for easier reading
     *
     * @return - currentState int variable
     ************************************************************/
    public int state() {
        return currentState;
    }

    public void setState(int state) {
        currentState = state;
    }

    // returns a bounding rectangle for the default bound
    public Rectangle getBounds() {
        if (nx != 0 || ny != 0)
            return entity.getBounds(boundSize, nx, ny);
        else
            return entity.getBounds(boundSize);
    }

    /***************************************************************
     * Setting alive boolean of the sprite and checking its status
     *
     * @return - Is the sprite Alive?
     ***************************************************************/
    public boolean alive() {
        return entity.isAlive();
    }

    public void setAlive(boolean alive) {
        entity.setAlive(alive);
    }

    /***************************************************************
     * Returns the source image width/height
     *
     * @return - Int width or height of the image
     ***************************************************************/
    public int imageWidth() {
        return entity.width();
    }

    public int imageHeight() {
        return entity.height();
    }

    // check for collision with a rectangular shape
    public boolean collidesWith(Rectangle rect) {
        return (rect.intersects(getBounds()));
    }

    // check for collision with another sprite
    public boolean collidesWith(Sprite sprite) {
        return (getBounds().intersects(sprite.getBounds()));
    }

    // Getters for image and display variables
    public JPanel frame() {
        return entity.frame;
    }

    public Graphics2D graphics() {
        return entity.g2d;
    }

    public Image image() {
        return entity.image;
    }

    public ImageEntity getEntity() {
        return entity;
    }

    /****************************************************
     * Get or Set the sprite Type variable
     *
     * @return - The sprite TYPE
     *****************************************************/
    public TYPE spriteType() {
        return sprType;
    }

    public void setSpriteType(TYPE type) {
        sprType = type;
    }

    /************************************************
     * Get or set for if the sprite was just collided with
     *
     * @return - Boolean for if a sprite is being collided
     ************************************************/
    public boolean collided() {
        return _collided;
    }

    public void setCollided(boolean collide) {
        _collided = collide;
    }

    /********************************************************************************************
     * Lifespan related methods
     * <p>
     * lifespan and lifeage are used to defin the possibility of a sprite
     * getting taken out of the system after a period of time known as the
     * lifespan. Each update after the sprite is created, lifeage goes up by 1.
     * If lifeage becomes greater than lifespan, the sprite is killed and taken
     * out of the system. This is done every update.
     * <p>
     * It is also possible to set the lifespan or lifeage at a time after the
     * sprite has begun existing
     *
     * @return - Int lifespan or lifeage
     *********************************************************************************************/
    public int lifespan() {
        return _lifespan;
    }

    public void setLifespan(int life) {
        _lifespan = life;
    }

    public int lifeage() {
        return _lifeage;
    }

    public void setLifeage(int age) {
        _lifeage = age;
    }

    public void updateLifetime() {
        // if lifespan is used, it must be > 0
        if (_lifespan > 0) {
            _lifeage++;
            if (_lifeage > _lifespan) { // Set a time limit for a sprite to
                // exist
                setAlive(false);
                _lifeage = 0;
            }
        }
    }// end update lifeTime
}