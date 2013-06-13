package com.grovermind.game.maze;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Maze {
	private int width;
	public int getWidth(){return width;}
	private int height;
	public int getHeight(){return width;}
	private Cell[][] grid;
	private Array<Cell> cells;
	private Array<Direction> directions;
	
	public Maze(int width, int height){
		this.width = width;
		this.height = height;
		
		//Create a grid(Array) of cells with width and height of maze
		this.grid = new Cell[width][height];
		
		//Initialize the grid with Cells
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				grid[i][j] = new Cell(i, j);
			}
		}
		
		//Create a list(Array) of directions
		this.directions = new Array<Direction>(true, 4);
		for (Direction dir : Direction.values()) {
			  directions.add(dir);
		}
		
		//Create a list(Array) of cells, initially empty
		cells = new Array<Cell>(true, 1);
		
		//Add a non-edge cell at random to list of cells
		cells.add(grid[1+MathUtils.random(width-2)][1+MathUtils.random(height-2)]);
				
		
	}
	

	public void step(){
		if(cells.size > 0) {
			int cellIndex = 0;
			int randomMethod = MathUtils.random(1)+1;


			switch (randomMethod) {
            case 1:  cellIndex = cells.size -1; //Newest
                     break;
            case 2:  cellIndex = MathUtils.random(cells.size -1); //Random
                     break;
            case 3:  cellIndex = MathUtils.random(MathUtils.floor(cells.size/2)); // Middle
                     break;
            default: cellIndex = 0;  //Oldest
                     break;
			}

			
			Cell cell = cells.get(cellIndex);
			
			boolean hasCarved = false;
			//Carve a passage to any unvisited neighbor of the cell
			
			//Iterate through directions in random order
			directions.shuffle();
			for(Direction dir : directions){
				if(	cell.getX() + dir.getX() >=0 && cell.getX() + dir.getX() < width && cell.getY() + dir.getY()>=0 && cell.getY() + dir.getY() < height)
				{
					Cell neighbor  = grid[cell.getX() + dir.getX()][cell.getY() + dir.getY()];
					if(!neighbor.hasBeenVisited()){
						
						cell.carve(dir);
						neighbor.carve(dir.opposite());
						
						cells.add(neighbor);
						hasCarved = true;
						break;
					}
				}
			}
			if(!hasCarved){
				cell.setCarvingComplete();
				cells.removeIndex(cellIndex);
			}
		}
		else {
			grid[0][height-1].carve(Direction.WEST);
			grid[width-1][0].carve(Direction.EAST);
		}
	}
	
	public void generate(){
		while (cells.size > 0){
			step();
		}
	}
	

	public String[] getCarvedDirectionsByCellCoordinates(int x, int y){
		
		String[] cellDirections = {"","","",""};
		
		int cellCarvedDirectionsBitValue =  grid[x][y].getCarvedDirectionsBitValue();
		int i = 0;
		 for(Direction dir : Direction.values()){
			 if((dir.getBitValue() & cellCarvedDirectionsBitValue) == 0) {
				 cellDirections[i] = dir.name();
			 }
			 i++;	
		}
		 return cellDirections;
		
	}
	public boolean getCellVisited(int x, int y){
		return grid[x][y].hasBeenVisited();
	}
	public boolean getCellDoneCarving(int x, int y){
		return grid[x][y].isDoneCarving();
	}


	//Nested cell class
	private class Cell {
		private int x;
		public int getX() {return x;}
		private int y;
		public int getY() {return y;}
		private int bitValue;
		public boolean hasBeenVisited(){return !(bitValue == 0);}
		
		private boolean doneCarving = false;
		public boolean isDoneCarving(){return doneCarving;}
		
		private Cell(int x, int y){
			this.x = x;
			this.y = y;
			bitValue = 0;
		}

		public void carve(Direction dir){
			bitValue |= dir.getBitValue();
		}
		public void setCarvingComplete(){
			doneCarving = true;
		}
		public int getCarvedDirectionsBitValue(){
			return bitValue;
		}

		
	}
	private enum Direction{
		NORTH(0,1,1){@Override public Direction opposite() { return SOUTH; }},
		EAST(1,0,2){@Override public Direction opposite() { return WEST; }},
		SOUTH(0, -1, 4){@Override public Direction opposite() { return NORTH; }},
		WEST(-1, 0, 8){@Override public Direction opposite() { return EAST; }};
		private int x;
		public int getX() {return x;}
		private int y;
		public int getY() {return y;}
		private int bitValue;
		public int getBitValue() {return bitValue;}
		abstract public Direction opposite();
		Direction(int x, int y, int bitValue){
			this.x = x;
			this.y = y;
			this.bitValue = bitValue;
		}
	}

}
