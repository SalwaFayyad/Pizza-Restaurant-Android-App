package com.example.pizza_appproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pizza_appproject.databinding.ActivityAdminHomeNavBinding;
import com.google.android.material.navigation.NavigationView;

public class AdminHome_nav extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminHomeNavBinding binding;
    public static NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminHomeNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navigationView = binding.adminNavView;
        setSupportActionBar(binding.appBarAdminHomeNav.toolbar);

        DrawerLayout drawer = binding.drawerLayout;

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.usernameid);
        TextView navEmail = headerView.findViewById(R.id.emailid);
        navUsername.setText(User.currentUser.getString(1) + " " + User.currentUser.getString(2));
        navEmail.setText(User.currentUser.getString(0));
        navigationView.setItemIconTintList(null);
        ImageView profile = headerView.findViewById(R.id.profileid);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_vieworders, R.id.nav_AddAdmin, R.id.nav_AddSpecialOffer)
                .setOpenableLayout(drawer)
                .build();

        // Assign the profile image
        if (User.currentUser.getBlob(8) != null) {
            byte[] blobImage = User.currentUser.getBlob(8);
            Bitmap bitmap = BitmapFactory.decodeByteArray(blobImage, 0, blobImage.length);
            profile.setImageBitmap(bitmap);
        } else {
            String gender = User.currentUser.getString(4);
            Log.d("GenderCheck", "Gender at index 4: " + gender);

            if ("Male".equals(gender)) {
                profile.setImageResource(R.drawable.boy);
                Log.d("ImageChange", "Profile image set to boy");
            } else {
                profile.setImageResource(R.drawable.girl);
                Log.d("ImageChange", "Profile image set to girl");
            }
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin_home_nav);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Manually set the navigation item selected listener
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_logout) {
                Log.d("Logout", "Logout item clicked");

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminHome_nav.this);
                builder.setTitle("Confirm Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Sure", (dialog, which) -> {
                            Intent intent = new Intent(AdminHome_nav.this, LogIn_Activity.class);
                            startActivity(intent);
                            finish();
                        })
                        .create()
                        .show();
                drawer.closeDrawer(GravityCompat.START);
                return true; // Indicate that the event was handled
            }

            // For other items, use NavigationUI to handle navigation
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                drawer.closeDrawer(GravityCompat.START);
            }
            return handled;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_admin_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin_home_nav);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
