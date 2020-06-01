package com.example.smartcrop;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsActivity extends AppCompatActivity {


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Settings");

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SwitchPreferenceCompat manual;
        private SwitchPreferenceCompat master;
        private SwitchPreferenceCompat slave;
        private SwitchPreferenceCompat moter;
        private DatabaseReference master_mode;
        private DatabaseReference reset_node;
        private int n1,n2,n3,n4;


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            manual = (SwitchPreferenceCompat) findPreference("manual");
            master = (SwitchPreferenceCompat) findPreference("master");
            slave = (SwitchPreferenceCompat) findPreference("slave");
            moter = (SwitchPreferenceCompat) findPreference("moter");
            master_mode = FirebaseDatabase.getInstance().getReference("USER/"+MainActivity.node+"/settings/MASTER_MODE");
            reset_node = FirebaseDatabase.getInstance().getReference("USER/"+MainActivity.node+"/settings/RESET_NODES");


            //manual Mode
            master_mode.child("manual").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (Integer.parseInt(dataSnapshot.getValue().toString())==1) {
                        manual.setChecked(true);
                        moter.setEnabled(true);
                    } else {
                        manual.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if (manual.isChecked()) {
                moter.setEnabled(true);
            } else {
                moter.setEnabled(false);
            }
            manual.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (manual.isChecked()) {
                        master_mode.child("manual").setValue(1);
                        moter.setEnabled(true);
                    } else {
                        master_mode.child("manual").setValue(0);
                        moter.setChecked(false);
                        moter.setEnabled(false);
                        master_mode.child("motor_on_off").setValue(0);
                    }
                    return true;
                }
            });


            //motor_on_off
            master_mode.child("motor_on_off").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (Integer.parseInt(dataSnapshot.getValue().toString())==1) {
                        moter.setChecked(true);
                    } else {
                        moter.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            moter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    if (moter.isChecked()) {
                        master_mode.child("motor_on_off").setValue(1);

                    } else {
                        master_mode.child("motor_on_off").setValue(0);
                    }

                    return true;
                }
            });


            //
            reset_node.child("MASTER_RESET").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (Integer.parseInt(dataSnapshot.getValue().toString())==1) {
                        master.setChecked(true);

                    } else {
                        master.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            master.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (master.isChecked()) {
                        reset_node.child("MASTER_RESET").setValue(1);

                    } else {
                        reset_node.child("MASTER_RESET").setValue(0);
                    }
                    return true;
                }
            });

            //SLAVE_RESET
            reset_node.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    n1 = Integer.parseInt(dataSnapshot.child("node1reset").getValue().toString());
                    n2 = Integer.parseInt(dataSnapshot.child("node2reset").getValue().toString());
                    n3 = Integer.parseInt(dataSnapshot.child("node3reset").getValue().toString());
                    n4 = Integer.parseInt(dataSnapshot.child("node4reset").getValue().toString());
                    if (n1==1&&n2==1&&n3==1&&n4==1) {
                        slave.setChecked(true);
                    } else {
                        slave.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            slave.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (slave.isChecked()) {
                        reset_node.child("node1reset").setValue(1);
                        reset_node.child("node2reset").setValue(1);
                        reset_node.child("node3reset").setValue(1);
                        reset_node.child("node4reset").setValue(1);

                    } else {
                        reset_node.child("node1reset").setValue(0);
                        reset_node.child("node2reset").setValue(0);
                        reset_node.child("node3reset").setValue(0);
                        reset_node.child("node4reset").setValue(0);
                    }
                    return true;
                }
            });
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }
}