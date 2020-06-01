package com.example.smartcrop;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tab;
    private ViewPager viewpager;
    private LinearLayout layout;
    GoogleSignInClient mGoogleSignInClient;
    private NetworkChangeReceiver receiver;
    public static String node = "user3";

    @Override
    protected void onResume() {
        super.onResume();
        loadLocale();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadLocale();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadLocale();
        layout = (LinearLayout) findViewById(R.id.lin);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("weblogin", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        node = pref.getString("node", null);
        if (node == null) {
            node = "user3";
        }
        GoogleSignInOptions gso = new
                GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
// Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        receiver = new NetworkChangeReceiver(layout);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, intentFilter);
        init();
    }


    public void loadLocale() {
        SharedPreferences pref = getSharedPreferences("lang", MODE_PRIVATE);
        String lang = pref.getString("lang", "");
        SetLocale(lang);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void SetLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("lang", MODE_PRIVATE).edit();
        editor.putString("lang", lang);
        editor.apply();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tab = (TabLayout) findViewById(R.id.tab);
        tab.addTab(tab.newTab().setText(R.string.home));
        tab.addTab(tab.newTab().setText(R.string.report));
        tab.addTab(tab.newTab().setText(R.string.logs));
        tab.setTabGravity(tab.GRAVITY_FILL);

        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                registerReceiver(receiver, intentFilter);
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), tab.getTabCount());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.settings) {

            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.logout) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            SharedPreferences pref = getApplicationContext().getSharedPreferences("weblogin", 0); // 0 - for private mode
            if (account != null) {
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            } else if (AccessToken.getCurrentAccessToken() != null) {
                AccessToken.setCurrentAccessToken(null);
                if (LoginManager.getInstance() != null) {
                    LoginManager.getInstance().logOut();
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else if (pref.getString("email", null) != null) {
                pref.edit().clear().apply();
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        }
        if (item.getItemId() == R.id.configure) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.4.1")));
        }
        if (item.getItemId() == R.id.help) {
            Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (viewpager.getCurrentItem()==0)
        {
            super.onBackPressed();
        }
        else if (viewpager.getCurrentItem()==1)
        {
            viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
        }
        else if (viewpager.getCurrentItem()==2){
            viewpager.setCurrentItem(viewpager.getCurrentItem() - 2);
        }
    }
}
