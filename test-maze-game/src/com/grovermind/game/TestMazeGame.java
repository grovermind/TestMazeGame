package com.grovermind.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.grovermind.game.maze.Maze;

public class TestMazeGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private TextureAtlas textureAtlas;
	private Maze maze;
	long lastStepTime;
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		maze = new Maze(20,20);
		//maze.generate();
		//maze.print();
	
		textureAtlas = new TextureAtlas("data/testMaze.txt");
		texture = new Texture(Gdx.files.internal("data/pink.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, maze.getWidth()*64, maze.getHeight()*64);
		batch = new SpriteBatch();
		
		
		


	}

	@Override
	public void dispose() {
		batch.dispose();
		textureAtlas.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(int i = 0; i < maze.getWidth(); i++){
			for(int j = 0; j < maze.getHeight(); j++){
				for(String dir : maze.getCarvedDirectionsByCellCoordinates(i, j))
					if(!dir.equals("")){
						batch.draw(textureAtlas.findRegion(dir), 64*i ,64*j);
					}
				if( maze.getCellVisited(i, j)&&!maze.getCellDoneCarving(i, j)){
					batch.draw(texture, 64*i ,64*j);
				}
			}
		
		
		}
		
		//batch.dra
		//batch.draw(regionSouth, 64, 0);
		//batch.draw(regionWest, 96, 0);
		batch.end();
		
		if(TimeUtils.nanoTime() - lastStepTime > 10000000) { 
			maze.step(); 
			lastStepTime = TimeUtils.nanoTime();
		}
		
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
