/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 * Date: June 27, 2015
 * <p>
 * <p>
 * Title: Base Game Entity
 * Description: Basic variable class that every Sprite inherits from. This class contains various
 * variables related to a sprites positioning and status.
 * <p>
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/
package axohEngine2.entities;

public class BaseGameEntity {

    /********************
     * Variables
     ********************/
    protected boolean alive; //Life boolean
    protected double x, y; //Position
    protected double velX, velY; //Physical movement
    protected double moveAngle, faceAngle; //Facing degree

    /*************************************************************************************
     * Constructor
     * <p>
     * This constructor is never called explicitly as it only contains variables
     * and no useful methods. It is instead called each time an imageEntity or higher
     * parent is instantiated.
     **************************************************************************************/
    protected BaseGameEntity() {
        setAlive(false);
        setX(0.0);
        setY(0.0);
        setVelX();
        setVelY();
        setMoveAngle();
        setFaceAngle();
    }

    //Getters
    public boolean isAlive() {
        return alive;
    }

    //Setters: i is an increase
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX() {
        this.velX = 0.0;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY() {
        this.velY = 0.0;
    }

    public void incX(double i) {
        this.x += i;
    }

    public void incY(double i) {
        this.y += i;
    }

    public void incVelX(double i) {
        this.velX += i;
    }

    public void incVelY(double i) {
        this.velY += i;
    }

    public void setMoveAngle() {
        this.moveAngle = 0.0;
    }

    public void incMoveAngle(double i) {
        this.moveAngle += i;
    }

    public void setFaceAngle() {
        this.faceAngle = 0.0;
    }

    public void inFaceAngle(double i) {
        this.faceAngle += i;
    }
}