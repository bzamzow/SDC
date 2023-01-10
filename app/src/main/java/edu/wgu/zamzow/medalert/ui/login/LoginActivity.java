package edu.wgu.zamzow.medalert.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import edu.wgu.zamzow.medalert.R;
import edu.wgu.zamzow.medalert.communicate.Accounts;
import edu.wgu.zamzow.medalert.utils.SharedPrefs;
import edu.wgu.zamzow.medalert.utils.Vars;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private boolean didLogin;
    private String userName;
    private String passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.editUserName);
        editPassword = findViewById(R.id.editPassword);
        Button btnCreate = findViewById(R.id.btnCreateAccount);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> {
            userName = editUsername.getText().toString();
            passWord = editPassword.getText().toString();

            RunLogin runLogin = new RunLogin();
            try {
                runLogin.execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        });

        btnCreate.setOnClickListener(view -> {
            Intent createUserActivity = new Intent(this,CreateUserActivity.class);
            startActivity(createUserActivity);
        });
    }

    private class RunLogin extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            Accounts accounts = new Accounts(getApplicationContext());
            try {
                didLogin = accounts.doLogin(userName,passWord);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return didLogin;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            System.out.println("Login result " + aBoolean);

            if (aBoolean) {
                SharedPrefs sharedPrefs = new SharedPrefs(getApplicationContext());
                sharedPrefs.setLoginStatus();
                sharedPrefs.setLoginName(Vars.user.getUserID());
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to login, please try again", Toast.LENGTH_LONG).show();
            }
        }
    }
}