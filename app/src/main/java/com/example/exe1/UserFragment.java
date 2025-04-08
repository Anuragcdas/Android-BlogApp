package com.example.exe1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exe1.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {
    private FragmentUserBinding binding;
    private EditText etUserId, etEmail;
    private Button btnValidate;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);


        etUserId = view.findViewById(R.id.etUserId);
        etEmail = view.findViewById(R.id.etEmail);
        progressBar = view.findViewById(R.id.progressBar);
        btnValidate = view.findViewById(R.id.Validatebutton);


        btnValidate.setOnClickListener(
                v -> {

                    String userIdStr = etUserId.getText().toString().trim();
                   String  email=etEmail.getText().toString().trim();
                    if (userIdStr.isEmpty()) {
                        etUserId.setError("User ID Is required");
                        return;

                        // checks whether it is int
                    }
                    if (!userIdStr.matches("\\d+")) {
                        etUserId.setError("Please Enter a Valid UserID");
                        return;

                    }
                    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    {
                        etEmail.setError("Please enter a valid email address");
                        return;

                    }





                    int userId;
                    try {
                        userId = Integer.parseInt(userIdStr);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }


                    if (userId < 1 || userId > 10) {
                        etUserId.setError("Please enter a Valid userId");
                        return;


                    }


                    progressBar.setVisibility(View.VISIBLE);

                    etUserId.setEnabled(false);
                    etEmail.setEnabled(false);

                    btnValidate.setEnabled(false);


                    new Handler().postDelayed(() ->
                    {
                        progressBar.setVisibility(View.GONE);
                        btnValidate.setEnabled(true);
                        etEmail.setEnabled(true);
                        etUserId.setEnabled(true);

                        Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                        intent.putExtra("user_Id", userId);
                        startActivity(intent);

                    }, 2000);


                }
        );


        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}