/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 * Date: July 5, 2015
 * <p>
 * Title: Item
 * Description: Create an item useable by the player with special characteristics
 * <p>
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/
//Package
package axohEngine2.project;

import axohEngine2.entities.Sprite;
import axohEngine2.entities.SpriteSheet;

import javax.swing.*;
import java.awt.*;

//Imports

public class Item extends Sprite {

    /*******************
     * Variables
     *******************/
    //health, damage - Ints depicting how much something might change ones health or damage modifier
    private int health;
    private boolean healItem = false;
    private boolean attackItem = false;
    private boolean keyItem = false;
    private boolean isEquipment;

    private String _name;

    /************************************************************************
     * Constructor
     * Define a new item with an image and other relative variables
     *
     * @param spriteNumber - Int location in the sprite sheet to find the item image
     * @param equipment    - Boolean depicting if the item is of equipment type(Very different common modifier)
     * @param frame        - JFreame window for the item to be displayed on
     * @param g2d          - Graphics2D object used to render this Item
     * @param sheet        - spriteSheet the item image can be found on
     * @param name         - String name of the item
     *************************************************************************/
    public Item(JPanel frame, Graphics2D g2d, SpriteSheet sheet, String name) {
        super(frame, g2d);
        setSprite(sheet, 2);
        this.sheet = sheet;
        isEquipment = false;
        _name = name;
    }

    /***************************************
     * Render the item on screen
     *
     * @param frame - JFrame window the Item is displayed on
     * @param g2d   - Graphics2D object needed to render images
     * @param x     - x position of the item on screen
     * @param y     - y position of the item on screen
     *****************************************/
    public void render(JPanel frame, Graphics2D g2d, int x, int y) {
        g2d.setFont(new Font("Arial", Font.PLAIN, 72));
        g2d.drawImage(getImage(), x, y, getSpriteSize(), getSpriteSize(), frame);
    }

    //Set an item to be of healing type with certain characteristics
    public void setHealItem(int healingAmount) {
        health = healingAmount;
        healItem = true;
    }

    //Same as setHealItem but for the damaging type item
    public void setAttackItem() {
        attackItem = true;
    }

    //Same as setHealItem but for key items - currently only changes a boolean for checks
    public void setKeyItem() {
        keyItem = true;
    }

    //Getter for the item name
    public String getName() {
        return _name;
    }

    //Getter for if the item is an equipment type
    public boolean isEquimpent() {
        return isEquipment;
    }

    //Remember to update the useItem() method under InGameMenu.java when adding new group of items. All are handled seperately
    //Return an int defining a quick item type check(This could be changed in to an enum in the future)
    public int checkItem() { // TODO: Under construction
        if (keyItem) {
            return 1;
        }
        if (healItem) {
            return 2;
        }
        if (attackItem) {
            return 3;
        }
        return -1;
    }

    //Getter for how much an item effects ones health
    public int getHealth() {
        return health;
    }
}
