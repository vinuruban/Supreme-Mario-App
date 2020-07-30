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
		batch.draw(mario[marioState], (Gdx.graphics.getWidth() / 2) - (mario[0].getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (mario[0].getHeight() / 2)); //centers mario and loops through all 3 frames!

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
