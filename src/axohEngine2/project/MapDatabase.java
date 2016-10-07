/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 * Date: July 5, 2015
 * <p>
 * Title: Map Database
 * Description: A data handling class used for large projects. This class contains all of the spritesheets,
 * tiles, events, items, mobs and map creations since they all interlock together.
 * <p>
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/
//Package
package axohEngine2.project;

import java.awt.Graphics2D;

import axohEngine2.Judgement;

//Imports

import axohEngine2.entities.Mob;
import axohEngine2.entities.SpriteSheet;
import axohEngine2.map.Event;
import axohEngine2.map.Map;
import axohEngine2.map.Tile;

public class MapDatabase {

    //Array of maps
    public Map[] maps;
    //SpriteSheets
    SpriteSheet misc;
    SpriteSheet buildings;
    SpriteSheet environment32;
    SpriteSheet extras2;
    SpriteSheet mainCharacter;
    //Maps
    Map city;
    Map cityO;
    Map houses;
    Map housesO;
    //Tiles - Names are defined in the constructor for better identification
	Tile d;
	Tile g;
	Tile f;
	Tile b;
	Tile r;
	Tile e;
	Tile ro;
	Tile h;
	Tile hf;
	Tile c;
	Tile t1;
	Tile t2;
	Tile t3;
	Tile t4;
	Tile p1l;
	Tile p1m;
	Tile p1r;
	Tile p2l;
	Tile p2r;
	Tile p3l;
	Tile p3m;
	Tile p3r;
	Tile p4l;
	Tile p4m;
	Tile p4r;
	Tile psl;
	Tile psr;
	Tile s;
	Tile pc;
	Tile wtl;
	Tile wtm;
	Tile wtr;
	Tile wml;
	Tile wmm;
	Tile wmr;
	Tile wbl;
	Tile wbm;
	Tile wbr;
	Tile pc2;
	Tile pc3;
	Tile pc4;
    //Events
    Event warp1;
    Event warp2;
    Event getPotion;
    Event getMpotion;
    //Items
    Item potion;
    Item mpotion;
    //NPC's and Monsters
	Mob npc;
	Mob monster;
	Mob monster1;
	Mob monster2;

