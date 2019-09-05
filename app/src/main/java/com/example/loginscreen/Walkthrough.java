package com.example.loginscreen;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Walkthrough extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private Button nextBtn;
    private int mCurrentPage;

    private TextView[] mDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        nextBtn = (Button) findViewById(R.id.nextBtn);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[5];
        mDotLayout.removeAllViews();
        for (int i = 0; i < 5; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.transparentwhite));

            mDotLayout.addView(mDots[i]);
        }
        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.transparentwhite));
        }
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage = i;

            if (i == 0) {
                nextBtn.setEnabled(true);
                nextBtn.setText("Skip");
                nextBtn.setVisibility(View.VISIBLE);
            } else if (i == mDots.length - 1) {
                nextBtn.setEnabled(true);
                nextBtn.setText("Finish");
            }  else {
                nextBtn.setEnabled(true);
                nextBtn.setText("Skip");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }


    };

    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.nextBtn:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }

//        nextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent1);
//            }
//        });

    }
}

