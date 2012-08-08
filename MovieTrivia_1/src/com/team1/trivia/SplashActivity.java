package com.team1.trivia;


import com.team1.trivia.R;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends TriviaActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     // Create a background gradient
        GradientDrawable grad = new GradientDrawable(Orientation.TOP_BOTTOM,
                        new int[] { Color.WHITE, Color.BLUE });
        this.getWindow().setBackgroundDrawable(grad);

        setContentView(R.layout.splash);
        startAnimating();
    }

    /**
     * Helper method to start the animation on the splash screen
     */
    private void startAnimating() {
        // Fade in top title
        TextView logo1 = (TextView) findViewById(R.id.splashTitle1);
        Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo1.startAnimation(fade1);
        
        // Spin in the logo
        ImageView image = (ImageView) findViewById(R.id.logoImageView);
        Animation spin = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        image.startAnimation(spin);
        
        // Fade in bottom title after a built-in delay.
        TextView logo2 = (TextView) findViewById(R.id.splashTitle2);
        Animation fade2 = AnimationUtils.loadAnimation(this, R.anim.fade_in2);
        logo2.startAnimation(fade2);
        
        // Transition to Main Menu when bottom title finishes animating
        fade2.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
        
            	// The animation has ended, transition to the Main Menu screen
                startActivity(new Intent(
                		SplashActivity.this, 
                		OpeningActivity.class));
                SplashActivity.this.finish();
            }

            // Required method from Abstract
            public void onAnimationRepeat(Animation animation) {
            }
            
            // Required method from Abstract
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    @Override
    protected void onPause() { // Called when phone is switched to another app (i.e. phone call)
        super.onPause();
        
        // Stop the animation
        TextView logo1 = (TextView) findViewById(R.id.splashTitle1);
        logo1.clearAnimation();
        TextView logo2 = (TextView) findViewById(R.id.splashTitle2);
        logo2.clearAnimation();
        
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Start animating at the beginning so we get the full splash screen experience
        startAnimating();
    }
}
