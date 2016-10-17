/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 * Date: June 29, 2015
 * <p>
 * Title: Mob
 * Description: A class which adds additional abilities to a possibly animated image. used for monsters or
 * normal characters. This class has a lot of room for improvement.
 * <p>
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/
//Package
package axohEngine2.entities;



import axohEngine2.project.TYPE;
import axohEngine2.sound.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Random;


public class Mob extends AnimatedSprite implements MouseListener {

    /*************
     * Variables
     *************/
    //random - Use to obtain a randomly generated number
    //attacks - A list of attacks that can be used by a Mob
    //hostile - Can the mob attack the player?
    //health - HP
    /*ai - An enum which sets the ai of an npc, that ai needs to be written in a method and then added as
       a TYPE in the TYPE.java class before it can be used. Right now only random ai is possible, and PLAYER.*/
    //xx and yy - Variables used as a starting position from a spawn point
    //speed - How fast the mob can move(Default 2 pixels per update)
    //attacking - A possible state the mob could be in, for many kinds of checks
    //takenOut - Boolean to see if the mob has it's weapon out
    //currentAttack - The currently selected attack to use from the list of Mob attacks
    private Random random = new Random();
    private LinkedList<Attack> attacks;
    private boolean hostile;
	private int maxHealth;
    private int health;
    private TYPE ai;
    private int xx;
    private int yy;
    private int speed = 3;
    private boolean attacking;
    private boolean takenOut = false;
    private Attack currentAttack;
    
    // Should be a short sound clip.
    // Currently implemented as a Midi file.
    // Should change to .wav / .mp3 and add a class to handle both of those
    // File types in axohEngine2.Sound.
   
    //Not actualy an mp3 file
    private String soundEffect = "res\\music\\Sound Effects\\CB.mp3"; 
    private MidiSequence damageSound = new MidiSequence(soundEffect);

    //Four variable booleans depicting the last direction the mob was moving(This could be phased out of the system)
    private boolean wasRight = false;
    private boolean wasLeft = false;
    private boolean wasUp = false;
    private boolean wasDown = false;

    //moveDir - Direction the mob was moving
    //direction - The direction the Mob is facing
    //randomDir - The random choice of a direction used in random movements
    private DIRECTION moveDir;
    private DIRECTION direction;
    private DIRECTION randomDir;

    //Wait timers
    private boolean waitOn = false;
    private int wait;

    //Graphics and Window objects the mob needs for display
    private Graphics2D g2d;
    private JPanel frame;

    /************************************************************************
     * Constructor
     *
     * @param panel        - JFrame window for display
     * @param g2d          - Graphics2D object used for displaying an image on a JFrame
     * @param sheet        - The spriteSheet the animation or image is taken from
     * @param spriteNumber - The position on the spriteSheet the animation is or image starts
     * @param ai           - A TYPE enum depicting the ai's kind (radom, set path, player, etc)
     * @param name         - The character name in a String
     * @param hostility    - Boolean, is the mob going to attack the player?
     *************************************************************************/
    public Mob(JPanel panel, Graphics2D g2d, SpriteSheet sheet, int spriteNumber, TYPE ai, String name, boolean hostility) {
        super(panel, g2d, sheet, spriteNumber, name);
        attacks = new LinkedList<>();
        this.frame = panel;
        this.g2d = g2d;
        this.ai = ai;

        hostile = hostility;
        setName(name);
        health = 0;
        setSolid(true);
        setAlive(true);
        setSpriteType(ai);
		fixHealth();
    }

    //Getters for name and ai type
    public String getName() {
        return super._name;
    }

    public void setName(String name) {
        super._name = name;
    }

    public TYPE getType() {
        return ai;
    }

    //Setters for current health, ai, name and speed
	public void setHealth(int maxHealth) { 
		this.maxHealth = maxHealth;
		health = maxHealth;
	}

