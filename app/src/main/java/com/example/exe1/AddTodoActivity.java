package com.example.exe1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;


public class AddTodoActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextInputEditText userId, title;
    private AutoCompleteTextView autotext;
    private Button btnSumit;

    private View root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_todo);


        root=findViewById(R.id.main);

        Toolbar toolbar=findViewById(R.id.toolbartodo);

        AppConstants.OnCallBack(AddTodoActivity.this,toolbar,"ADD NEW TODO TASK");

        userId = findViewById(R.id.todouserId);
        title = findViewById(R.id.todotitle);
        autotext = findViewById(R.id.autoComplete);

        btnSumit = findViewById(R.id.btnSummit);


        requestQueue = Volley.newRequestQueue(this);


        String[] options = {"Yes", "No"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, options);

        autotext.setAdapter(adapter);
        autotext.setThreshold(1);


        autotext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                autotext.showDropDown();
            }
        });

        autotext.setOnClickListener(v -> autotext.showDropDown());


        btnSumit.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Confirm Submission")
                    .setMessage("Are you sure you want to add this To-Do?")
                    .setPositiveButton("Yes", (dialog, which) -> addTodo())
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });


        //////////////////////////
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void cleartext()
    {
        userId.setText("");
        title.setText("");
        autotext.setText("");
    }

    private void addTodo() {
       String url = AppConstants.API_TODO;


        String userIdSTR = userId.getText().toString().trim();
        String titlestr = title.getText().toString().trim();
        String autotextSTr = autotext.getText().toString().trim();

        if (userIdSTR.isEmpty() || titlestr.isEmpty() || autotextSTr.isEmpty()) {
            Toast.makeText(this, "All Fields are Required", Toast.LENGTH_SHORT).show();
            Log.e("API_ERROR", "Empty fields detected");
            return;
        }



        int userId;
        try {
            userId = Integer.parseInt(userIdSTR);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid User ID!", Toast.LENGTH_SHORT).show();
            Log.e("API_ERROR", "Invalid User ID: " + userIdSTR);
            return;
        }

        boolean completed;

        if(autotextSTr=="yes")
        {
             completed= true;
        }
        else
        {
             completed=false;
        }

        JSONObject todoData = new JSONObject();
        try {
            todoData.put("userId", userId);
            todoData.put("title", titlestr);
            todoData.put("completed", completed);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("API_ERROR", "JSON Exception: " + e.getMessage());
            return;
        }

        Log.d("API_CALL", "Sending request: " + todoData.toString());

         JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, todoData,
                response -> {




                    Log.d("API_RESPONSE", "Response: " + response.toString());
                    Toast.makeText(this, "New TODO Added Successfully!", Toast.LENGTH_LONG).show();

                    cleartext();

                },
                error -> {
                    NetworkUtils.handleVolleyError(this,error,root,()-> {


                                   addTodo();

                    });

                }
        );

        requestQueue.add(jsonObjectRequest);




    }




}