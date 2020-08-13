package com.suprememario.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class SupremeMario extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background; //bg img
	Texture[] mario; //array since multiple frames are added
	Texture dizzyMario; //dizzy mario - game over state

	Random random;

	int marioState = 0; //to loop through each frames of Mario
	int pause = 0; //to slow down animation speed since render() speed is fast
	float gravity = 0.8f;
	float velocity = 0;
	int marioX = 0; //x-position of mario
	int marioY = 0; //y-position of mario
	int marioWidth = 0; //size
	int marioHeight = 0; //size
	Rectangle marioRectangle; //rectangle that represents mario

	//Score
	int score = 0;
	BitmapFont scoreText;

	//Game state - if game is on / game is over
	int gameState = 0;

	//Full screen size
	int screenWidth = 0;
	int screenHeight = 0;

	//Coin
	Texture coin; //coin img
	ArrayList<Integer> coinXs = new ArrayList<Integer>(); //x-position of coin
	ArrayList<Integer> coinYs = new ArrayList<Integer>(); //y-position of coin
	int coinCount = 0; //for spacing
	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>(); //to make a rectangle at position of coin

	//Bomb
	Texture bomb; //bomb img
	ArrayList<Integer> bombXs = new ArrayList<Integer>(); //x-position of bomb
	ArrayList<Integer> bombYs = new ArrayList<Integer>(); //y-position of bomb
	int bombCount = 0; //for spacing
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>(); //to make a rectangle at position of bomb

	@Override
	public void create () { //create() method gets called onCreate
		batch = new SpriteBatch();

		//bg img
		background = new Texture("bg.png");

		//mario
		mario = new Texture[4]; //number of frames = 4
		mario[0] = new Texture("frame-1.png");
		mario[1] = new Texture("frame-2.png");
		mario[2] = new Texture("frame-3.png");
		mario[3] = new Texture("frame-4.png");

		//dizzy mario - game over state
		dizzyMario = new Texture("dizzy-1.png");

		//coin
		coin = new Texture("coin.png");

		//bomb
		bomb = new Texture("bomb.png");

		//full screen size
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		//mario size
		marioWidth = mario[0].getWidth();
		marioHeight = mario[0].getHeight();

		//center mario - x-position never changes since mario doesn't move left or right
		marioX = (screenWidth / 2) - (marioWidth / 2);

		//center mario initially - at game start, mario should drop from the center of the screen
		marioY = screenHeight / 2;

		random = new Random();

		//to display score
		scoreText = new BitmapFont();
		scoreText.setColor(Color.WHITE);
		scoreText.getData().setScale(10);
	}

	@Override
	public void render () { //render() method gets called over and over again!
		batch.begin();
		batch.draw(background, 0, 0, screenWidth, screenHeight); //draws bg image at full screen! Also x=0 & y=0 is at bottom-left corner of the screen!

		if (gameState == 1) { /** WHILE GAME IS ON **/
			//BOMB
			if (bombCount < 250) { //after every 250 render loop, add bomb!
				bombCount++;
			} else {
				bombCount = 0;
				makeBomb(); //this simply creates the coordinates of the bomb
			}

			bombRectangles.clear(); //clear array at each render

			for (int i=0; i < bombXs.size(); i++) { //this will create and move the bomb across from right to left
				batch.draw(bomb, bombXs.get(i), bombYs.get(i)); //draws the bomb at the right end of the screen
				bombXs.set(i, bombXs.get(i) - 8); //moves bomb to left by 8 (QUICKER THAN COIN) at every render count
				bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight())); //give the rectangle the coordinates and the size of the bomb
			}

			//COIN
			if (coinCount < 100) { //after every 100 render loop, add coin!
				coinCount++;
			} else {
				coinCount = 0;
				makeCoin(); //this simply creates the coordinates of the coin
			}

			coinRectangles.clear(); //clear array at each render

			for (int i=0; i < coinXs.size(); i++) { //this will create and move the coin across from right to left
				batch.draw(coin, coinXs.get(i), coinYs.get(i)); //draws the coin at the right end of the screen
				coinXs.set(i, coinXs.get(i) - 4); //moves coin to left by 4 at every render count
				coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight())); //give the rectangle the coordinates and the size of the coin
			}

			//to loop through each frames of Mario. For need for For/While loops since render() method loops for us!
			if (pause < 8) { //frame changes are every 8 render loops - this slows down animation!
				pause++;
			} else { // if pause = 8, then change frames!
				pause = 0;
				if (marioState < 3) {
					marioState++;
				} else {
					marioState = 0;
				}
			}

			//to make Mario jump by clicking mouse
			if (Gdx.input.justTouched()) {
				velocity = -25; //this negative will add to marioY value below!
			}

			velocity += gravity; //add 0.8f to velocity at every render() loop
			marioY -= velocity; //decrease y-position of Mario

			//to stop mario from sinking into the ground
			if (marioY <= 0) {
				marioY = 0;
			}

			//to stop mario from flying above the screen
			int maxHeight = screenHeight - marioHeight;
			if (marioY >= maxHeight) {
				marioY =  maxHeight;
			}

			marioRectangle = new Rectangle(marioX, marioY, marioWidth, marioHeight);

			//IF MARIO COLLIDES WITH A COIN
			for (int i=0; i < coinRectangles.size(); i++) {
				if (Intersector.overlaps(marioRectangle, coinRectangles.get(i))) {
					score++;

					//to remove coin once collided (at point of collision) + to stop us from colliding with the coin over and over again (when moving while colliding)
					coinRectangles.remove(i);
					coinXs.remove(i);
					coinYs.remove(i);

					break; //break at point of collision
				}
			}

			//IF MARIO COLLIDES WITH A BOMB
			for (int i=0; i < bombRectangles.size(); i++) {
				if (Intersector.overlaps(marioRectangle, bombRectangles.get(i))) {
					Gdx.app.log("Bomb", "Collided");
					gameState = 2;
				}
			}

		} else if (gameState == 0) { /** WAITING TO START **/
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else if (gameState == 2) { /** WHEN GAME IS OVER **/
			if (Gdx.input.justTouched()) { //on click, restart
				gameState = 1;
				marioY = screenHeight / 2;
				score = 0;
				velocity = 0;
				coinXs.clear();
				coinYs.clear();
				coinRectangles.clear();
				coinCount = 0;
				bombXs.clear();
				bombYs.clear();
				bombRectangles.clear();
				bombCount = 0;
			}
		}

		/** CODE BELOW NEEDS TO BE EXECUTED REGARDLESS OF THE GAME STATE **/

		if (gameState == 2) {
			batch.draw(dizzyMario, marioX, marioY); //centers mario and loops through all 3 frames!
		} else {
			batch.draw(mario[marioState], marioX, marioY); //centers mario and loops through all 3 frames!
		}

		if (gameState == 2) {
			scoreText.draw(batch, "Game over :(", 100, 200);
		} else {
			scoreText.draw(batch, "" + score, 100, 200);
		}

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public void makeCoin() {
		float randomNumber = random.nextFloat(); //between 0 and 1

		//random height between 0 and Gdx.graphics.getHeight()
		float height = randomNumber * screenHeight;

		coinXs.add(screenWidth); //coin is made at the right end of the screen
		coinYs.add((int) height); //coin is made at random height within the screen
	}

	public void makeBomb() {
		float randomNumber = random.nextFloat(); //between 0 and 1

		//random height between 0 and Gdx.graphics.getHeight()
		float height = randomNumber * screenHeight;

		bombXs.add(screenWidth); //bomb is made at the right end of the screen
		bombYs.add((int) height); //bomb is made at random height within the screen
	}

}