    /****************************************************************
     * Constructor
     * Instantiate all variables for the game
     *
     * @param frame - JFrame Window for the map to be displayed on
     * @param g2d   - Graphics2D object needed to display images
     * @param scale - Number to be multiplied by each image for correct on screen display
     *******************************************************************/
    public MapDatabase(Judgement frame, Graphics2D g2d, int scale) {
        //Currently a maximum of 200 maps possible(Can be changed if needed)
        maps = new Map[200];

        //Set up spriteSheets
        misc = new SpriteSheet("/textures/environments/environments1.png", 16, 16, 16, scale);
        buildings = new SpriteSheet("/textures/environments/4x4buildings.png", 4, 4, 64, scale);
        environment32 = new SpriteSheet("/textures/environments/32SizeEnvironment.png", 8, 8, 32, scale);
        extras2 = new SpriteSheet("/textures/extras/extras2.png", 16, 16, 16, scale);
        mainCharacter = new SpriteSheet("/textures/characters/mainCharacter.png", 8, 8, 32, scale);

        //Set up tile blueprints and if they are animating
        //TODO: make these have better names. This is retarded
		d = new Tile(frame, g2d, "door", environment32, 0);
		
        f = new Tile(frame, g2d, "flower", misc, 1);
		g = new Tile(frame, g2d, "grass", misc, 0);
		b = new Tile(frame, g2d, "bricks", misc, 16, true);
		r = new Tile(frame, g2d, "walkWay", misc, 6);
		e = new Tile(frame, g2d, "empty", misc, 7);
		ro = new Tile(frame, g2d, "rock", misc, 2);
		h = new Tile(frame, g2d, "house", buildings, 0, true);
		hf = new Tile(frame, g2d, "floor", misc, 8);
		c = new Tile(frame, g2d, "chest", extras2, 0, true);
		p1l = new Tile(frame, g2d, "platform1left", misc, 32, true);
		p1m = new Tile(frame, g2d, "platform1mid", misc, 33, true);
		p1r = new Tile(frame, g2d, "platform1right", misc, 34, true);
		p2l = new Tile(frame, g2d, "platform2left", misc, 35, true);
		p2r = new Tile(frame, g2d, "platform2right", misc, 36, true);
		p3l = new Tile(frame, g2d, "platform3left", misc, 37, true);
		p3m = new Tile(frame, g2d, "platform3mid", misc, 38, true);
		p3r = new Tile(frame, g2d, "platform3right", misc, 39, true);
		p4l = new Tile(frame, g2d, "platform4left", misc, 40, true);
		p4m = new Tile(frame, g2d, "platform4mid", misc, 41, true);
		p4r = new Tile(frame, g2d, "platform4right", misc, 42, true);
		psl = new Tile(frame, g2d, "platformstairleft", misc, 44, true);
		psr = new Tile(frame, g2d, "platformstairright", misc, 45, true);
		s = new Tile(frame, g2d, "stair", misc, 43);
		pc = new Tile(frame, g2d, "platformcorner", misc, 46, true);
		pc2 = new Tile(frame, g2d, "platformCorner2", misc, 51, true);
		pc3 = new Tile(frame, g2d, "platformCorner3", misc, 52, true);
		pc4 = new Tile(frame, g2d, "platformCorner4", misc, 53, true);
		wtl = new Tile(frame, g2d, "waterTopLeft", misc, 48, true);
		wtm = new Tile(frame, g2d, "waterTopMid", misc, 49, true);
		wtr = new Tile(frame, g2d, "waterTopRight", misc, 50, true);
		wml = new Tile(frame, g2d, "waterMidLeft", misc, 64, true);
		wmm = new Tile(frame, g2d, "waterMidMid", misc, 65, true);
		wmr = new Tile(frame, g2d, "waterMidRight", misc, 66, true);
		wbl = new Tile(frame, g2d, "waterBotLeft", misc, 80, true);
		wbm = new Tile(frame, g2d, "waterBotMid", misc, 81, true);
		wbr = new Tile(frame, g2d, "waterBotRight", misc, 82, true);


        //Set the tile blueprints in an array for the Map
		Tile[] cityTiles = {g, g, pc4, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, pc, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g,
			    g, g, p2r, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p3l, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, p3m, pc, g, g, g, g,
			    g, pc4, p3r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p4l, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p4m, p3l, pc, g, g, g,
			    g, p2r, p4r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p4l, p3l, pc, g, g,
			    g, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p4l, p2l, g, g,
			    g, p2r, g, g, f, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, f, g, f, g, g, g, g, g, p2l, g, g,
			    g, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, wtl, wtr, f, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    p1r, pc2, p1r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, wml, wmr, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    p2r, g, p2r, g, g, g, g, f, g, g, g, g, g, g, g, g, g, g, g, g, g, wml, wmr, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    p2r, g, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, wml, wmr, g, g, g, g, g, g, c, g, c, g, g, g, g, g, p2l, g, g,
			    p2r, g, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, wml, wmr, g, g, f, f, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    p2r, pc4, p3r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, wml, wmr, g, g, f, f, g, g, c, g, c, g, g, g, g, g, p2l, g, g,
			    p2r, p2r, p4r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, f, f, g, g, wbl, wbr, g, g, g, g, g, g, g, g, g, g, g, g, g, p1l, pc3, g, g,
			    p2r, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g, g,
			    p2r, p2r, g, g, g, g, g, f, f, g, g, g, g, g, g, g, g, g, g, g, g, g, g, f, g, g, g, g, g, g, g, g, g, g, g, g, p3l, pc, g, g,
			    p2r, p2r, g, g, g, g, g, f, f, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p4l, p2l, g, g,
			    p2r, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    p3r, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    p4r, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p3l, pc, g,
			    pc4, p3r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, wtl, wtm, wtm, wtm, wtr, g, g, g, g, g, g, g, g, g, g, g, p4l, p2l, g,
			    p2r, p4r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, wml, wmm, wmm, wmm, wmr, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g,
			    p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, wbl, wbm, wbm, wbm, wbr, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g,
			    p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g,
			    p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, f, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g,
			    p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g,
			    p2r, g, g, g, g, g, g, g, f, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g,
			    p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g,
			    p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g,
			    p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p1l, pc3, g,
			    p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    pc2, p1r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    g, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    g, pc2, p1r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, g,
			    g, g, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p1l, p1m, p1m, p1m, p1m, p1m, p1m, p1r, g, g, g, g, g, g, g, g, g, p1l, pc3, g, g,
			    g, g, pc2, p1m, p1r, g, g, g, g, g, g, p1l, p1m, p1m, p1m, p1m, p1m, p1m, p1m, pc3, p1l, p1m, p1m, p1m, p1m, p1r, pc2, p1m, p1m, p1m, p1m, p1r, g, g, g, g, p2l, g, g, g,
			    g, g, g, g, pc2, p1m, p1m, p1m, p1m, p1m, p1m, pc3, g, g, g, g, g, g, g, g, p2l, g, g, g, g, pc2, p1r, g, g, g, g, pc2, p1r, g, g, g, p2l, g, g, g,
			    g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, p1l, p1m, p1m, pc3 , g, g, g, g, g, p2r, g, g, g, g, g, pc2, p1m, p1m, p1m, pc3, g, g, g};

		//TODO:
		// Tip: e = empty
		Tile[] cityOTiles = {e, e, e, h, e, e, e, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, e, e, e, h, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, c, e, e, e, c, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, p1l, p1m, c, p1m, p1m, p1m, p1r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, c, e, ro, e, e, e, e, e, e, p2l, g, g, g, g, g, p2r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, p2l, g, g, g, g, g, p2r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, c, c, c, c, e, e, p3l, psr, g, psl, pc, g, p2r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, p4l, p4r, g, p4l, p2l, g, p2r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, p2l, g, p2r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, p2l, g, p2r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, p2l, g, p2r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, p2l, g, p2r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, p2l, g, p2r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, p2l, g, p2r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, p3l, p3m, p3r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, p4l, p4m, p4r, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
				 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e};


        /*Tile[] houseTiles = {e, e, e, e, e, e,
        		e, e, e, e, e, e,
        		e, e, e, e, e, e,
        		e, e, e, e, e, e,
        		e, e, e, e, e, e,
        		e, e, e, e, e, e,};*/

        /*Tile[] houseOTiles = {e, e, e, e, e, e,
        		e, e, e, e, e, e,
        		e, e, e, e, e, e,
        		e, e, e, e, e, e,
        		e, e, e, e, e, e,
        		e, e, e, e, e, e,};*/
        
        Tile[] houseTiles = {hf, hf, hf, hf, hf, hf,
                hf, hf, hf, hf, hf, hf,
                hf, hf, hf, hf, hf, hf,
                hf, hf, hf, hf, hf, hf,
                hf, hf, hf, hf, hf, hf,
                hf, hf, hf, hf, hf, hf};
        
        Tile[] houseOTiles = {hf, hf, hf, hf, hf, hf,
                hf, hf, hf, hf, hf, hf,
                hf, hf, hf, hf, hf, hf,
                hf, hf, hf, hf, hf, hf,
                hf, hf, hf, hf, hf, hf,
                hf, hf, hf, hf, hf, hf};


        //Put the maps together and add them to the array of possible maps
        city = new Map(frame, g2d, cityTiles, 40, 40, "city", 10);
		initMapTiles(cityTiles);
        maps[0] = city;
        cityO = new Map(frame, g2d, cityOTiles, 40, 40, "cityO", 10);
		initMapTiles(cityOTiles);
        maps[1] = cityO;
        houses = new Map(frame, g2d, houseTiles, 6, 6, "houses", 10);
        maps[2] = houses;
        housesO = new Map(frame, g2d, houseOTiles, 6, 6, "housesO", 10);
        maps[3] = housesO;

        //Put together all items (Dont forget to add these to the count and setup methods in inGameMenu.java)
        potion = new Item(frame, g2d, extras2, "Potion");
        potion.setHealItem(20);
        mpotion = new Item(frame, g2d, extras2, "Mega Potion");
        potion.setHealItem(30);

        //Put together all events
        //Warping events
        warp1 = new Event("fromHouse", TYPE.WARP);
        warp1.setWarp("city", "cityO", 200, -50);
        warp2 = new Event("toHouse", TYPE.WARP);
        warp2.setWarp("houses", "housesO", 620, 250);

        //Item events
        getPotion = new Event("getPotion", TYPE.ITEM);
        getPotion.setItem(potion);
        getMpotion = new Event("getMpotion", TYPE.ITEM);
        getMpotion.setItem(mpotion);

        //Add the events to their specific tiles and maps
        houses.accessTile(5).addEvent(warp1);
        cityO.accessTile(92).addEvent(getPotion);
        cityO.accessTile(242).addEvent(getPotion);
        cityO.accessTile(328).addEvent(getPotion);
        cityO.accessTile(327).addEvent(getMpotion);
        cityO.accessTile(326).addEvent(getMpotion);
        cityO.accessTile(325).addEvent(getMpotion);
        cityO.accessTile(93).addEvent(getMpotion);
        cityO.accessTile(94).addEvent(getMpotion);
        cityO.accessTile(95).addEvent(getMpotion);
        cityO.accessTile(96).addEvent(getMpotion);

        //Set up Monsters and NPCs
        /*npc = new Mob(frame, g2d, mainCharacter, 40, TYPE.RANDOMPATH, "npc", false);
        npc.setMultBounds(6, 50, 92, 37, 88, 62, 92, 62, 96);
		npc.setMoveAnim(32, 48, 40, 56, 3, 8);
		npc.setHealth(60);*/
        
		npc = new Mob(frame, g2d, mainCharacter, 40, TYPE.RANDOMPATH, "npc", false);
		npc.setBounds(16, 30, 60);
		npc.setMoveAnim(32, 48, 40, 56, 3, 8);
		npc.setHealth(100);
		
		monster = new Mob(frame, g2d, mainCharacter, 40, TYPE.RANDOMPATH, "monster", false);
		monster.setBounds(16, 30, 60);
		monster.setMoveAnim(32, 48, 40, 56, 3, 8);
		monster.setHealth(50);
		
		monster1 = new Mob(frame, g2d, mainCharacter, 40, TYPE.RANDOMPATH, "monster1", false);
		monster1.setBounds(16, 30, 60);
		monster1.setMoveAnim(32, 48, 40, 56, 3, 8);
		monster1.setHealth(250);
		
		monster2 = new Mob(frame, g2d, mainCharacter, 40, TYPE.RANDOMPATH, "monster2", false);
		monster2.setBounds(16, 30, 60);
		monster2.setMoveAnim(32, 48, 40, 56, 3, 8);
		monster2.setHealth(25);
		
		cityO.addMobToMap(monster);
		cityO.accessTile(425).addMob(monster);
		cityO.accessTile(630).addMob(npc);

        //Add the mobs to their tile home
        //cityO.accessTile(98).addMob(npc);
    }

    /************************************************************
     * Get a map back  based on its index in the array of maps
     *
     * @param index - Position in the maps array
     * @return - Map
     *************************************************************/
    public Map getMap(int index) {
        return maps[index];
    }
    
	public void initMapTiles(Tile[] mapTiles) 
	{
		for(int f = 0; f < (int)(mapTiles.length); f++)
		{
			mapTiles[f].tileX = f % (int)Math.sqrt(mapTiles.length);
			mapTiles[f].tileY = f / (int)Math.sqrt(mapTiles.length);
			//System.out.println("" + mapTiles[f].tileX + " - " + mapTiles[f].tileY);
		}
	}
}