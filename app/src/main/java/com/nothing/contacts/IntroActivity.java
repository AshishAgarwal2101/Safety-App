package com.nothing.contacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class IntroActivity extends FragmentActivity {

    ViewPager viewPager;
    RadioGroup g1;
    RadioButton b1, b2, b3, b4, b5;
    static int plus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        viewPager = (ViewPager) findViewById(R.id.pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new MyAdapter(fragmentManager));
        g1 = (RadioGroup) findViewById(R.id.group);
        b1 = (RadioButton) findViewById(R.id.radioButton1);
        b2 = (RadioButton) findViewById(R.id.radioButton2);
        b3 = (RadioButton) findViewById(R.id.radioButton3);
        b4 = (RadioButton) findViewById(R.id.radioButton4);
        b5 = (RadioButton) findViewById(R.id.radioButton5);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    b1.setChecked(true);
                }
                if (position == 1) {
                    b2.setChecked(true);
                }
                if (position == 2) {
                    b3.setChecked(true);
                }
                if (position == 3) {
                    b4.setChecked(true);
                }
                if (position == 4) {
                    b5.setChecked(true);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        load();
        if (plus == 1) {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void next(View view) {
        save();
        if (plus == 1) {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void load() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        plus = sharedPreferences.getInt("key", plus);
    }

    public void save() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("key", plus = 1);
        editor.commit();
    }


    public void now(View view) {
        boolean checked = ((RadioButton) view).isChecked();


        switch (view.getId()) {
            case R.id.radioButton1:
                if (checked) {
                    viewPager.setCurrentItem(0);
                    break;
                }
            case R.id.radioButton2:
                if (checked) {
                    viewPager.setCurrentItem(1);
                    break;
                }

            case R.id.radioButton3:
                if (checked) {
                    viewPager.setCurrentItem(2);
                    break;
                }

            case R.id.radioButton4:
                if (checked) {
                    viewPager.setCurrentItem(3);
                    break;
                }

            case R.id.radioButton5:
                if (checked) {
                    viewPager.setCurrentItem(4);
                    break;
                }
        }
    }
}

class MyAdapter extends FragmentStatePagerAdapter {

    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        if (i == 0) {
            fragment = new FragmentA();
        }
        if (i == 1) {
            fragment = new FragmentB();
        }
        if (i == 2) {
            fragment = new FragmentC();
        }
        if (i == 3) {
            fragment = new FragmentD();
        }
        if (i == 4) {
            fragment = new FragmentE();
        }
        return fragment;
    }


    @Override
    public int getCount() {
        return 5;
    }

}


