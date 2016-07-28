package ca.m3dia.iotuselessgoosebox;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //login to Particle cloud
        Common.authenticate(this);

        //setup fragments
        ViewPagerFragment savedFragment = (ViewPagerFragment) getSupportFragmentManager().findFragmentById(R.id.placeholder);
        if (savedFragment == null) {
            ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.placeholder, viewPagerFragment);
            fragmentTransaction.commit();
        }
    }
}
