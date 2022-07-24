package com.example.v3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.v3.Fragment.addFragment;
import com.example.v3.Fragment.homeFragment;
import com.example.v3.Fragment.notificationFragment;
import com.example.v3.Fragment.profileFragment;
import com.example.v3.Fragment.searchFragment;
import com.example.v3.databinding.ActivityMainBinding;
import com.iammert.library.readablebottombar.ReadableBottomBar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,new homeFragment());
        transaction.commit();

        binding.readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {

                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

                switch (i){
                    case 0:
                        transaction.replace(R.id.container,new homeFragment());
                        break;
                    case 1:
                        transaction.replace(R.id.container,new notificationFragment());
                        break;
                    case 2:
                        transaction.replace(R.id.container,new addFragment());
                        break;
                    case 3:
                        transaction.replace(R.id.container,new searchFragment());
                        break;
                    case 4:
                        transaction.replace(R.id.container,new profileFragment());
                        break;
                }
                transaction.commit();

            }
        });
    }
}