/**********************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 * Date: June 15, 2015
 * Test
 * Title: Data
 * Description: Hold data from a game in variables for serializing later
 * <p>
 * <p>
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 *********************************************************************/
//Package
package axohEngine2.data;

import java.io.Serializable;

public class Data implements Serializable {

	private static final long serialVersionUID = -4668422157233446222L;

    //Variables to be updated and saved or accessed at some point
    private int _playerX, _playerY, mapX, mapY;
    private String name;
    private int width, height;
	private int playerHealth, playerMagic;

    public int getPlayerHealth() {
		return playerHealth;
	}

	public void setPlayerHealth(int playerHealth) {
		this.playerHealth = playerHealth;
	}

	public int getPlayerMagic() {
		return playerMagic;
	}

	public void setPlayerMagic(int playerMagic) {
		this.playerMagic = playerMagic;
	}

    public void setWidth(int i){
    	width = i;
    }
    
    public void setHeight(int i){
    	height = i;
    }
    
    public int getWidth(){
    	return width;
    }
    
    public int getHeight(){
    	return height;
    }

    public int getPlayerX() {
        return _playerX;
    }

    public int getPlayerY() {
        return _playerY;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public void setPlayerLocation(int x, int y) {
        _playerX = x;
        _playerY = y;
    }
    
    public void setMapLocation(int x, int y){
    	mapX = x;
    	mapY = y;
    }
    
    public int getMapX(){
    	return mapX;
    }
    
    public int getMapY(){
    	return mapY;
    }
    
}
