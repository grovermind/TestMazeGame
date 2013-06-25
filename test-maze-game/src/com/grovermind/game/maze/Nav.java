package com.grovermind.game.maze;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;


public class Nav{
	private Maze maze;	
	public Nav(Maze maze){
		this.maze = maze;
	}
	
	public float[] getLocation(float xPos, float yPos){		
		// user has, potentially magically, selected an area off the size of the grid
		if (xPos < 0)                          {xPos = 0;}
		if (xPos > maze.getWidth()  * 64 - 16) {xPos = maze.getWidth() * 64 - 16;}
		if (yPos < 0)                          {yPos = 0;}
		if (yPos > maze.getHeight() * 64 - 16) {yPos = maze.getHeight() * 64 - 16;}		
				
		float gridPos[]  = gridSnap(xPos,yPos);
		gridPos = determineDestination(gridPos);
		
		float location[] = locSnap(xPos,yPos);
		xPos = location[0] * 32 - 16;
		yPos = location[1] * 32 - 16;
		return new float[] {xPos,yPos};
	}
	
	private float[] locSnap(float xPos, float yPos){	
		// centers avatar in selected position
		float xLoc  = Math.round(xPos/32);
		float yLoc  = Math.round(yPos/32);
		return new float[] {xLoc, yLoc};
	}
	private float[] gridSnap(float xPos, float yPos){
		// returns x&y coords of selected area
		float xGrid = (float) Math.floor(xPos/64);
		float yGrid = (float) Math.floor(yPos/64);
		return new float[] {xGrid, yGrid};
	}
	
	private float[] determineDestination(float[] gridPos){		
		float xGrid = gridPos[0];
		float yGrid = gridPos[1];			
		float xMax = maze.getWidth();
		float yMax = maze.getHeight();
		
		
		return gridPos;
	}
	public void hat() { return; }
	
} 

