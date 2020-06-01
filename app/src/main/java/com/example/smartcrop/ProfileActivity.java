package com.example.smartcrop;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {

    private ImageView img_user;
    private TextView Tv1, Tv2, Tv3, Tv4;
    private Toolbar toolbar;
    private Button change;



    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loadLocale();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        change = (Button)findViewById(R.id.change);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        img_user = (ImageView) findViewById(R.id.img_user);
        Tv1 = (TextView) findViewById(R.id.tv_1);
        Tv2 = (TextView) findViewById(R.id.tv_2);
        Tv3 = (TextView) findViewById(R.id.tv_3);
        Tv4 = (TextView) findViewById(R.id.tv_4);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (AccessToken.getCurrentAccessToken() != null) {

            Tv1.setText(pref.getString("first_name", null));
            Tv2.setText(pref.getString("email", null));
            Tv3.setText("");
            Tv4.setText("");
            Glide.with(this).load(pref.getString("img_url", null)).into(img_user);
        }
        else if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Tv1.setText(personName);
            Tv2.setText(personEmail);
            Tv3.setText("");
            Tv4.setText("");
            Glide.with(this).load(personPhoto).into(img_user);
        }
        else {
            SharedPreferences pref1 = getApplicationContext().getSharedPreferences("weblogin", 0); // 0 - for private mode
            SharedPreferences.Editor editor1 = pref.edit();
            img_user.setVisibility(View.INVISIBLE);
            Tv1.setText(pref1.getString("name", null));
            Tv2.setText(pref1.getString("email", null));
            Tv3.setText(pref1.getString("phone", null));
            Tv4.setText(pref1.getString("pincode", null));
        }
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showchangeLanguageDialog();
            }
        });
    }

    private void showchangeLanguageDialog() {
        final String listitems[] = {"English","हिंदी","ગુજરાતી"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
        dialog.setTitle("Choose Language...");
        dialog.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialoglocale, int i) {
                if (i==0)
                {
                    SetLocale("en");
                    onRestart();
                }
                else if (i==1)
                {
                    SetLocale("hi");
                    onRestart();
                }
                else if (i==2)
                {
                    SetLocale("gu");
                    onRestart();
                }
                dialoglocale.dismiss();
            }
        });
        AlertDialog dialog1 = dialog.create();
        dialog1.show();
    }

    private void SetLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("lang",MODE_PRIVATE).edit();
        editor.putString("lang",lang);
        editor.apply();
    }

    public void loadLocale()
    {
        SharedPreferences pref = getSharedPreferences("lang",MODE_PRIVATE);
        String lang = pref.getString("lang","");
        SetLocale(lang);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
