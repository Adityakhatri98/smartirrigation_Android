package com.example.smartcrop;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.anastr.speedviewlib.TubeSpeedometer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.MODE_PRIVATE;

public class ChartFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference node1;
    private ArrayList<Integer> nodedata;
    int n1, n2, n3, n4;
    private TubeSpeedometer tubeSpeedometer;
    private Button btn_node1;
    private Button btn_node2;
    private Button btn_node3;
    private Button btn_node4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_chart, container, false);
        loadLocale();
        tubeSpeedometer = (TubeSpeedometer) view.findViewById(R.id.tubeSpeedometer);
        btn_node1 = (Button) view.findViewById(R.id.node1);
        btn_node2 = (Button) view.findViewById(R.id.node2);
        btn_node3 = (Button) view.findViewById(R.id.node3);
        btn_node4 = (Button) view.findViewById(R.id.node4);
        btn_node1.setOnClickListener(this);
        btn_node2.setOnClickListener(this);
        btn_node3.setOnClickListener(this);
        btn_node4.setOnClickListener(this);
        return view;
    }

    private void firebase() {
        nodedata = new ArrayList<>();
        node1 = FirebaseDatabase.getInstance().getReference("USER/"+MainActivity.node+"/node_data/allnodedata");
        node1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                n1 = Integer.parseInt(dataSnapshot.child("node1data").getValue().toString());
                n2 = Integer.parseInt(dataSnapshot.child("node2data").getValue().toString());
                n3 = Integer.parseInt(dataSnapshot.child("node3data").getValue().toString());
                n4 = Integer.parseInt(dataSnapshot.child("node4data").getValue().toString());
                nodedata.add(n1);
                nodedata.add(n2);
                nodedata.add(n3);
                nodedata.add(n4);
                nodedata(nodedata);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    int data[] = new int[4];

    private void nodedata(ArrayList<Integer> nodedata) {

        for (int j = 0; j < 4; j++) {
            float convert = ((float) (800 - nodedata.get(j)) / (float) 435) * 100;

            if (convert < 0) {
                convert = 0;
            }
            if (convert > 100) {
                convert = 100;
            }
            data[j] = Math.round(convert);
        }
        nodedata.clear();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        firebase();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.node1:
                btn_node1.setBackgroundResource(R.drawable.my_btn_change);
                btn_node2.setBackgroundResource(R.drawable.my_btn);
                btn_node3.setBackgroundResource(R.drawable.my_btn);
                btn_node4.setBackgroundResource(R.drawable.my_btn);
                tubeSpeedometer.speedTo((data[0]));
                break;
            case R.id.node2:
                btn_node1.setBackgroundResource(R.drawable.my_btn);
                btn_node2.setBackgroundResource(R.drawable.my_btn_change);
                btn_node3.setBackgroundResource(R.drawable.my_btn);
                btn_node4.setBackgroundResource(R.drawable.my_btn);
                tubeSpeedometer.speedTo((data[1]));
                break;
            case R.id.node3:
                btn_node1.setBackgroundResource(R.drawable.my_btn);
                btn_node2.setBackgroundResource(R.drawable.my_btn);
                btn_node3.setBackgroundResource(R.drawable.my_btn_change);
                btn_node4.setBackgroundResource(R.drawable.my_btn);
                tubeSpeedometer.speedTo((data[2]));
                break;
            case R.id.node4:
                btn_node1.setBackgroundResource(R.drawable.my_btn);
                btn_node2.setBackgroundResource(R.drawable.my_btn);
                btn_node3.setBackgroundResource(R.drawable.my_btn);
                btn_node4.setBackgroundResource(R.drawable.my_btn_change);
                tubeSpeedometer.speedTo((data[3]));
                break;

        }

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
