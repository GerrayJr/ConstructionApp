package com.gerray.ConstructionApp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gerray.ConstructionApp.Authentication.LoginActivity;

public class splash extends AppCompatActivity {

    //Variables
    ImageView backgroundImage;
    TextView nextGen;

    //Animations
    Animation sideAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //hooks
        backgroundImage=findViewById(R.id.background_image);
        nextGen=findViewById(R.id.next_Gen);

        //Animations
        sideAnim= AnimationUtils.loadAnimation(this,R.anim.side_anim);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        //set Animations on elements
        backgroundImage.setAnimation(sideAnim);
        nextGen.setAnimation(bottomAnim);

        int SPLASH_TIMER = 5000;
        new Handler().postDelayed(() -> {
            Intent intent= new Intent(splash.this, LoginActivity.class);
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View,String>(backgroundImage , "logo_name");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(splash.this,pairs);
            startActivity(intent,options.toBundle());

        }, SPLASH_TIMER);



    }
}