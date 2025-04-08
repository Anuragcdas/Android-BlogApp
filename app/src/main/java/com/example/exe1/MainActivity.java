package com.example.exe1;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navagation_bar);
        progressBar = findViewById(R.id.mainActprogress_bar);

        // Set default fragment when activity starts
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            progressBar.setVisibility(View.VISIBLE);

            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home)
                selectedFragment = new HomeFragment();
            else if (item.getItemId() == R.id.nav_user)
                selectedFragment = new UserFragment();
            else if (item.getItemId() == R.id.nav_album)
                selectedFragment = new AlbumFragment();
            else if (item.getItemId() == R.id.nav_todo)
                selectedFragment = new TodoFragment();

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            } else {;
                progressBar.setVisibility(View.GONE);
            }


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .runOnCommit(() -> progressBar.setVisibility(View.GONE)).commit();


            return true;
        });

        // Handle system window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .runOnCommit(() -> progressBar.setVisibility(View.GONE))
                .commit();
    }
}
