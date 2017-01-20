package com.example.twistedpurpose.lab3;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Base fragment for movie application, template for child fragments
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    // Skeleton for creating a fragment
    protected abstract Fragment createFragment();

    // Fetch the layout id of the fragment
    protected int getLayoutId() {
        return R.layout.activity_single_fragment;
    }

    // On creation, do fragment container assignment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }
}