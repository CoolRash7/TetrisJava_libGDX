package com.coolrash.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Test extends ApplicationAdapter {

	//INIT
	public static final int SPRITE_SIZE = 16;
	public static final int BLOCK_SIZE = 32;
	public static final int X_FIELD = 10;
	public static final int Y_FIELD = 20;
	public static Random rand = new Random();
	enum SHAPE {O, I, S, Z, L, J, T}

	//PLAYER (TETRIS)
	static class Player {

		public int x = 400;
		public int y = Y_FIELD-1;
		public SHAPE shape = SHAPE.L;
		public static int coords[][] = new int[2][4];

		Player() {
			setCoords(shape);
		}

		// начальные значение фигурки определенного типа
		private void setCoords(SHAPE shape) {
			switch (shape) {
				case O:
					coords[0][0] = 0;
					coords[1][0] = 0;

					coords[0][1] = 0;
					coords[1][1] = -1;

					coords[0][2] = -1;
					coords[1][2] = 0;

					coords[0][3] = -1;
					coords[1][3] = -1;
					break;

				case I:
					coords[0][0] = 0;
					coords[1][0] = 0;

					coords[0][1] = 1;
					coords[1][1] = 0;

					coords[0][2] = -1;
					coords[1][2] = 0;

					coords[0][3] = -2;
					coords[1][3] = 0;
					break;

				case S:
					coords[0][0] = 0;
					coords[1][0] = 0;

					coords[0][1] = 1;
					coords[1][1] = 0;

					coords[0][2] = 0;
					coords[1][2] = -1;

					coords[0][3] = -1;
					coords[1][3] = -1;
					break;

				case Z:
					coords[0][0] = 0;
					coords[1][0] = 0;

					coords[0][1] = -1;
					coords[1][1] = 0;

					coords[0][2] = 0;
					coords[1][2] = -1;

					coords[0][3] = 1;
					coords[1][3] = -1;
					break;

				case L:
					coords[0][0] = 0;
					coords[1][0] = 0;

					coords[0][1] = 1;
					coords[1][1] = 0;

					coords[0][2] = -1;
					coords[1][2] = 0;

					coords[0][3] = -1;
					coords[1][3] = -1;
					break;

				case J:

					coords[0][0] = 0;
					coords[1][0] = 0;

					coords[0][1] = -1;
					coords[1][1] = 0;

					coords[0][2] = 1;
					coords[1][2] = 0;

					coords[0][3] = 1;
					coords[1][3] = -1;
					break;

				case T:

					coords[0][0] = 0;
					coords[1][0] = 0;

					coords[0][1] = 1;
					coords[1][1] = 0;

					coords[0][2] = -1;
					coords[1][2] = 0;

					coords[0][3] = 0;
					coords[1][3] = -1;
					break;
			}

		}

		public void rotateShape() {
			boolean changed = false;
			for (int i = 0; i < 4; i++) {
				changed = false;
				//0 is x, 1 is y. YOU REMEMBER FUCKING PEACE OF SHIT?
				//+ coords change - 4options

				if (coords[0][i] == 0 && coords[1][i] == 1) {
					coords[0][i] = 1;
					coords[1][i] = 0;
					changed = true;
				}

				if (coords[0][i] == 1 && coords[1][i] == 0 && !changed) {
					coords[0][i] = 0;
					coords[1][i] = -1;
					changed = true;
				}

				if (coords[0][i] == 0 && coords[1][i] == -1 && !changed) {
					coords[0][i] = -1;
					coords[1][i] = 0;
					changed = true;
				}

				if (coords[0][i] == -1 && coords[1][i] == 0 && !changed) {
					coords[0][i] = 0;
					coords[1][i] = 1;
					changed = true;
				}

				//X
				if (coords[0][i] == 1 && coords[1][i] == 1 && !changed) {
					coords[0][i] = 1;
					coords[1][i] = -1;
					changed = true;
				}

				if (coords[0][i] == 1 && coords[1][i] == -1 && !changed) {
					coords[0][i] = -1;
					coords[1][i] = -1;
					changed = true;
				}

				if (coords[0][i] == -1 && coords[1][i] == -1 && !changed) {
					coords[0][i] = -1;
					coords[1][i] = 1;
					changed = true;
				}

				if (coords[0][i] == -1 && coords[1][i] == 1 && !changed) {
					coords[0][i] = 1;
					coords[1][i] = 1;
					changed = true;
				}
			}
		}

		public SHAPE getRandomShape() {
			SHAPE result = SHAPE.O;
			int randInt = rand.nextInt(7);
			//enum SHAPE {O, I, S, Z, L, J, T}
			switch(randInt) {
				case 0:
					result = SHAPE.O;
					break;

				case 1:
					result = SHAPE.I;
					break;

				case 2:
					result = SHAPE.S;
					break;

				case 3:
					result = SHAPE.Z;
					break;

				case 4:
					result = SHAPE.L;
					break;

				case 5:
					result = SHAPE.J;
					break;

				case 6:
					result = SHAPE.T;
					break;
			}
			return result;
		}

		public void newShape() {
			this.x = 4;
			this.y = Y_FIELD-1;
			this.setCoords(player.getRandomShape());
		}
	}

	public static int gameField[][] = new int[X_FIELD][Y_FIELD];
	public static Player player = new Player();

	public static float deltaTime;
	private float tempTimerShape=0;

	private SpriteBatch batch;
	private Texture image;
	private TextureRegion region;
	private Sprite sprite;

	@Override
	public void create() {
		batch = new SpriteBatch();
		image = new Texture("tetris.png");
		region = new TextureRegion(image, 0, 0, BLOCK_SIZE, BLOCK_SIZE);
		sprite = new Sprite(image, 0, 0, BLOCK_SIZE, BLOCK_SIZE);
	}

	@Override
	public void render() {
		//deltaTime
		deltaTime = Gdx.graphics.getDeltaTime();

		update(deltaTime);
		input();
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(image, 0, 100);
		renderGameField(100, 100, batch);
		batch.end();
	}

	public void update(float deltaTime) {
		try {

			tempTimerShape += deltaTime;
			if (tempTimerShape > 0.5) {

				player.y--;
				tempTimerShape = 0;
			}

			//check bottom is NOT empty
			for (int i = 0 ; i < 4; i++) {
				if (player.y + player.coords[1][i] -1 <= 0) {
					for (int j =0 ;j< 4; j++)
						gameField[player.x + player.coords[0][j]] [player.y + player.coords[1][j]-1] = 1;
					player.newShape();
				}

				if (gameField[player.x + player.coords[0][i]] [player.y + player.coords[1][i]-1] != 0) {
					for (int j =0 ;j< 4; j++)
						gameField[player.x + player.coords[0][j]][player.y + player.coords[1][j]] = 1;
					player.newShape();
				}
			}

		} catch (Exception e) {
			if (player.x <= 0)
				player.x++;

			if (player.x >= X_FIELD-1)
				player.x--;

			if (player.y <= 0) {
				player.x = 4;
				player.y = Y_FIELD - 1;
				player.setCoords(player.getRandomShape());
			}

			System.out.println("ERROR ON UPDATE");

		}
	}

	public void input() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			player.rotateShape();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) &&
				player.x + player.coords[0][0] - 1 >= 0 &&
				player.x + player.coords[0][1] - 1 >= 0 &&
				player.x + player.coords[0][2] - 1 >= 0 &&
				player.x + player.coords[0][3] - 1 >= 0)
			player.x--;

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) &&
				player.x + player.coords[0][0] + 1 < X_FIELD &&
				player.x + player.coords[0][1] + 1 < X_FIELD &&
				player.x + player.coords[0][2] + 1 < X_FIELD &&
				player.x + player.coords[0][3] + 1 < X_FIELD)
			player.x++;

		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			player.y--;
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		image.dispose();
	}

	private void renderGameField(int x, int y, SpriteBatch batch) {
		for (int i = 0; i < X_FIELD; i++) {
			for (int j = 0; j < Y_FIELD; j++) {
				sprite.setRegion(gameField[i][j] * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
				batch.draw( sprite,x + (i * SPRITE_SIZE), y + (j * BLOCK_SIZE), BLOCK_SIZE, BLOCK_SIZE );
			}
		}
		//draw player
		for (int i = 0;  i < 4; i++) {
			sprite.setRegion(2 * SPRITE_SIZE, 0, BLOCK_SIZE, BLOCK_SIZE);

			batch.draw(sprite, x + ((player.coords[0][i] + player.x)* SPRITE_SIZE), y + ((player.coords[1][i] + player.y) * SPRITE_SIZE), BLOCK_SIZE, BLOCK_SIZE);
		}

	}

	public void log(String message) {
		System.out.println(message);
	}
}