package com.example.smartcrop;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInButton SignIn;
    public static int RC_SIGN_IN = 0;
    private TextView dontHaveAccount;
    private FrameLayout frame;
    private LoginButton btnFacebook;
    private CallbackManager callback;
    private EditText userEmail;
    private String regex = "[a-z0-9]+([-+._][a-z0-9]+){0,2}@.*?(\\.(a(?:[cdefgilmnoqrstuwxz]|ero|(?:rp|si)a)|b(?:[abdefghijmnorstvwyz]iz)|c(?:[acdfghiklmnoruvxyz]|at|o(?:m|op))|d[ejkmoz]|e(?:[ceghrstu]|du)|f[ijkmor]|g(?:[abdefghilmnpqrstuwy]|ov)|h[kmnrtu]|i(?:[delmnoqrst]|n(?:fo|t))|j(?:[emop]|obs)|k[eghimnprwyz]|l[abcikrstuvy]|m(?:[acdeghklmnopqrstuvwxyz]|il|obi|useum)|n(?:[acefgilopruz]|ame|et)|o(?:m|rg)|p(?:[aefghklmnrstwy]|ro)|qa|r[eosuw]|s[abcdeghijklmnortuvyz]|t(?:[cdfghjklmnoprtvwz]|(?:rav)?el)|u[agkmsyz]|v[aceginu]|w[fs]|y[etu]|z[amw])\\b){1,2}";
    private EditText userPwd;
    private Button btnLogin;
    private JsonPlaceHolder apiInterface;
    private TextView wrongPass;
    private NetworkChangeReceiver receiver;
    private ConstraintLayout layout;


    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        userEmail = (EditText) view.findViewById(R.id.edt_signin_email);
        loadLocale();
        userPwd = (EditText) view.findViewById(R.id.edt_signin_password);
        btnLogin = (Button) view.findViewById(R.id.btn_signin);
        btnLogin.setOnClickListener(this::onClick);
        SignIn = (GoogleSignInButton) view.findViewById(R.id.sign_in_button);
        SignIn.setOnClickListener(this::onClick);
        layout = (ConstraintLayout)view.findViewById(R.id.login);
        wrongPass = (TextView) view.findViewById(R.id.email_password);
        apiInterface = APIClient.getClient().create(JsonPlaceHolder.class);
        //facebook
        callback = CallbackManager.Factory.create();
        btnFacebook = (LoginButton) view.findViewById(R.id.login_facebook);
        btnFacebook.setFragment(this);
        btnFacebook.setReadPermissions(Arrays.asList("email", "public_profile"));
        dontHaveAccount = view.findViewById(R.id.tv_signup);
        frame = (FrameLayout) getActivity().findViewById(R.id.frame);
        return view;
    }

    private void loadUser(JSONObject object) {
        String first_name, gender, email, img_url, id, birthday;
        try {
            first_name = object.getString("name");
            email = object.getString("email");
            id = object.getString("id");
            img_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

            SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("first_name", first_name);
            editor.putString("email", email);
            editor.putString("img_url", img_url);
            editor.commit();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignupFragment());
            }
        });

        userEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        userPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private void checkInputs() {
        Drawable custom = getResources().getDrawable(R.drawable.error);
        custom.setBounds(0, 0, custom.getIntrinsicWidth(), custom.getIntrinsicHeight());
        if (!TextUtils.isEmpty(userEmail.getText().toString())) {
            userEmail.setError(null);
            if (!TextUtils.isEmpty(userPwd.getText().toString()) && userPwd.getText().toString().length() >= 8 && userPwd.getText().toString().length() < 20) {
                userPwd.setError(null);
                btnLogin.setEnabled(true);
                btnLogin.setBackgroundColor(getResources().getColor(R.color.colorblue));
                btnLogin.setTextColor(getResources().getColor(R.color.colorwhite_enable));
            } else {
                userPwd.setError(getString(R.string.pwderror), custom);
                btnLogin.setEnabled(false);
                btnLogin.setBackgroundColor(getResources().getColor(R.color.colorlightblue));
                btnLogin.setTextColor(getResources().getColor(R.color.colorwhite_disable));
            }
        } else {
            userEmail.setError(getString(R.string.emailerror), custom);
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundColor(getResources().getColor(R.color.colorlightblue));
            btnLogin.setTextColor(getResources().getColor(R.color.colorwhite_disable));
        }
    }


    private void google() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        signIn();
    }

    private  void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_right, R.anim.slideout_left);
        fragmentTransaction.replace(frame.getId(), fragment);
        fragmentTransaction.addToBackStack("login");
        fragmentTransaction.commit();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        callback.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (mGoogleSignInClient != null) {
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            updateUI(account);
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);

        } catch (ApiException e) {
            Toast.makeText(getActivity(), "Please try again!!!", Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    @Override
    public void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (updateUI(account)) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }
        if (AccessToken.getCurrentAccessToken() != null) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }
        super.onStart();
    }

    private Boolean updateUI(GoogleSignInAccount account) {
        if (account != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sign_in_button:
                receiver = new NetworkChangeReceiver(layout);
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                getActivity().registerReceiver(receiver,intentFilter);
                if (Utils.isconnected(getContext()))
                {
                    google();
                }
                break;
            case R.id.btn_signin:
                receiver = new NetworkChangeReceiver(layout);
                IntentFilter intentFilter1 = new IntentFilter();
                intentFilter1.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                getActivity().registerReceiver(receiver,intentFilter1);
                if (Utils.isconnected(getContext()))
                {
                    getdata();
                }
                break;
            case R.id.login_facebook:
                receiver = new NetworkChangeReceiver(layout);
                IntentFilter intentFilter2 = new IntentFilter();
                intentFilter2.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                getActivity().registerReceiver(receiver,intentFilter2);
                if (Utils.isconnected(getContext()))
                {
                    facebook();
                }
                break;
        }

    }

    private void facebook() {
        LoginManager.getInstance().registerCallback(callback,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                loadUser(object);
                            }
                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });

    }

    private void getdata() {
        Drawable custom = getResources().getDrawable(R.drawable.error);
        custom.setBounds(0, 0, custom.getIntrinsicWidth(), custom.getIntrinsicHeight());
        if (userEmail.getText().toString().toLowerCase().matches(regex)) {
            String email = userEmail.getText().toString();
            String pwd = userPwd.getText().toString();
            postreq(email, pwd);
        } else {
            userEmail.setError(getString(R.string.emailerror), custom);
        }

    }

    private void postreq(String email, String pwd) {
        Call<UserBean> call = apiInterface.createUser(email, pwd);
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        call.enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                if (!response.isSuccessful()) {
                    dialog.dismiss();
                    wrongPass.setVisibility(View.VISIBLE);
                    wrongPass.setText(getString(R.string.loginerror));
                } else {
                    dialog.dismiss();
                    wrongPass.setVisibility(View.GONE);
                    if (response.body() != null) {
                        UserBean model = response.body();
                        SharedPreferences pref = getContext().getSharedPreferences("weblogin", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("name", model.getUserName());
                        editor.putString("email", model.getUserEmail());
                        editor.putString("pincode", model.getUserPincode());
                        editor.putString("phone", model.getUserPhone());
                        editor.putString("node", model.getUserNode());
                        editor.commit();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        wrongPass.setVisibility(View.VISIBLE);
                        wrongPass.setText(getString(R.string.loginerror));
                    }
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                dialog.dismiss();
                wrongPass.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Something went to wrong !!", Toast.LENGTH_SHORT).show();
                wrongPass.setText(getString(R.string.loginerror));
            }
        });
    }
    private void SetLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration,getContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("lang",MODE_PRIVATE).edit();
        editor.putString("lang",lang);
        editor.apply();
    }

    public void loadLocale()
    {
        SharedPreferences pref = getActivity().getSharedPreferences("lang",MODE_PRIVATE);
        String lang = pref.getString("lang","");
        SetLocale(lang);
    }
}

