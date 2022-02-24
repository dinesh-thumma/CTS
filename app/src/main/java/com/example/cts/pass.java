package com.example.cts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class pass extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{

    String imei;
    String status;
    Spinner text;
    Button button, click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);

        text = findViewById(R.id.spinner2);
        button = findViewById(R.id.button4);

        click =  findViewById(R.id.button5);
        click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               final Handler handler = new Handler(Looper.getMainLooper());
                       handler.postDelayed(() -> {

                           Toast.makeText(pass.this, "CTS is Up To Date !", Toast.LENGTH_SHORT).show();
                        },800);
            }
        });





        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.data, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        text.setAdapter(adapter);
        text.setOnItemSelectedListener(this);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    try {
                        imei = telephonyManager.getImei();
                    } catch (Exception e) {
                        e.printStackTrace();
                        imei = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                    }
                }
            } else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1010);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    imei = telephonyManager.getDeviceId();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1010);
            }
        }




        button.setOnClickListener(v -> {
            insertData();
            System.out.println("this is the code for integrity " + imei + "sldjflad ff" + status);
        });
    }

    public void insertData(){

        StringRequest request = new StringRequest(Request.Method.POST, "https://fouled-rigs.000webhostapp.com/php/update.php?meid="+imei+"sta="+status, response -> {

            if (response.equalsIgnoreCase("Update successfull")){
                Toast.makeText(this,"Data Updated Successfully ",Toast.LENGTH_LONG).show();
            }
            else{

            }
        }, error -> Toast.makeText(this, error.getMessage(),Toast.LENGTH_LONG).show()
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("meid",imei);
                params.put("sta", status);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        status = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}