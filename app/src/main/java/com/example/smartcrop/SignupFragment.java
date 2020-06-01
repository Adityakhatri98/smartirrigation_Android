package com.example.smartcrop;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {


    private EditText userEmail;
    private EditText userName;
    private EditText userPwd;
    private EditText userPincode;
    private EditText userAddress;
    private EditText userPhone;
    private JsonPlaceHolder apiInterface;
    private Button btnSignup;
    private TextView alreadyHaveAccount;
    private FrameLayout frame;
    private String regex = "[a-z0-9]+([-+._][a-z0-9]+){0,2}@.*?(\\.(a(?:[cdefgilmnoqrstuwxz]|ero|(?:rp|si)a)|b(?:[abdefghijmnorstvwyz]iz)|c(?:[acdfghiklmnoruvxyz]|at|o(?:m|op))|d[ejkmoz]|e(?:[ceghrstu]|du)|f[ijkmor]|g(?:[abdefghilmnpqrstuwy]|ov)|h[kmnrtu]|i(?:[delmnoqrst]|n(?:fo|t))|j(?:[emop]|obs)|k[eghimnprwyz]|l[abcikrstuvy]|m(?:[acdeghklmnopqrstuvwxyz]|il|obi|useum)|n(?:[acefgilopruz]|ame|et)|o(?:m|rg)|p(?:[aefghklmnrstwy]|ro)|qa|r[eosuw]|s[abcdeghijklmnortuvyz]|t(?:[cdfghjklmnoprtvwz]|(?:rav)?el)|u[agkmsyz]|v[aceginu]|w[fs]|y[etu]|z[amw])\\b){1,2}";
    private String email, name, pwd, pincode, address, phone;
    private Button change;
    private NetworkChangeReceiver receiver;
    private LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadLocale();
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        userEmail = (EditText) view.findViewById(R.id.edt_signup_email);
        userName = (EditText) view.findViewById(R.id.edt_signup_name);
        change = (Button)view.findViewById(R.id.change);
        userPwd = (EditText) view.findViewById(R.id.edt_signup_password);
        userPincode = (EditText) view.findViewById(R.id.edt_signup_pincode);
        userAddress = (EditText) view.findViewById(R.id.edt_signup_address);
        userPhone = (EditText) view.findViewById(R.id.edt_signup_phone);
        layout = (LinearLayout)view.findViewById(R.id.layout);
        btnSignup = (Button) view.findViewById(R.id.btn_signup);
        frame = (FrameLayout) getActivity().findViewById(R.id.frame);
        alreadyHaveAccount = (TextView) view.findViewById(R.id.tv_signin);
        apiInterface = APIClient.getClient().create(JsonPlaceHolder.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showchangeLanguageDialog();
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

        userPhone.addTextChangedListener(new TextWatcher() {
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

        userName.addTextChangedListener(new TextWatcher() {
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

        userAddress.addTextChangedListener(new TextWatcher() {
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

        userPincode.addTextChangedListener(new TextWatcher() {
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


        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new LoginFragment());
            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiver = new NetworkChangeReceiver(layout);
                IntentFilter intentFilter2 = new IntentFilter();
                intentFilter2.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                getActivity().registerReceiver(receiver,intentFilter2);
                if (Utils.isconnected(getContext()))
                {
                    checkEmail();
                }

            }
        });

    }

    private void checkEmail() {
        Drawable custom = getResources().getDrawable(R.drawable.error);
        custom.setBounds(0, 0, custom.getIntrinsicWidth(), custom.getIntrinsicHeight());

        if (userEmail.getText().toString().toLowerCase().matches(regex)) {
            email = userEmail.getText().toString().toLowerCase();
            name = userName.getText().toString();
            pwd = userPwd.getText().toString();
            pincode = userPincode.getText().toString();
            address = userAddress.getText().toString();
            phone = userPhone.getText().toString();
            UserBean model = new UserBean(email, name, pwd, pincode, address, phone, "user3", "0");
            SharedPreferences pref = getContext().getSharedPreferences("websignup", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("email", model.getUserEmail());
            editor.putString("name", model.getUserName());
            editor.putString("pwd", model.getUserPwd());
            editor.putString("address", model.getUserAddress());
            editor.putString("pincode", model.getUserPincode());
            editor.putString("phone", model.getUserPhone());
            editor.putString("node", model.getUserNode());
            editor.putString("auth", model.getUserauth());
            editor.commit();
            //       setFragment(new SendOtpFragment());
            postreq();
        } else {
            userEmail.setError(getString(R.string.emailerror), custom);
        }
    }

    private void postreq() {
        Drawable custom = getResources().getDrawable(R.drawable.ic_warning_black_24dp);
        custom.setBounds(0, 0, custom.getIntrinsicWidth(), custom.getIntrinsicHeight());
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Call<ApiError> call = apiInterface.checkemail(email);
        call.enqueue(new Callback<ApiError>() {
            @Override
            public void onResponse(Call<ApiError> call, Response<ApiError> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ApiError error = response.body();
                        if (error.getCode().equals("409")) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), getString(R.string.emailwar), Toast.LENGTH_SHORT).show();
                            userEmail.setError(getString(R.string.emailwar), custom);
                        } else if (error.getCode().equals("200")) {
                            dialog.dismiss();
                            setFragment(new SendOtpFragment());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiError> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Something went to wrong !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkInputs() {
        Drawable custom = getResources().getDrawable(R.drawable.error);
        custom.setBounds(0, 0, custom.getIntrinsicWidth(), custom.getIntrinsicHeight());
        if (!TextUtils.isEmpty(userName.getText().toString()) && userName.getText().toString().length() > 5) {
            userName.setError(null);
            if (!TextUtils.isEmpty(userPhone.getText().toString()) && userPhone.getText().toString().length() == 10) {
                userPhone.setError(null);
                if (!TextUtils.isEmpty(userAddress.getText().toString()) && userAddress.getText().toString().length() < 50 && userAddress.getText().toString().length() > 10) {
                    userAddress.setError(null);
                    if (!TextUtils.isEmpty(userPincode.getText().toString()) && userPincode.getText().toString().length() == 6) {
                        userPincode.setError(null);
                        if (!TextUtils.isEmpty(userEmail.getText().toString())) {
                            userEmail.setError(null);
                            if (!TextUtils.isEmpty(userPwd.getText().toString()) && userPwd.getText().toString().length() >= 8 && userPwd.getText().toString().length() < 20) {
                                userPwd.setError(null);
                                btnSignup.setEnabled(true);
                                btnSignup.setBackgroundColor(getResources().getColor(R.color.colorblue));
                                btnSignup.setTextColor(getResources().getColor(R.color.colorwhite_enable));
                            } else {
                                userPwd.setError(getString(R.string.pwderror), custom);
                                btnSignup.setEnabled(false);
                                btnSignup.setBackgroundColor(getResources().getColor(R.color.colorlightblue));
                                btnSignup.setTextColor(getResources().getColor(R.color.colorwhite_disable));
                            }
                        } else {
                            userEmail.setError(getString(R.string.emailerror), custom);
                            btnSignup.setEnabled(false);
                            btnSignup.setBackgroundColor(getResources().getColor(R.color.colorlightblue));
                            btnSignup.setTextColor(getResources().getColor(R.color.colorwhite_disable));
                        }

                    } else {
                        userPincode.setError(getString(R.string.pincodeerror), custom);
                        btnSignup.setEnabled(false);
                        btnSignup.setBackgroundColor(getResources().getColor(R.color.colorlightblue));
                        btnSignup.setTextColor(getResources().getColor(R.color.colorwhite_disable));
                    }
                } else {
                    userAddress.setError(getString(R.string.addresserror), custom);
                    btnSignup.setEnabled(false);
                    btnSignup.setBackgroundColor(getResources().getColor(R.color.colorlightblue));
                    btnSignup.setTextColor(getResources().getColor(R.color.colorwhite_disable));
                }
            } else {
                userPhone.setError(getString(R.string.phoneerror), custom);
                btnSignup.setEnabled(false);
                btnSignup.setBackgroundColor(getResources().getColor(R.color.colorlightblue));
                btnSignup.setTextColor(getResources().getColor(R.color.colorwhite_disable));
            }
        } else {
            userName.setError(getString(R.string.nameerror), custom);
            btnSignup.setEnabled(false);
            btnSignup.setBackgroundColor(getResources().getColor(R.color.colorlightblue));
            btnSignup.setTextColor(getResources().getColor(R.color.colorwhite_disable));
        }
    }


    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slideout_right);
        fragmentTransaction.addToBackStack("signup");
        fragmentTransaction.replace(frame.getId(), fragment);
        fragmentTransaction.commit();
    }


    private void showchangeLanguageDialog() {
        final String listitems[] = {"English","हिंदी","ગુજરાતી"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Choose Language...");
        dialog.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialoglocale, int i) {
                if (i==0)
                {
                    SetLocale("en");
                    ReLoadFragment(new SignupFragment());
                }
                else if (i==1)
                {
                    SetLocale("hi");
                    ReLoadFragment(new SignupFragment());
                }
                else if (i==2)
                {
                    SetLocale("gu");
                    ReLoadFragment(new SignupFragment());
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
    private void ReLoadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(this);
        fragmentTransaction.attach(this);
        fragmentTransaction.commit();
    }
}