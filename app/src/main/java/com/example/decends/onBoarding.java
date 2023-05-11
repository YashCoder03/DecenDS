package com.example.decends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decends.Fragments.onBoardingPage1;
import com.example.decends.Fragments.onBoardingPage2;
import com.example.decends.Fragments.onBoardingPage3;
import com.example.decends.utility.NetworkChangeListener;
import com.example.decends.utility.SlideAdapter;
import com.example.decends.utility.VPAdapter;
import com.google.android.material.tabs.TabLayout;

public class onBoarding extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    LinearLayout dots;
    TextView[] dot;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    Button lets;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);

        lets = findViewById(R.id.get_started_btn);
        viewPager = findViewById(R.id.viewPager2);
        dots = findViewById(R.id.dots);

        VPAdapter vpAdapter = new VPAdapter(this);
        vpAdapter.addFragment(new onBoardingPage1());
        vpAdapter.addFragment(new onBoardingPage2());
        vpAdapter.addFragment(new onBoardingPage3());


        SlideAdapter slideAdapter = new SlideAdapter(this);
        viewPager.setAdapter(vpAdapter);

        //addDots(0);

        /*lets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(onBoarding.this, "starting Seekbar", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(onBoarding.this,SeekBar.class));
                finish();
            }
        });
    }

    protected void onStart() {

        IntentFilter intentFilter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
        super.onStart();
    }
    protected void onStop(){
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    private void addDots(int position)
    {
        dot = new TextView[3];
        dots.removeAllViews();
        for(int i = 0 ; i < dot.length;i++)
        {
            dot[i] = new TextView(this);
            dot[i].setText(Html.fromHtml("&#8226"));
            dot[i].setTextSize(35);
            dots.addView(dot[i]);
        }
        if(dot.length > 0)
        {
            dot[position].setTextColor(getResources().getColor(R.color.black));
        }
        viewPager.addOnAttachStateChangeListener((View.OnAttachStateChangeListener) changeListener);
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);

            if(position == 0)
            {
                lets.setVisibility(View.INVISIBLE);
            }
            else if(position == 1)
            {
                animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_anim);
                lets.setAnimation(animation);
                lets.setVisibility(View.VISIBLE);
            }
            else if(position == 2)
            {
                lets.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };*/

    }

}