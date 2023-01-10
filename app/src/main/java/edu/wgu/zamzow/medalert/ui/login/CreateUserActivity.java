package edu.wgu.zamzow.medalert.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.atomic.AtomicBoolean;

import edu.wgu.zamzow.medalert.R;
import edu.wgu.zamzow.medalert.communicate.Accounts;
import edu.wgu.zamzow.medalert.objects.User;

public class CreateUserActivity extends AppCompatActivity {

    private EditText editUserName, editPass, editFirstName, editLastName, editEmail;
    private FloatingActionButton fabCreateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        editUserName = findViewById(R.id.editUsername);
        editPass = findViewById(R.id.editPass);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);

        fabCreateUser = findViewById(R.id.fabCreateUser);

        fabCreateUser.setOnClickListener(view -> {
            if (CreateUser().get()) {
                finish();
            } else {

            }
        });
    }

    private AtomicBoolean CreateUser() {
        User user = new User();
        user.setUserName(editUserName.getText().toString());
        user.setPassword(editPass.getText().toString());
        user.setFirstName(editFirstName.getText().toString());
        user.setLastName(editLastName.getText().toString());
        user.setEmail(editEmail.getText().toString());

        Accounts accounts = new Accounts(this);
        return accounts.CreateAccount(user);
    }
}