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

public class onBoarding extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    LinearLayout dots;
    TextView[] dot;
    ImageButton next;

    Button yes;
    Button no;
    int current= 0;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);
        View inflatedView = getLayoutInflater().inflate(R.layout.fragment_on_boarding_page3, null,true);
        next = findViewById(R.id.next);

        yes = inflatedView.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(onBoarding.this, PeerId.class);
                startActivity(intent);
                finish();
            }
        });

        no = inflatedView.findViewById(R.id.no);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        viewPager = findViewById(R.id.viewPager2);
        dots = findViewById(R.id.dots);

        VPAdapter vpAdapter = new VPAdapter(this);
        vpAdapter.addFragment(new onBoardingPage1());
        vpAdapter.addFragment(new onBoardingPage2());
        vpAdapter.addFragment(new onBoardingPage3());

        //Toast.makeText(this,"inside",Toast.LENGTH_SHORT).show();

        SlideAdapter slideAdapter = new SlideAdapter(this);

        addDots(1);
        viewPager.setAdapter(vpAdapter);

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

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            addDots(position);
        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            Toast.makeText(getApplicationContext(),"outside",Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}