    public void setAi(TYPE ai) {
        this.ai = ai;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
	public int fixHealthBar (int health) {
		float newHealth = 0;
		float healthf = (float)health;
		float maxHealthf = (float)maxHealth;
		float healthOverMax = healthf/maxHealthf;
		newHealth = healthOverMax*80;
		//System.out.println((int)newHealth);
		return (int)newHealth;

	}
	
	public void fixHealth () {
		if(health < 0) {
			health = 0;
		}
	}
	

    /**************************************************
     * Set all of the movement related variables to whatever nothing is
     **************************************************/
    public void resetMovement() {
        randomDir = DIRECTION.NONE;
        wait = 0;
        waitOn = false;
        moveDir = DIRECTION.NONE;
    }

    /***************************************************************
     * Stop a mob from moving. This stop depends on the type of ai, this needs to
     * be updated every time a new ai is added. This does not work for player mobs.
     ****************************************************************/
    public void stop() {
        if (ai == TYPE.RANDOMPATH) {
            randomDir = DIRECTION.NONE;
            waitOn = true;
            wait = 100 + random.nextInt(200);
            stopAnim();
        }
    }

    /***************************************************************
     * Method thats called by the system to call various methods for many
     * different ai types. Update this for future ai types.
     ****************************************************************/
    public void updateMob() {
        if (ai == TYPE.RANDOMPATH) {
            randomPath();
        }
        if (ai == TYPE.SEARCH) {
            search();
        }
        if (ai == TYPE.CHASE) {
            chase();
        }
        if (hostile && health < 0) {
            setAlive(false);
        }
    }
    
    // Should detect whether or not a mob can move in a direction or is blocked.
    public boolean collisionDetection(DIRECTION dir){
    	return true;
    }

    /***************************************************************
     * AI logic used for the randomly moving ai type
     ****************************************************************/
    private void randomPath() {
        int xa = 0;
        int ya = 0;
        int r = random.nextInt(7);
        
        if (wait <= 0) {
            waitOn = false;
            randomDir = DIRECTION.NONE;
            moveDir = DIRECTION.NONE;
        }

        if (r == 0 && !waitOn) { //right
            randomDir = DIRECTION.RIGHT;
            waitOn = true;
            wait = random.nextInt(100);
        }
        if (r == 1 && !waitOn) { //left
            randomDir = DIRECTION.LEFT;
            waitOn = true;
            wait = random.nextInt(100);
        }
        if (r == 2 && !waitOn) { //up
            randomDir = DIRECTION.UP;
            waitOn = true;
            wait = random.nextInt(100);
        }
        if (r == 3 && !waitOn) { //down
            randomDir = DIRECTION.DOWN;
            waitOn = true;
            wait = random.nextInt(100);
        }
        if (r >= 4 && !waitOn) { //Not moving
            waitOn = true;
            wait = random.nextInt(100);
            stopAnim();
        }

        if (randomDir == DIRECTION.RIGHT) xa = speed;
        	//startAnim();
        if (randomDir == DIRECTION.LEFT) xa = -speed;
        	//startAnim();
        if (randomDir == DIRECTION.UP) ya = speed;
        	//startAnim();
        if (randomDir == DIRECTION.DOWN) ya = -speed;
        	//startAnim();
        
        // This determines whether or not to move the mob.
        if(collisionDetection(randomDir)){
        	move(xa, ya);	
        }
        
        if (waitOn) wait--;
    }

    /***************************************************************
     * AI logic used for the search for something ai type
     ****************************************************************/
    private void search() {
    	
    }

    /***************************************************************
     * AI logic used for the chase something ai type
     ****************************************************************/
    private void chase() {
    }

    /***************************************************************
     * Method used to change a mobs position by the xa and ya parameters.
     * This also updates a mobs aniamtion based on what direction is is moving
     * in. Four animations are needed for a full moving sprite.
     *
     * @param xa - Int movement in pixels on the x axis
     * @param ya - Int movement in pixels on the y axis
     ****************************************************************/
    
    
    public void move(int xa, int ya) {
        if (xa < 0) { //left
            xx += xa;

            if (moveDir != DIRECTION.LEFT) setAnimTo(leftAnim);
            startAnim();
            moveDir = DIRECTION.LEFT;
        } else if (xa > 0) { //right
            xx += xa;

            if (moveDir != DIRECTION.RIGHT) setAnimTo(rightAnim);
            startAnim();
            moveDir = DIRECTION.RIGHT;
        }

        if (ya < 0) {  //up
            yy += ya;

            if (moveDir != DIRECTION.UP) setAnimTo(upAnim);
            startAnim();
            moveDir = DIRECTION.UP;
        } else if (ya > 0) { //down
            yy += ya;

            if (moveDir != DIRECTION.DOWN) setAnimTo(downAnim);
            startAnim();
            moveDir = DIRECTION.DOWN;
        }
        if (xa == 0 && ya == 0) stopAnim();
    }

    /********************************************************************************
     * Update various player related information. Methods like these can be used for other AI's as well.
     * Update: weapon in/out, direction, keys pressed, movements etc.
     *
     * @param left  - keyCode
     * @param right - keyCode
     * @param up    - keyCode
     * @param down  - keyCode
     ********************************************************************************/
    public void updatePlayer(boolean left, boolean right, boolean up, boolean down) {
        if (left) {
            if (right || up || down) wasLeft = true;
            if (wasLeft && !up && !down && !right) {
                toggleLeg(true);
                toggleLeft(false);
                toggleRight(false);
                toggleHead(false);
                wasLeft = false;
                direction = DIRECTION.LEFT;
                if (!takenOut) setAnimTo(leftAnim);
                if (takenOut) setAnimTo(currentAttack.getMoveAnim(direction));
            }

            if (moveDir != DIRECTION.LEFT) {
                if (!takenOut) setAnimTo(leftAnim);
                if (takenOut) setAnimTo(currentAttack.getMoveAnim(DIRECTION.LEFT));
                if (hasMultBounds) {
                    toggleLeg(true);
                    toggleLeft(false);
                    toggleRight(false);
                    toggleHead(false);
                }
            }
            startAnim();
            moveDir = DIRECTION.LEFT;
            direction = DIRECTION.LEFT;
        }
        if (right) {
            if (left || up || down) wasRight = true;
            if (wasRight && !up && !down && !left) {
                toggleLeg(true);
                toggleLeft(false);
                toggleRight(false);
                toggleHead(false);
                wasRight = false;
                direction = DIRECTION.RIGHT;
                if (!takenOut) setAnimTo(rightAnim);
                if (takenOut) setAnimTo(currentAttack.getMoveAnim(direction));
            }

            if (moveDir != DIRECTION.RIGHT) {
                if (!takenOut) setAnimTo(rightAnim);
                if (takenOut) setAnimTo(currentAttack.getMoveAnim(DIRECTION.RIGHT));
                if (hasMultBounds) {
                    toggleLeg(true);
                    toggleLeft(false);
                    toggleRight(false);
                    toggleHead(false);
                }
            }
            startAnim();
            moveDir = DIRECTION.RIGHT;
            direction = DIRECTION.RIGHT;
        }
        if (up) {
            if (left || right || down) wasUp = true;
            if (wasUp && !right && !down && !left) {
                toggleLeg(false);
                toggleLeft(true);
                toggleRight(true);
                toggleHead(true);
                wasUp = false;
                direction = DIRECTION.UP;
                if (!takenOut) setAnimTo(upAnim);
                if (takenOut) setAnimTo(currentAttack.getMoveAnim(direction));
            }

            if (moveDir != DIRECTION.UP) {
                if (!takenOut) setAnimTo(upAnim);
                if (takenOut) setAnimTo(currentAttack.getMoveAnim(DIRECTION.UP));
                if (hasMultBounds) {
                    toggleLeg(false);
                    toggleLeft(true);
                    toggleRight(true);
                    toggleHead(true);
                }
            }
            startAnim();
            moveDir = DIRECTION.UP;
            direction = DIRECTION.UP;
        }
        if (down) {
            if (left || up || right) wasDown = true;
            if (wasDown && !up && !right && !left) {
                toggleLeg(false);
                toggleLeft(true);
                toggleRight(true);
                toggleHead(true);
                wasDown = false;
                direction = DIRECTION.DOWN;
                if (!takenOut) setAnimTo(downAnim);
                if (takenOut) setAnimTo(currentAttack.getMoveAnim(direction));
            }

            if (moveDir != DIRECTION.DOWN) {
                if (!takenOut) setAnimTo(downAnim);
                if (takenOut) setAnimTo(currentAttack.getMoveAnim(DIRECTION.DOWN));
                if (hasMultBounds) {
                    toggleLeg(false);
                    toggleLeft(true);
                    toggleRight(true);
                    toggleHead(true);
                }
            }
            startAnim();
            moveDir = DIRECTION.DOWN;
            direction = DIRECTION.DOWN;
        }

        if (!playOnce) attacking = false;
        if (!left && !right && !up && !down) {
            stopAnim();
        }
    }

    /*********************************************************************************
     * Check to see if a mob is currently attacking or change the state of whether
     * it is attacking or not. Depnding on the check, certain animations can be
     * played out.
     *********************************************************************************/
    public void inOutItem() {
        takenOut = !takenOut;
        int inOutAnim = currentAttack.getInOutAnim(direction);
        int inOutTotal = currentAttack.getInOutTotal();
        int inOutDelay = currentAttack.getInOutDelay();
        setFullAnim(inOutAnim, inOutTotal, inOutDelay);
        if (takenOut)
            playOnce(currentAttack.getMoveAnim(direction), currentAttack.getMoveTotal(), currentAttack.getMoveDelay(), inOutAnim + inOutTotal);
        else {
            if (direction == DIRECTION.LEFT) playOnce(leftAnim, walkFrames, walkDelay, inOutAnim + inOutTotal);
            if (direction == DIRECTION.RIGHT) playOnce(rightAnim, walkFrames, walkDelay, inOutAnim + inOutTotal);
            if (direction == DIRECTION.UP) playOnce(upAnim, walkFrames, walkDelay, inOutAnim + inOutTotal);
            if (direction == DIRECTION.DOWN) playOnce(downAnim, walkFrames, walkDelay, inOutAnim + inOutTotal);
        }
    }

    //Getters
    public boolean isTakenOut() {
        return takenOut;
    }

    public boolean attacking() {
        return attacking;
    }

    //Set the animations for attacks of whatever attack is selected and play them on screen
    public void attack() {
        attacking = true;
        setFullAnim(currentAttack.getAttackAnim(direction), currentAttack.getAttackTotal(), currentAttack.getAttackDelay());
        playOnce(currentAttack.getMoveAnim(direction), currentAttack.getMoveTotal(), currentAttack.getMoveDelay(), currentAttack.getAttackAnim(direction) + currentAttack.getAttackTotal());
    }

    /******************************************
     * Get the list of attacks the mob can use
     *
     * @return - LinkedList of attacks
     *****************************************/
    public LinkedList<Attack> attacks() {
        return attacks;
    }

    /**********************************************************
     * Add an attack to the list of possibles a mob can use
     *
     * @param name        - String differentiating one attack from another
     * @param magicDam    - Number to be used when calculating attack Magic damage
     * @param strengthDam - Number to be used when calculating attack Strength damage
     *********************************************************/
    public void addAttack(String name, int magicDam, int strengthDam) {
        attacks.add(new Attack(name, magicDam, strengthDam));
    }

    /*******************************************************************
     * @param name - String name of the attack to retrieve from the Mob
     * @return - An Attack that is wanted
     *******************************************************************/
    public Attack getAttack(String name) {
        for (Attack attack : attacks) {
            if (attack.getName().equals(name)) return attack;
        }
        return null;
    }

    /***************************************************
     * @return - Attack datatype which refers to the currently being used attack
     ***************************************************/
    public Attack getCurrentAttack() {
        return currentAttack;
    }

    /***************************************************
     * @param name - String name of the attack for the Mob to use now
     ***************************************************/
    public void setCurrentAttack(String name) {
        currentAttack = getAttack(name);
    }

    /****************************************************
     * Lower this mobs health by a damage as well by a modifier from 0 to
     * a random int of a maximum value of the damage parameter % 5.
     * 
     * Also used to do a sound effect when hurt.
     *
     * @param damage - An int used to lower this mobs health
     *****************************************************/
	public void takeDamage(double damage){ 
		health -= damage; 
		 
		
		//Sequence isn't being set for some reason.
		//System.out.println("Damage");
		//damageSound.setLooping(false);
		
		//if(damageSound.isLoaded())
			//damageSound.play();
		
		 
	}

    /***************************************************
     * @return - Getter for the current health the mob is at
     ***************************************************/
    public int health() {
        return health;
    }
    
	public int maxHealth() { 
		return maxHealth; 
	}

    /***************************************************
     * Get the x or y location of the mob in the room or
     * set a new x or y location relative to it's current position
     *
     * @return - x or y int of location
     ***************************************************/
    public double getXLoc() {
        return entity.getX();
    }

    public double getYLoc() {
        return entity.getY();
    }

    public void setLoc(int x, int y) { //Relative to current position
        xx = xx + x;
        yy = yy + y;
    }

    /**********************************************
     * Render the Mob in the game room at anx and y location
     *
     * @param x - Int x position
     * @param y - Int y position
     ***********************************************/
    public void renderMob(int x, int y) {
        g2d.drawImage(getImage(), x + xx, y + yy, getSpriteSize(), getSpriteSize(), frame);
        entity.setX(x + xx);
        entity.setY(y + yy);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.randomPath();
        System.out.println("CLICKED");
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void setLoc() {
        // TODO Auto-generated method stub

    }
}