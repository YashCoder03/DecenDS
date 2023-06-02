package com.example.decends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.ImageButton;
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

public class onBoarding extends AppCompatActivity implements ViewPagerListener {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    LinearLayout dots;
    private FragmentManager fragmentManager;
    TextView[] dot;
    ImageButton next;

    Button yes;
    Button no;
    int current= 0;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    Animation animation;
    onBoardingPage3 onBoardingPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);


        next = findViewById(R.id.next);

        VPAdapter vpAdapter = new VPAdapter(this);

        View onBoardingPage1View = getLayoutInflater().inflate(R.layout.fragment_on_boarding_page1, null);
        onBoardingPage1 onBoardingPage1 = new onBoardingPage1();
        View onBoardingPage2View = getLayoutInflater().inflate(R.layout.fragment_on_boarding_page2, null);
        onBoardingPage2 onBoardingPage2 = new onBoardingPage2();
        View onBoardingPage3View = getLayoutInflater().inflate(R.layout.fragment_on_boarding_page3, null);
        onBoardingPage3 onBoardingPage3 = new onBoardingPage3();

        vpAdapter.addFragment(onBoardingPage1);
        vpAdapter.addFragment(onBoardingPage2);
        vpAdapter.addFragment(onBoardingPage3);

        Button no = onBoardingPage3View.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(onBoarding.this, "", Toast.LENGTH_SHORT).show();
            }
        });




        viewPager = findViewById(R.id.viewPager2);
        dots = findViewById(R.id.dots);


        SlideAdapter slideAdapter = new SlideAdapter(this);

        addDots(1);
        viewPager.setAdapter(vpAdapter);
        //viewPager.setInitialSavedState(savedState);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(current + 1);
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if(position ==  2)
                {
                    next.setVisibility(View.INVISIBLE);
                }
                else
                {
                    next.setVisibility(View.VISIBLE);
                }
                addDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
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
        current = position;
        //Toast.makeText(getApplicationContext(),"inside",Toast.LENGTH_SHORT).show();
        dots.removeAllViews();
        for(int i = 0 ; i < dot.length;i++)
        {
            dot[i] = new TextView(this);
            dot[i].setText(Html.fromHtml("&#8226"));
            dot[i].setTextSize(35);
            dots.addView(dot[i]);
        }

        dot[position].setTextColor(getResources().getColor(R.color.purple_700,getApplicationContext().getTheme()));

    }

    @Override
    public void no() {
        viewPager.setCurrentItem(1);
    }
}

