package com.example.smartcrop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

import androidx.constraintlayout.widget.ConstraintLayout;

public class NetworkChangeReceiver extends BroadcastReceiver {
    LinearLayout layout;
    ConstraintLayout layout1;
    public NetworkChangeReceiver() {

    }

    public NetworkChangeReceiver(LinearLayout layout) {
        this.layout = layout;
    }

    public NetworkChangeReceiver(ConstraintLayout layout) {
        this.layout1 = layout;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!Utils.isconnected(context)) {
            if (layout1!=null)
            {
                Snackbar.make(layout1, "Please check your internet connection !",
                        Snackbar.LENGTH_LONG).show();
            }
            else {
                Snackbar.make(layout, "Please check your internet connection !",
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
