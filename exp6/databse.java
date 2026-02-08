package com.example.mobilecom;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etPlace, etState;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI link karna
        etName = findViewById(R.id.etName);
        etPlace = findViewById(R.id.etPlace);
        etState = findViewById(R.id.etState);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Firebase ka reference (Users table)
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String place = etPlace.getText().toString().trim();
            String state = etState.getText().toString().trim();

            if (name.isEmpty() || place.isEmpty() || state.isEmpty()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            // Data map banana
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("name", name);
            userMap.put("place", place);
            userMap.put("state", state);

            // Cloud par data bhejna
            databaseReference.push().setValue(userMap)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(MainActivity.this, "Details Submitted Successfully!", Toast.LENGTH_SHORT).show();
                        // Fields clear karna
                        etName.setText("");
                        etPlace.setText("");
                        etState.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
