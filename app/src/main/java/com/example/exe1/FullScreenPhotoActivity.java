package com.example.exe1;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class FullScreenPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_full_screen_photo);

        ImageView imageView = findViewById(R.id.imageViewFullScreen);
        String photourl = getIntent().getStringExtra("photo_url");

        Toolbar toolbar=findViewById(R.id.toolbarfull);

        AppConstants.OnCallBack(this,toolbar,"VIEW IMAGE");



        Glide.with(this)
                .load(photourl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.errorimage)
                .into(imageView);


        ///////////////////////////////////////////////////////
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}