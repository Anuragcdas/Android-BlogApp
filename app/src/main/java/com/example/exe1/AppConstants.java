package com.example.exe1;

import android.app.Activity;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class AppConstants {

    public static String BASE_URL = "https://jsonplaceholder.typicode.com/";

    public static String API_POST = BASE_URL + "posts/";
    public static String API_USER = BASE_URL + "users";
    public static String API_COMMENT = BASE_URL + "comments/";
    public static String API_AlBUM = BASE_URL + "albums/";
    public static String API_PHOTO = BASE_URL + "photos/";
    public static String API_TODO = BASE_URL + "todos/";

    public static void showToastMessage(Activity mActivity, String message, int lengthShort) {
        Toast.makeText(mActivity, message, lengthShort).show();

    }

    public static void OnCallBack(AppCompatActivity activity,Toolbar toolbar,String title)
    {

           if(toolbar!= null){

            activity.setSupportActionBar(toolbar);
          }

        if (activity.getSupportActionBar() != null){

            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            activity.getSupportActionBar().setTitle(title);

        }
        toolbar.setNavigationOnClickListener(v -> activity.getOnBackPressedDispatcher().onBackPressed());

    }



}


