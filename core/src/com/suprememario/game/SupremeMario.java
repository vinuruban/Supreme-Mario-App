package com.suprememario.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SupremeMario extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background; //bg img
	Texture[] mario; //array since multiple frames are added
	int marioState = 0; //to loop through each frames of Mario
	int pause = 0; //to slow down animation speed since render() speed is fast
	float gravity = 0.8f;
	float velocity = 0;
	int marioY = 0; //y-position of mario
	
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

		//center mario
		marioY = Gdx.graphics.getHeight() / 2;
	}

	@Override
	public void render () { //render() method gets called over and over again!
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //draws bg image at full screen! Also x=0 & y=0 is at bottom-left corner of the screen!

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
		int maxHeight = Gdx.graphics.getHeight() - mario[0].getWidth();
		if (marioY >= maxHeight) {
			marioY =  maxHeight;
		}

		batch.draw(mario[marioState], (Gdx.graphics.getWidth() / 2) - (mario[0].getWidth() / 2), marioY); //centers mario and loops through all 3 frames!

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
