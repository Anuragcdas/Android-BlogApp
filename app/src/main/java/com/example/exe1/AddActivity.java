package com.example.exe1;

import static com.example.exe1.AppConstants.API_POST;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AddActivity extends AppCompatActivity {


    private EditText etTitle, etContent;
    private Button btnSubmmit;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);



         rootView = findViewById(R.id.main);




        Toolbar toolbar=findViewById(R.id.toolbarPost);

        AppConstants.OnCallBack(this,toolbar,"ADD NEW POST");


        // UI COMPONENTS
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        btnSubmmit = findViewById(R.id.btnSummit);

        btnSubmmit.setOnClickListener(V -> {


            showConfirmationDialog();
        });

        requestQueue = Volley.newRequestQueue(this);

        ////////////////////////////////////////////////
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void showConfirmationDialog() {

        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please Fill In All the Field", Toast.LENGTH_SHORT).show();
            return;
        } else {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confrm Post");
            builder.setMessage("Are you sure you want to submit this post ?");


            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    postingFunction();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });


            AlertDialog dialog = builder.create();
            dialog.show();

        }


    }

    private void postingFunction() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Posting");
        builder.setMessage("Please wait while your post is being added");
        builder.setCancelable(false);

        AlertDialog postDialog = builder.create();
        postDialog.show();

        addNewPost(postDialog);

    }


    private void addNewPost(AlertDialog postDialog) {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("title", etTitle.getText().toString().trim());
            jsonObject.put("body", etContent.getText().toString().trim());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, API_POST, jsonObject,
                response -> {

                    postDialog.dismiss();
                    Toast.makeText(AddActivity.this, "Post Added Successfully", Toast.LENGTH_LONG).show();

                    // Fragment Translation code

                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    intent.putExtra("navigateTo", "HomeFragment");
                    startActivity(intent);
                    finish();

                }, error -> {

            postDialog.dismiss();

            NetworkUtils.handleVolleyError(this,error,rootView,()->

            {


                    postingFunction();



            });

        }
        );

        requestQueue.add(jsonObjectRequest);


    }


}

