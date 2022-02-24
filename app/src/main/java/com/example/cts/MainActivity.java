package com.example.cts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText name, email, password, age,bl;
    CheckBox male, female, other;
    Button register;
    private String gender;
    FirebaseAuth firebase;
    private int LOCATION_PERMISSION_CODE = 1;
    String imei;
    TelephonyManager telephonyManager;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebase = FirebaseAuth.getInstance();
        name = findViewById(R.id.edtusername);
        email = findViewById(R.id.emailAddress);
        password = findViewById(R.id.editTextTextPassword);
        age = findViewById(R.id.editTextNumber);
        register = findViewById(R.id.button);
        male = findViewById(R.id.checkBox);
        female = findViewById(R.id.checkBox2);
        other = findViewById(R.id.checkBox3);
        bl = findViewById(R.id.ba);

        // to get the unique id of the device
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    try {
                        imei = telephonyManager.getImei();
                    } catch (Exception e) {
                        e.printStackTrace();
                        imei = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    }
                }
            } else
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1010);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    imei = telephonyManager.getDeviceId();
                }
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1010);
            }
        }

        bl.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_DEVICE_INFO_SETTINGS);
            startActivity(intent);
        });

        register.setOnClickListener(v -> {

            // to get the unique id of the device
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    if (telephonyManager != null) {
                        try {
                            imei = telephonyManager.getImei();
                        } catch (Exception e) {
                            e.printStackTrace();
                            imei = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                        }
                    }
                } else
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1010);
                }
            } else {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    if (telephonyManager != null) {
                        imei = telephonyManager.getDeviceId();
                    }
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1010);
                }
            }
            String username = name.getText().toString().trim();
            String emailId = email.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String ag = age.getText().toString().trim();
            String blue = bl.getText().toString().trim();


            if(male.isChecked()){
                gender = "Male";
            }
            else if (female.isChecked()){
                gender = "Female";
            }
            else if(other.isChecked()){
                gender = "Other";
            }

            if (username.isEmpty()){
                name.setError("Enter Username");
                name.requestFocus();
            }
            else if(emailId.isEmpty()){
                email.setError("Enter Email");
                email.requestFocus();
            }
            else if(pass.isEmpty()){
                password.setError("Enter Password");
                password.requestFocus();
            }

            else if(blue.isEmpty()){
                bl.setError("Enter BluetoothAddress");
                bl.requestFocus();
            }
            else if(ag.isEmpty()){
                age.setError("Enter age");
                age.requestFocus();
            }
            else {
                firebase.createUserWithEmailAndPassword(emailId, pass).addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Registration unsuccessful !", Toast.LENGTH_LONG).show();
                    }
                    else{


                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        String dt = String.valueOf(cal.getTime());

                        name.setText("");
                        email.setText("");
                        password.setText("");
                        age.setText("");
                        male.setChecked(false);
                        female.setChecked(false);
                        other.setChecked(false);

                        System.out.println("Data is :" + imei + " " + username + " " + emailId + " " + ag + " " + gender + " " + blue);

                        Intent intent = new Intent(MainActivity.this, Symptoms.class);
                        intent.putExtra("uname", username);
                        intent.putExtra("ime",imei);
                        intent.putExtra("email", emailId);
                        intent.putExtra("uage", ag);
                        intent.putExtra("ugender", gender);
                        intent.putExtra("ddt", dt);
                        intent.putExtra("bluet", blue);
                        startActivity(intent);

                        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(getApplicationContext(), " Request Permissioin granted",Toast.LENGTH_LONG ).show();
                        }
                        else {
                            requestPermission();
                        }
                        finish();

                    }
                });
            }
        });
    }

    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this).setTitle("Permission needed")
                    .setMessage("To use the app")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == LOCATION_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this, Homepage.class));

            finish();
        }
    }


}