package com.example.smartcrop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import com.chaos.view.PinView;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.content.Context.MODE_PRIVATE;


public class SendOtpFragment extends Fragment implements View.OnClickListener {

    private PinView pinView;
    private Button next;
    private TextView textU;
    private String otp;
    private TextView resendotp;
    private String email;
    private static JsonPlaceHolder apiInterface;
    private String name, pwd, address, pincode, auth, node, phone;
    private View frame;
    private TextView text;

    public SendOtpFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_otp, container, false);
        pinView = view.findViewById(R.id.pinView);
        next = view.findViewById(R.id.button);
        loadLocale();
        textU = view.findViewById(R.id.textView_noti);
        resendotp = view.findViewById(R.id.resendotp);
        next.setOnClickListener(this::onClick);
        text = view.findViewById(R.id.topText);
        frame = view.findViewById(R.id.frame);
        apiInterface = APIClient.getClient().create(JsonPlaceHolder.class);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SharedPreferences pref = getContext().getSharedPreferences("websignup", 0);
        SharedPreferences.Editor editor = pref.edit();
        name = pref.getString("name", null);
        email = pref.getString("email", null);
        phone = pref.getString("phone", null);
        pincode = pref.getString("pincode", null);
        auth = pref.getString("auth", null);
        resendotp.setOnClickListener(this::onClick);
        address = pref.getString("address", null);
        pwd = pref.getString("pwd", null);
        node = pref.getString("node", null);
        text.append(" " + email);
        genOtp();
        callThread(otp);
        timer();
    }
    Activity activity = getActivity();

    private void timer() {
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                if(activity != null){
                    resendotp.setTextColor(getResources().getColor(R.color.colorblack_enable));
                    resendotp.setText(getString(R.string.remaining) + millisUntilFinished / 1000);
                }
            }

            public void onFinish() {
                if(activity != null){
                    resendotp.setTextColor(getResources().getColor(R.color.colorblack));
                    resendotp.setText(getString(R.string.resend));
                }
            }
        }.start();
    }

    public void genOtp() {
        otp = new DecimalFormat("0000").format(new Random().nextInt(9999));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.resendotp:
                if (resendotp.getText().equals(getString(R.string.resend))) {
                    Log.e("check", "onClick: " );
                    pinView.setText("");
                    genOtp();
                    callThread(otp);
                    timer();
                }
                break;

            case R.id.button:
                if (next.getText().equals("Verify") || next.getText().equals("વેરિફાઈ") || next.getText().equals("वेरिफ़ाई")) {
                    String OTP = pinView.getText().toString();
                    if (OTP.equals(otp)) {
                        pinView.setLineColor(Color.GREEN);
                        textU.setText(getString(R.string.otpdone));
                        textU.setTextColor(Color.GREEN);
                        next.setText(getString(R.string.loginnow));
                        postreq();
                    } else if (pinView.equals("")){
                        Toast.makeText(getActivity(), "Please Enter OTP", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        pinView.setLineColor(Color.RED);
                        textU.setText(getString(R.string.incorrect));
                        textU.setTextColor(Color.RED);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter the details", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void postreq() {

        Call<UserBean> call = apiInterface.createPost(email, name, pwd, pincode
                , address, phone);
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
                    Toast.makeText(getActivity(), "Something Went Wrong!!!", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    UserBean model = response.body();
                    Intent intent = new Intent(getContext(), RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {

            }
        });
    }

    private void callThread(String otp) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("check", "run: "+otp);
                    SendEmail.send(otp, email);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void SetLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("lang", MODE_PRIVATE).edit();
        editor.putString("lang", lang);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences pref = getActivity().getSharedPreferences("lang", MODE_PRIVATE);
        String lang = pref.getString("lang", "");
        SetLocale(lang);
    }
}