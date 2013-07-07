package com.grovermind.game.maze;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;


public class Nav{
	private Maze maze;	
	public Nav(Maze maze){
		this.maze = maze;
	}
	
	private class GridPosition{
		private float x;
		public float getX() {return x;}
		private float y;
		public float getY() {return y;}
		
		private GridPosition(float x, float y){
			this.x = x;
			this.y = y;
		}
	}
	
	public float[] getLocation(float xPos, float yPos){		
		/* accounts for possibility that user has, potentially magically,
		 selected an area off the grid */
		if (xPos < 0)                          {xPos = 0;}
		if (xPos > maze.getWidth()  * 64 - 8) {xPos = maze.getWidth() * 64 - 8;}
		if (yPos < 0)                          {yPos = 0;}
		if (yPos > maze.getHeight() * 64 - 8) {yPos = maze.getHeight() * 64 - 8;}		
				
		int gridPos[]  = gridSnap(xPos,yPos);
		gridPos = determineDestination(gridPos);
		float centerLocation[] = locSnap2(xPos,yPos);
		//From position that is centered 1/2 the size of the avatar for bottom left corner.
		xPos = 64 * gridPos[0] + centerLocation[0] -16;
		yPos = 64 * gridPos[1] + centerLocation[1] -16;
		return new float[] {xPos,yPos};
	}
		
	private float pointDistance(float x1, float y1, float x2, float y2){
		float distance = (float) Math.sqrt( Math.pow((x1-x2),2) + Math.pow((y1-y2),2));
		return distance;
	}
	
	private float[] locSnap2(float xPos, float yPos){
		float xSnap = xPos%64;
		float ySnap = yPos%64;
		float xSideDist = 32 - Math.abs(32 - xSnap);
		float ySideDist = 32 - Math.abs(32 - ySnap);
										
		float shortestDist = pointDistance (0,0,32,32);
		
		GridPosition[] GridPos = new GridPosition[5];
		GridPos[0] = new GridPosition(0, 32);		
		GridPos[1] = new GridPosition(32,32);
		GridPos[2] = new GridPosition(32,0);
		GridPos[3] = new GridPosition(16,32);
		GridPos[4] = new GridPosition(32,16);
		
		float xTemp = xSideDist;
		float yTemp = ySideDist;

		for (int i = 0; i < 3; i++){			
			float dist = pointDistance (GridPos[i].getX(), GridPos[i].getY(), xSideDist, ySideDist);
			if (dist < shortestDist){
				shortestDist = dist;
				xTemp = GridPos[i].getX();
				yTemp = GridPos[i].getY();
			}
		}

		xSideDist = xTemp;
		ySideDist = yTemp;
		
		if (xSnap < 32){
			xSnap = xSideDist;
		}
		else {
			xSnap = 64 - xSideDist;
		}
		
	  if (ySnap < 32){
			ySnap = ySideDist;
		}
		else {
			ySnap = 64 - ySideDist;
		}
		
		return new float[] {xSnap, ySnap};		
	}
	/*		String[] directions = maze.getCarvedDirectionsByCellCoordinates(xGrid, yGrid);
	for (String dir : directions){
		if(dir.equals("NORTH")){
			
		}
		else if (dir.equals("EAST")) {
		}
		else if (dir.equals("WEST")) {
		}
		else if (dir.equals("SOUTH")) {
		}
		
	} */		
	
	private int[] gridSnap(float xPos, float yPos){
		// returns x&y coords of selected area
		int xGrid = (int) Math.floor(xPos/64);
		int yGrid = (int) Math.floor(yPos/64);
		return new int[] {xGrid, yGrid};
	}
	
	private int[] determineDestination(int[] gridPos){		
		int xGrid = (int)gridPos[0];
		int yGrid = (int)gridPos[1];			
		String[] directions = maze.getCarvedDirectionsByCellCoordinates(xGrid, yGrid);
		float xMax = maze.getWidth();
		float yMax = maze.getHeight();		
		
		
		return gridPos;
		
	}
	
	public void hat() { return; }
	
} 

