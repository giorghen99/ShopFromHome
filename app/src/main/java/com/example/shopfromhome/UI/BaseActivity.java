package com.example.shopfromhome.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.shopfromhome.R;
import com.example.shopfromhome.Utils.SessionManager;
import com.example.shopfromhome.gestore.HomepageGestoreActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
    }

    protected void setupNavigation(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("  SHOPFROMHOME");
        }
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            handleDrawerNavigation(item);
            drawerLayout.closeDrawers();
            return true;
        });

        updateNavigationView(navigationView);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(this::handleBottomNavigation);
    }

    protected void updateNavigationView(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.userName);

        if (sessionManager.isLoggedIn()) {
            String email = sessionManager.getEmail();
            userNameTextView.setText(getString(R.string.welcome_message_logged_in, email));

            navigationView.getMenu().setGroupVisible(R.id.menu_logged_out, false);
            navigationView.getMenu().setGroupVisible(R.id.menu_logged_in, true);

            if ("MAGAZZINIERE".equalsIgnoreCase(sessionManager.getRole())) {
                navigationView.getMenu().findItem(R.id.nav_manage_site).setVisible(true);
            } else {
                navigationView.getMenu().findItem(R.id.nav_manage_site).setVisible(false);
            }
        } else {
            userNameTextView.setText(getString(R.string.welcome_message_guest));
            navigationView.getMenu().setGroupVisible(R.id.menu_logged_in, false);
            navigationView.getMenu().setGroupVisible(R.id.menu_logged_out, true);
            navigationView.getMenu().findItem(R.id.nav_manage_site).setVisible(false);
        }
    }

    protected boolean handleBottomNavigation(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_products) {
            startActivity(new Intent(this, ProductListActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (itemId == R.id.nav_cart) {
            startActivity(new Intent(this, CarrelloActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (itemId == R.id.nav_orders) {
            startActivity(new Intent(this, OrdersActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        return true;
    }

    protected void handleDrawerNavigation(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (itemId == R.id.nav_register) {
            startActivity(new Intent(this, RegisterActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (itemId == R.id.nav_logout) {
            sessionManager.logout();
            updateNavigationView(navigationView);
            invalidateOptionsMenu();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (itemId == R.id.nav_account) {
            startActivity(new Intent(this, AccountActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (itemId == R.id.nav_about_us) {
            startActivity(new Intent(this, AboutUsActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (itemId == R.id.nav_manage_site) {
            startActivity(new Intent(this, HomepageGestoreActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationView(navigationView);
    }
}