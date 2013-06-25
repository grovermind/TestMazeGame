package com.grovermind.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.grovermind.game.maze.Maze;
import com.grovermind.game.maze.Nav;

public class TestMazeGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture drawTexture;	
	private int xpos;
	private int ypos;
	private TextureAtlas textureAtlas;
	private Texture littleManImage;
	private Rectangle littleMan;
	private Maze maze;
	private Nav nav;
	long lastStepTime;
	boolean started = false;
	boolean finished = false;	
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		maze = new Maze(20,20);
		nav  = new Nav(maze);
	
		textureAtlas = new TextureAtlas("data/testMazeII.txt");		
		drawTexture = new Texture(Gdx.files.internal("data/pink.png"));
		littleManImage = new Texture(Gdx.files.internal("data/eye_32.png"));
		drawTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
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
				for(int k = 0; k < 4; k++) {
					for(int l = 0; l < 4; l++) {
						batch.draw(textureAtlas.findRegion("hall"), 64*i+ 16*k ,64*j+16*l);
					}
				}
				batch.draw(textureAtlas.findRegion("wall"), 64*i+ 16*0 ,64*j+16*0);
				batch.draw(textureAtlas.findRegion("wall"), 64*i+ 16*0 ,64*j+16*3);
				batch.draw(textureAtlas.findRegion("wall"), 64*i+ 16*3 ,64*j+16*0);
				batch.draw(textureAtlas.findRegion("wall"), 64*i+ 16*3 ,64*j+16*3);
				for(String dir : maze.getCarvedDirectionsByCellCoordinates(i, j)){
					if(!dir.equals("")){
						if(dir.equals("NORTH")){
							batch.draw(textureAtlas.findRegion("wall"), 64*i+ 16*1 ,64*j+16*3);
							batch.draw(textureAtlas.findRegion("wall"), 64*i+ 16*2 ,64*j+16*3);
						}
						else if (dir.equals("EAST")) {
							batch.draw(textureAtlas.findRegion("wall"), 64*i+ 16*3 ,64*j+16*1);
							batch.draw(textureAtlas.findRegion("wall"), 64*i+ 16*3 ,64*j+16*2);
						}
						else if (dir.equals("WEST")) {
							batch.draw(textureAtlas.findRegion("wall"), 64*i+ 16*0 ,64*j+16*1);
							batch.draw(textureAtlas.findRegion("wall"), 64*i+ 16*0 ,64*j+16*2);
						}
						else if (dir.equals("SOUTH")) {
							batch.draw(textureAtlas.findRegion("wall"), 64*i+ 16*1 ,64*j+16*0);
							batch.draw(textureAtlas.findRegion("wall"), 64*i+ 16*2 ,64*j+16*0);
						}
						//batch.draw(textureAtlas.findRegion(dir), 64*i ,64*j);						
					}
				}
				if( maze.getCellVisited(i, j)&&!maze.getCellDoneCarving(i, j)){

					batch.draw(drawTexture, 64*i ,64*j);
					
				}
			}		
		}
		batch.end();
				
		//		
			
	//	}

		if(TimeUtils.nanoTime() - lastStepTime > 1000000) { 
			maze.step(); 
			lastStepTime = TimeUtils.nanoTime();
		}
	
	// Start
		if( maze.isDoneGenerating() &&! started){
			littleMan = new Rectangle();
			littleMan.x = 0;
			littleMan.y = maze.getHeight() * 64 - 48;
			batch.begin();
			batch.draw(littleManImage, littleMan.x, littleMan.y);
			batch.end();		
			started = true;
		}
		
	//Control 
		if( maze.isDoneGenerating() && started){					
			batch.begin();
			batch.draw(littleManImage, littleMan.x, littleMan.y);
			batch.end();
			if(Gdx.input.isTouched()){	
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				float location[] = nav.getLocation(touchPos.x, touchPos.y); 
				littleMan.x = location[0];
				littleMan.y = location[1];					
			}
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (camera.position.x > 0)
                    camera.translate(-3, 0, 0);
	    }
	    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
	            if (camera.position.x < maze.getWidth()*64)
	                    camera.translate(3, 0, 0);
	    }
	    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
	            if (camera.position.y > 0)
	                    camera.translate(0, -3, 0);
	    }
	    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
	            if (camera.position.y < maze.getHeight()*64)
	                    camera.translate(0, 3, 0);
	    }
		if(Gdx.input.isKeyPressed(Keys.A))   camera.zoom += .01;
		if(Gdx.input.isKeyPressed(Keys.Q))   camera.zoom -= .01;
		camera.update();		
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
