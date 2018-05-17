package com.vasu_gupta.firebasetalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseActivity extends AppCompatActivity {

    DatabaseReference databaseReference ;

    EditText etData ;
    Button btnAdd ;
    ListView lvItems ;

    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        databaseReference = FirebaseDatabase.getInstance().getReference("Items");

        etData = findViewById(R.id.etData);

        btnAdd = findViewById(R.id.btnAdd);

        lvItems = findViewById(R.id.listview);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String temp = etData.getText().toString();
                if (!temp.equals("")){
                    String key = databaseReference.push().getKey();
                    databaseReference.push().child("item-name").setValue(temp);
                    etData.setText("");
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String str_value = dataSnapshot1.child("item-name").getValue(String.class);
                    list.add(0 , str_value);
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(DatabaseActivity.this ,R.layout.list_item ,list);
                lvItems.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
