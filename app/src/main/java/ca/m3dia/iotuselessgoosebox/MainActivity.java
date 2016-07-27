package ca.m3dia.iotuselessgoosebox;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Common.authenticate();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Trouble connecting to the GooseBox. Try again.", Toast.LENGTH_SHORT);
        }

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
