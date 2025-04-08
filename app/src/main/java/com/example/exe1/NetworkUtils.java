package com.example.exe1;



import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class NetworkUtils {

    /**
     * Checks if the given Throwable is a network-related error (for Retrofit).
     * @param throwable Exception received in Retrofit onFailure
     * @return True if it's a network error, otherwise false
     */
    public static boolean isNetworkError(Throwable throwable) {
        return throwable instanceof IOException || throwable instanceof UnknownHostException || throwable instanceof SocketTimeoutException;
    }

    /**
     * Displays an error message using a Toast.
     * @param context Application context
     * @param message Error message to display
     */
    public static void showErrorToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a Snackbar with a retry action for network errors.
     * @param rootView The view to attach Snackbar to (usually root layout of the activity/fragment)
     * @param message The message to display
     * @param retryCallback The callback to execute when retry is clicked
     */
    public static void showRetrySnackbar(View rootView, String message, Runnable retryCallback) {
        if (rootView != null) {
            Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", v -> retryCallback.run()) // Retry callback executes provided function
                    .show();
        }
    }

    /**
     * Handles Retrofit API errors and displays appropriate messages.
     * @param context Application context
     * @param throwable Retrofit failure exception
     * @param rootView View for showing Snackbar if needed
     * @param retryCallback Retry function to execute if needed
     */
    public static void handleRetrofitError(Context context, Throwable throwable, View rootView, Runnable retryCallback) {
        String errorMessage = "Something went wrong. Please try again.";

        if (isNetworkError(throwable)) {
            errorMessage = "No internet connection. Please check your network.";
        } else if (throwable instanceof HttpException) {
            int errorCode = ((HttpException) throwable).response().code();
            errorMessage = "Server error: " + errorCode;
        }

        // Show Snackbar for retry option
        showRetrySnackbar(rootView, errorMessage, retryCallback);
    }

    /**
     * Handles common Volley errors and displays appropriate messages.
     * @param context Application context
     * @param error VolleyError received in onErrorResponse
     * @param rootView View for showing Snackbar if needed
     * @param retryCallback Retry function to execute if needed
     */
    public static void handleVolleyError(Context context, VolleyError error, View rootView, Runnable retryCallback) {
        String errorMessage = "Something went wrong. Please try again.";

        if (error instanceof NoConnectionError) {
            errorMessage = "No internet connection. Please check your network.";
        } else if (error instanceof TimeoutError) {
            errorMessage = "Request timed out. Please try again later.";
        }

        // Show Snackbar for retry option
        showRetrySnackbar(rootView, errorMessage, retryCallback);
    }
}
