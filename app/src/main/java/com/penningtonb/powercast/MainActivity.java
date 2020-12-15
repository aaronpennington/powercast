package com.penningtonb.powercast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    TextView loginText;
    String uid;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = findViewById(R.id.main_container);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        email = intent.getStringExtra("USER_EMAIL");
        uid = intent.getStringExtra("USER_UID");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        PlayerControlFragment pcFragment = PlayerControlFragment.newInstance("null");
        //SearchFragment searchFragment = SearchFragment.newInstance(uid, "b");
        SubscriptionsFragment subsFragment = SubscriptionsFragment.newInstance(uid, "b");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.control_container, pcFragment);
        fragmentTransaction.add(R.id.search_container, subsFragment);
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener
            navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(
                @NonNull MenuItem item)
        {
            // By using switch we can easily get
            // the selected fragment
            // by using their id.
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.fragSubs:
                    selectedFragment = SubscriptionsFragment.newInstance(uid, "b");
                    break;
                case R.id.fragSearch:
                    selectedFragment = SearchFragment.newInstance(uid, "b");
                    break;
                case R.id.fragUser:
                    selectedFragment = UserFragment.newInstance(uid, email);
                    break;
            }
            // It will help to replace the one fragment to other.
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.search_container,
                            selectedFragment)
                    .commit();
            return true;
        }
    };

    public void switchContent(int id, Fragment fragment, boolean back_stack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment, fragment.toString());
        if (back_stack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}



