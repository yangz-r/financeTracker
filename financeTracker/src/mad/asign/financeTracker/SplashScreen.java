package mad.asign.financeTracker;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Color;



/**
 * This Class is to Display The Splash Screen When the Application is Launch
 * It will Display for 4.5 seconds and will be switch to the Home Screen.
 * 
 * @author RoboR
 *
 */
public class SplashScreen extends View{

	public SplashScreen(Context context){
		super(context);
		this.setBackgroundColor(Color.WHITE);
		
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setDuration(2000);

		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setStartOffset(2500);
		fadeOut.setDuration(2000);

		AnimationSet animation = new AnimationSet(true);
		animation.addAnimation(fadeOut);
		animation.addAnimation(fadeIn);
		
		this.setAnimation(animation);
		
	}
	
	@Override
	protected void onDraw(final Canvas canvas){		
		Bitmap launchPic = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
				
		setLogo(canvas, launchPic);		
	}
	


	/**
 	 * Set the logo "Finance Tracker" picture to the center of the screen.
	 * This screen height and width is take into consideration to suites all type
	 * of screen size. 
	 * 
	 * @param canvas the canvas to draw with
	 * @param pic the logo picture
	 */
	private void setLogo(Canvas canvas, Bitmap pic){
		int picWidth = pic.getWidth();
		int picHeight = pic.getHeight();
		double screenWidth = this.getWidth();
		double screenHeight = this.getHeight();
		double scale = 0;
		
		if(screenWidth <= screenHeight){
			//set to scale the picture by the width
			scale = screenWidth / picWidth * 0.8;	
		}else{
			//set to scale the picture by the height
			scale = screenHeight / picHeight * 0.8;
		}

		Bitmap scalePic = Bitmap.createScaledBitmap(pic, (int) (picWidth*scale), (int) (picHeight*scale), false);
		
		float leftPoint = (float) ((screenWidth - scalePic.getWidth() )/2);
		float topPoint = (float) ((screenHeight - scalePic.getHeight() )/2);

		canvas.drawBitmap(scalePic, leftPoint, topPoint, null);
	}
}
