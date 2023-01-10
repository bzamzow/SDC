package edu.wgu.zamzow.medalert;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.wgu.zamzow.medalert.databinding.ActivityMainBinding;
import edu.wgu.zamzow.medalert.ui.login.LoginActivity;
import edu.wgu.zamzow.medalert.utils.SharedPrefs;
import edu.wgu.zamzow.medalert.utils.Vars;

public class MainActivity extends AppCompatActivity {

    private edu.wgu.zamzow.medalert.databinding.ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPrefs sharedPrefs = new SharedPrefs(this);
        boolean isLoggedIn = sharedPrefs.getLoginStatus();
        if (isLoggedIn) {
            sharedPrefs.getLogin();
            SetupInterface();
        } else {
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivityForResult(loginActivity, Vars.LOGIN_ACTIVITY);
        }
    }

    private void SetupInterface() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}