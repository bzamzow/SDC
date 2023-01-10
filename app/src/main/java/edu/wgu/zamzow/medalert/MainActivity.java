package edu.wgu.zamzow.medalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.wgu.zamzow.medalert.databinding.ActivityMainBinding;
import edu.wgu.zamzow.medalert.ui.cabinet.CabinetFragment;
import edu.wgu.zamzow.medalert.ui.home.HomeFragment;
import edu.wgu.zamzow.medalert.ui.login.LoginActivity;
import edu.wgu.zamzow.medalert.ui.reports.ReportsFragment;
import edu.wgu.zamzow.medalert.utils.SharedPrefs;
import edu.wgu.zamzow.medalert.utils.Vars;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private final HomeFragment homeFragment = new HomeFragment();
    private final CabinetFragment cabinetFragment = new CabinetFragment();
    private final ReportsFragment reportsFragment = new ReportsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPrefs sharedPrefs = new SharedPrefs(this);
        boolean isLoggedIn = sharedPrefs.getLoginStatus();
        if (isLoggedIn) {
            Vars.userID = sharedPrefs.getLogin();
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
                R.id.navigation_home, R.id.navigation_cabinet, R.id.navigation_reports)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        navView.setOnItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SetupInterface();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, homeFragment).commit();
                return true;
            case R.id.navigation_cabinet:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, cabinetFragment).commit();
                return true;
            case R.id.navigation_reports:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, reportsFragment).commit();
                return true;
            default:
                return false;
        }
    }

}