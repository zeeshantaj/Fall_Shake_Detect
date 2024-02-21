package com.example.shake_and_fall_check_app.DetailsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.shake_and_fall_check_app.R;

public class Details_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deails);

        boolean isTrue = getIntent().getBooleanExtra("isHistory",false);
        if (isTrue){
            setFragment(new History_Fragment());
        }else {
            setFragment(new Shake_Fragment());
        }

    }
    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.detailFrameLayout,fragment);
        transaction.commit();
    }
}