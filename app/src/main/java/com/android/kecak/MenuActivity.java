package com.android.kecak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.uncopt.android.widget.text.justify.JustifiedTextView;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    CardView cardKecak, cardKonfirmasi, cardHistory, cardLogout;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
        alert
                .setMessage("Apakah anda akan Logout ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        Pengunjung user = SharedPrefManager.getInstance(this).getUser();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kecak Uluwatu");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        JustifiedTextView txtNama = headerView.findViewById(R.id.txtNama);
        JustifiedTextView txtEmail = headerView.findViewById(R.id.txtEmail);

        txtNama.setText(user.getNama_pengunjung());
        txtEmail.setText(user.getEmail());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.logOut:
                        AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
                        alert
                                .setMessage("Apakah anda akan Logout ?")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                        return true;
                    default:
                        return true;
                }
            }
        });

        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        cardKecak = findViewById(R.id.cardKecak);
        cardKecak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kecak = new Intent(MenuActivity.this, KecakListActivity.class);
                startActivity(kecak);
                finish();
            }
        });

        cardKonfirmasi = findViewById(R.id.cardKonfirmasi);
        cardKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent konfirmasi = new Intent(MenuActivity.this, PemesananActivity.class);
                startActivity(konfirmasi);
                finish();
            }
        });

        cardHistory = findViewById(R.id.cardHistory);
        cardHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent history = new Intent(MenuActivity.this, TiketActivity.class);
//                startActivity(history);
//                finish();
            }
        });

        cardLogout = findViewById(R.id.cardLogout);
        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
                alert
                        .setMessage("Apakah anda akan Logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                SharedPrefManager.getInstance(getApplicationContext()).logout();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        Pengunjung user = SharedPrefManager.getInstance(this).getUser();

        switch (item.getItemId()) {
            case R.id.btnProfil:
                AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
                alert
                        .setTitle("Login")
                        .setMessage("Nama : "+user.getNama_pengunjung()+"\n"+
                                "Email : "+user.getEmail()+"\n"+"Alamat : "+user.getAlamat())
                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
