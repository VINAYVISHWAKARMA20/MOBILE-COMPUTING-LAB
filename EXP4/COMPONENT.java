package com.example.mobilecom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView profileImage;
    private int selectedAge = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileImage = findViewById(R.id.profileImage);
        EditText etName = findViewById(R.id.etName);
        SeekBar sbAge = findViewById(R.id.sbAge);
        TextView tvAgeLabel = findViewById(R.id.tvAgeLabel);
        Spinner spCity = findViewById(R.id.spCity);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        TextView tvResult = findViewById(R.id.tvResult);

        String[] cities = {"Mumbai", "Delhi", "Bangalore", "Pune", "Chennai"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        spCity.setAdapter(adapter);

        sbAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedAge = progress;
                tvAgeLabel.setText("Select Age: " + selectedAge);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        ActivityResultLauncher<Intent> picker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            profileImage.setImageBitmap(bitmap);
                            profileImage.clearColorFilter();
                        } catch (Exception e) { e.printStackTrace(); }
                    }
                }
        );

        findViewById(R.id.btnUploadImage).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            picker.launch(intent);
        });

        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String city = spCity.getSelectedItem().toString();

            if (!name.isEmpty()) {
                String res = "Registered: " + name + "\nAge: " + selectedAge + "\nCity: " + city;
                tvResult.setText(res);
            } else {
                Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
