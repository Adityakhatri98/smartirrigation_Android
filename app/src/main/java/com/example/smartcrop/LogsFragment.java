package com.example.smartcrop;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
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

public class LogsFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {


    ListView listView;
    private DatabaseReference mDatabase;
    private DatabaseReference mData;
    Node data;
    ArrayList<Node> list;
    ArrayList<String> d;
    MyBaseAdapter baseAdapter;
    private Spinner spinner;
    private ProgressBar progress;
    private LinearLayout lin;
    private TextView tvSelect;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_report, container, false);
        listView = (ListView) view.findViewById(R.id.node_list);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        loadLocale();
        lin = (LinearLayout)view.findViewById(R.id.lin);
        tvSelect = (TextView)view.findViewById(R.id.tv_select);
        progress = (ProgressBar)view.findViewById(R.id.progress);
        spinner.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress.setVisibility(View.VISIBLE);
        firebase();
    }

    private void firebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference("USER/"+MainActivity.node+"/LOGS");
        list = new ArrayList<>();
        d = new ArrayList<>();
        d.add(getString(R.string.select));
        lin.setVisibility(View.VISIBLE);
        baseAdapter = new MyBaseAdapter(getContext(), list);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String date = dataSnapshot.getKey();
                if (date.equals("1970-01-01")) {

                } else {
                    d.add(date);
                    ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, d);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(aa);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                list.clear();
                d.clear();
                getData(dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    public void getData(String cDate) {
        mData = FirebaseDatabase.getInstance().getReference("USER/"+MainActivity.node+"/LOGS/" + cDate);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String date = dataSnapshot.getKey();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    data = new Node();
                    data.setDate(date);
                    data.setTime(ds.getKey());
                    for (DataSnapshot ds1 : ds.getChildren()) {

                        String keys = ds1.getKey();

                        if (keys.equals("node1")) {
                            data.setN1(ds1.getValue().toString());

                        } else if (keys.equals("node2")) {
                            data.setN2(ds1.getValue().toString());
                        } else if (keys.equals("node3")) {
                            data.setN3(ds1.getValue().toString());
                        } else if (keys.equals("node4")) {
                            data.setN4(ds1.getValue().toString());
                        }
                    }
                    list.add(data);
                }
                listView.setAdapter(baseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        progress.setVisibility(View.GONE);
        if (position == 0) {
            tvSelect.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            list.clear();
            getData(d.get(position));
            tvSelect.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
