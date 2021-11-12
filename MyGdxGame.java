package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	
	Random rnd = new Random();
	final static int WIDTH_FIELD = 10;
	final static int HEIGHT_FIELD = 25;
	final int DISTANCE = 100;
	
	int figureX[] = new int[4];
	int figureY[] = new int[4];
	int figureID = 0;
	int timer = 0;
	
	static SpriteBatch batch;
	Texture img;
	Texture imgTetris;
	BitmapFont font;
	
	public int x=5, y =24;
	public static int field[][]=new int[WIDTH_FIELD][HEIGHT_FIELD];
	 
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		img = new Texture("badlogic.jpg");
		imgTetris = new Texture("tetris.png");
		
		/*
		for (int i = 0; i < HEIGHT_FIELD; i++) {
			for (int j = 0; j < WIDTH_FIELD; j++) {
				field[j][i] = rnd.nextInt(7);
			}
		}
		*/
		figureID = 1;
		createFigure(1);
	}

	@Override
	public void render () {
		
		if (Gdx.input.isKeyPressed(Input.Keys.A) && canMove() && canLeftMove()) x--;
		if (Gdx.input.isKeyPressed(Input.Keys.D) && canMove() && canRightMove()) x++;
		if (Gdx.input.isKeyJustPressed(Input.Keys.F)) Rotate();
		if (Gdx.input.isKeyPressed(Input.Keys.W)) y++;
		if (Gdx.input.isKeyPressed(Input.Keys.S)) y--;
		
		/*
		if (timer > 5) {
			timer = 0;
			y--;
		} else
			timer++;
		*/
		
		ScreenUtils.clear(0, 0.5f, 0.5f, 1);
		batch.begin();
		font.draw(batch, "x: "+x + "; y = " + y, 10, 50);
		

	
		//batch.draw(imgTetris, 100, 100, 32, 0, 16, 16);

		for (int i = 0; i < HEIGHT_FIELD; i++) {
			for (int j = 0; j < WIDTH_FIELD; j++) {
				drawSquare(batch, j*16+220,i*16+40, field[j][i]);
			}
		}

		for (int i = 0; i < 4; i++) {
				drawSquare(batch, figureX[i]*16+220+(x*16), figureY[i]*16+40+(y*16), figureID+1);
		}
		
		batch.end();
		fixFigure();
		if (checkCollisionFall()) { writeFigure(); y = 24; figureID = rnd.nextInt(7); createFigure(figureID);}
		if (checkCollisionFigure()) {writeFigure(); y = 24; figureID = rnd.nextInt(7); createFigure(figureID);}
		clearLines();

	}
	
	void drawSquare(SpriteBatch batch, int x, int y, int color) {
		batch.draw(imgTetris, x, y, color*16, 0, 16, 16);
	}
	
	void Rotate() {
		int centerX, centerY;
		centerX = figureX[0];
		centerY = figureY[0];
		for (int i = 0; i < 4; i++) {
			int x = figureY[i] - centerY;
			int y = figureX[i] - centerX;
			figureX[i] = centerX - x;
			figureY[i] = centerY + y;
		}
	}
	
	void clearLines() {
		int combo = 0;
		int count = 0;
		int what = 0;
		int oldField[][] = new int[WIDTH_FIELD][HEIGHT_FIELD];
		
		for (int i = 0; i< HEIGHT_FIELD; i++) {
			for (int j = 0; j < WIDTH_FIELD; j++) {
				oldField[j][i] = field[j][i];
			}
		}
		
		for (int i = 0; i < HEIGHT_FIELD; i++) {
			count = 0;
			for (int j = 0; j < WIDTH_FIELD; j++) {
				if (field[j][i] != 0) {count++; combo++; what = i;}
			}
			
			//if full line
			if (count == WIDTH_FIELD) {
		
				for (int j = 0; j < WIDTH_FIELD; j++) {
					field[j][what] = 0;
				}
				
				for (int q = what; q < HEIGHT_FIELD; q++) {
					for (int j = 0; j < WIDTH_FIELD; j++) {
						field[j][q] = oldField[j][clamp(q+1, 0, HEIGHT_FIELD-1)];
					}
				}
			}
		}
		

	}
	
	boolean checkCollisionFall() {
		boolean fuck = false;
		for (int i = 0; i < 4; i++) {
			if (figureY[i] + y == 0) fuck=true;
		}

		//writeFigure();
		return fuck;
	}
	
	boolean checkCollisionFigure() {
		boolean fuck = false;
		for (int i = 0; i < 4; i++) {
			if (field[clamp(figureX[i]+x, 0, WIDTH_FIELD-1)][clamp(figureY[i]+y-1, 0, HEIGHT_FIELD-1)] != 0) 
				fuck = true;
		}
		return fuck;
	}
	
	void writeFigure() {
		for (int i = 0; i < 4; i++) {
			field[clamp(figureX[i]+x, 0, WIDTH_FIELD-1)][clamp(figureY[i]+y, 0, HEIGHT_FIELD-1)] = figureID+1;
		}
	}
	
	void writeFigure1() {
		for (int i = 0; i < 4; i++) {
			field[clamp(figureX[i]+x, 0, WIDTH_FIELD-1)][clamp(figureY[i]+y+1, 0, HEIGHT_FIELD-1)] = 4;
		}
	}
	
	boolean canMove() {
		boolean fuck=true;
		for (int i = 0; i < 4; i++) {
			if (x + figureX[i] < 0 || x + figureX[i] > WIDTH_FIELD-1)
				fuck = false;
			else
				fuck= true;
		}
		return fuck;
	}
	
	boolean canLeftMove() {
		boolean fuck = true;
		for (int i = 0; i < 4; i++) {
			if (field[clamp(figureX[i]+x-1, 0, WIDTH_FIELD-1)][clamp(figureY[i]+y, 0, HEIGHT_FIELD-1)] != 0)
				fuck = false;
		}
		return fuck;
	}
	
	boolean canRightMove() {
		boolean fuck = true;
		for (int i = 0; i < 4; i++) {
			if (field[clamp(figureX[i]+x+1, 0, WIDTH_FIELD-1)][clamp(figureY[i]+y, 0, HEIGHT_FIELD-1)] != 0)
				fuck = false;
		}
		return fuck;
	}
	
	void fixFigure() {
		boolean left = false;
		boolean right = false;
		for (int i = 0; i < 4; i++)
			if (x + figureX[i] < 0)
				left = true;
					
		
		for (int i = 0; i < 4; i++)
			if (x + figureX[i] > WIDTH_FIELD-1)
				right = true;
		
		if (left) x++;
		if (right) x--;
		
	//	x=clamp(x, -3, WIDTH_FIELD);
	}
	
	void createFigure(int id) {
		switch (id) {
		
		case 0: 
			//O
			figureX[0] = 0;
			figureY[0] = 0;
			
			figureX[1] = 0;
			figureY[1] = 1;
			
			figureX[2] = 1;
			figureY[2] = 1;
			
			figureX[3] = 1;
			figureY[3] = 0;
			break;
			
		case 1: 
			//T
			figureX[0] = 0;
			figureY[0] = 0;
			
			figureX[1] = -1;
			figureY[1] = -1;
			
			figureX[2] = 0;
			figureY[2] = -1;
			
			figureX[3] = 1;
			figureY[3] = -1;
			break;
			
		case 2: 
			//S
			figureX[0] = 0;
			figureY[0] = 0;
			
			figureX[1] = 1;
			figureY[1] = 0;
			
			figureX[2] = 1;
			figureY[2] = -1;
			
			figureX[3] = 0;
			figureY[3] = 1;
			break;
			
		case 3: 
			//Z
			figureX[0] = 1;
			figureY[0] = 0;
			
			figureX[1] = 0;
			figureY[1] = 0;
			
			figureX[2] = 1;
			figureY[2] = 1;
			
			figureX[3] = 0;
			figureY[3] = -1;
			break;
			
		case 4: 
			//L
			
			figureX[0] = 1;
			figureY[0] = 0;
			
			figureX[1] = 0;
			figureY[1] = 0;
			
			figureX[2] = 2;
			figureY[2] = 0;
			
			figureX[3] = 2;
			figureY[3] = 1;
			break;
			
		case 5: 
			//L (mirror)
			
			figureX[0] = 1;
			figureY[0] = 0;
			
			figureX[1] = 0;
			figureY[1] = 0;
			
			figureX[2] = 2;
			figureY[2] = 0;
			
			figureX[3] = 0;
			figureY[3] = 1;
			break;
			
		case 6: 
			//I
			
			figureX[0] = 0;
			figureY[0] = 1;
			
			figureX[1] = 0;
			figureY[1] = 0;
			
			figureX[2] = 0;
			figureY[2] = 2;
			
			figureX[3] = 0;
			figureY[3] = 3;
			break;
		}
	}
	
	public static int clamp (int var, int min, int max) {
		if (var >= max)
			return max;
		else if (var <= min)
			return min;
		else
			return var;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
