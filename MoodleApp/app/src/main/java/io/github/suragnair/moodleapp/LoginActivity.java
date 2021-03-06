package io.github.suragnair.moodleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
    }

    public void signInClicked (View view) {

        usernameEditText.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        View focusView = null;
        Boolean cancel = false;

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setHint("Username (Required)");
            focusView = usernameEditText;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setHint("Password (Required)");
            focusView = passwordEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            attemptLogin(username, password);
        }
    }

    private void attemptLogin (String username, String password){
        // Send login request to server
        Networking.getData(0, new String[]{username, password}, new Networking.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    if (response.getString("success").equals("true")) {
                        // Populate application with user details
                        ((MyApplication) getApplication()).setMyUser(new User(response.getString("user")));
                        // Close activity to resume MainActivity
                        finish();
                    } else {
                        // In case of Invalid credentials
                        Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("JSON Exception", e.getMessage());
                }
            }
        });
    }
}

