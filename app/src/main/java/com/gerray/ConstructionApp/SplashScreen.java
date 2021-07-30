package com.gerray.ConstructionApp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.gerray.ConstructionApp.Authentication.UserSelector;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class SplashScreen extends AppCompatActivity {
    private ViewPager mViewPager;
    private LinearLayout mLinearlayout;

    private SliderAdapter sliderAdapter;
    private TextView[] mDots;

    private Button mBackBtn, mNextBtn;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mViewPager = findViewById(R.id.viewPager);
//        mLinearlayout = findViewById(R.id.dots_layout);

        sliderAdapter = new SliderAdapter(this);
        mViewPager.setAdapter(sliderAdapter);
        addDotIndicator(0);

        mViewPager.addOnPageChangeListener(viewListener);

        mBackBtn = findViewById(R.id.back_btn);
        mNextBtn = findViewById(R.id.next_btn);
        mNextBtn.setOnClickListener(view -> mViewPager.setCurrentItem(mCurrentPage + 1));

        mBackBtn.setOnClickListener(view -> mViewPager.setCurrentItem(mCurrentPage - 1));
        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager(mViewPager);
    }

    public void addDotIndicator(int position) {
        mDots = new TextView[3];
//        mLinearlayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
//            mDots[i].setText(HtmlCompat.fromHtml("&#8226;",HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST).toString());
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.TransparentWhite));
//            mLinearlayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotIndicator(position);

            mCurrentPage = position;
            if (position == 0) {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("Next");
                mBackBtn.setText("");
            } else if (position == mDots.length - 1) {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("Finish");
                mNextBtn.setOnClickListener(view -> {
                    startActivity(new Intent(SplashScreen.this, UserSelector.class));
//                        startActivity(new Intent(SplashScreen.this, UserSelector.class));
                });
                mBackBtn.setText("Back");
            } else {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("Next");
                mBackBtn.setText("Back");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}