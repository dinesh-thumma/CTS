package com.example.cts;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Homepage extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    TextView text,text3,text5;
    Button click, scan,action;
    LocationManager locationManager;
    boolean gpsStatus;
    private String dt;                                                                              // To store current date//To store user id
    private String ddt1;                                                                            //To store yesterday date
    private String hin;
    private Toast toast;
    private MediaPlayer mp;
    String imei;
    String name;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        mp = MediaPlayer.create(this, R.raw.sample);

        text = findViewById(R.id.textView6);                                                        //user status
        text3 = findViewById(R.id.textView4);                                                       //name of the user
        scan = findViewById(R.id.button2);
        text5 = findViewById(R.id.textView8);
        click = findViewById(R.id.button3);
        action = findViewById(R.id.button4);
        hin = getIntent().getStringExtra("hin");
        UserData();

        text5.setText(hin);

        discover();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Data();
        }

        click.setOnClickListener(v -> {
            getData();
        });

        scan.setOnClickListener(v -> startActivity(new Intent(Homepage.this, Homepage.class)));

        action.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, pass.class);
            startActivity(intent);
            finish();
        });

    }

    public void discover(){

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    int rss = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    if(rss>=-60){
                        name = device.getAddress();
                    }
                }
            }
        };


        if(bluetoothAdapter.isEnabled()){
            checkGpsStatus();

            if(gpsStatus){
                if(bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                if(!bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.startDiscovery();
                }
                if(bluetoothAdapter.startDiscovery()) {
                    Toast.makeText(getApplicationContext(), "Started Scanning", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Please Turn on your GPS and Refresh the Page", Toast.LENGTH_SHORT).show();
                mp.start();
            }
        }
        else {
            checkBluetooth();

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                Toast.makeText(Homepage.this, "Please Refresh The Page After Turning The Bluetooth On", Toast.LENGTH_SHORT).show();
                mp.start();
            }, 4000);
        }

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, intentFilter);
        temp();
    }


    public void temp(){

        repeat();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            View myView = findViewById(R.id.button3);
            myView.performClick();
        },8000);
    }


    public void repeat(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            View myView = findViewById(R.id.button2);
            myView.performClick();
        },120000);
    }



    private void ShowToast() {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.customtoast, findViewById(R.id.custom_toast));
        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> mp.start(), 500);
    }


    protected void checkBluetooth() {
        if (bluetoothAdapter == null) {
            /* Toast.makeText(Homepage.this, "Bluetooth is not supported", Toast.LENGTH_SHORT).show(); */
        }
        else if(!bluetoothAdapter.isEnabled()){
            Intent blue = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(blue, REQUEST_ENABLE_BT);
        }

        if(bluetoothAdapter.isEnabled()){
            //Toast.makeText(Homepage.this, "Bluetooth is Enabled", Toast.LENGTH_SHORT).show();
        }
    }



    public void checkGpsStatus(){
        locationManager = (LocationManager)getApplicationContext().getSystemService(getApplicationContext().LOCATION_SERVICE);
        assert locationManager != null;
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(gpsStatus) {
        } else {
        }
    }

    public void getData(){

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

        class background extends AsyncTask<String, Void, String> {

            @RequiresApi(api = Build.VERSION_CODES.M)
            protected void onPostExecute(String daa){

                try {
                    JSONArray jsonArray = new JSONArray(daa);
                    JSONObject object = null;

                    for(int i = 0; i<jsonArray.length(); i++){
                        object = jsonArray.getJSONObject(i);
                        String stat = object.getString("Status");
                        if(stat.equals("Infected")){
                            ShowToast();
                           insertData();
                        }else{
                            //ShowToast();
                        }
                    }

                } catch (JSONException e) {
                   // Toast.makeText(getApplicationContext(),daa,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... strings) {

                try{
                    URL url = new URL("https://fouled-rigs.000webhostapp.com/php/getData.php?blueid="+name);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuffer daa = new StringBuffer();
                    String line;

                    while((line =reader.readLine())!=null){

                        daa.append(line).append("\n");
                    }
                    reader.close();
                    return daa.toString();
                }catch(Exception e){
                    return e.getMessage();
                }
            }
        }

        background bac = new background();
        bac.execute();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void insertData(){

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

        StringRequest request = new StringRequest(Request.Method.POST, "https://fouled-rigs.000webhostapp.com/php/change.php?meid="+imei, response -> {

            if (response.equalsIgnoreCase("Data Submit Successfully")){
                Toast.makeText(this,"Data inserted",Toast.LENGTH_LONG).show();
            }
            else{
                /* Toast.makeText(this,response,Toast.LENGTH_LONG).show(); */
            }
        }, error -> Toast.makeText(this, error.getMessage(),Toast.LENGTH_LONG).show()
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("meid",imei);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }


    public void UserData(){
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
        class back extends AsyncTask<String, Void, String> {

            @RequiresApi(api = Build.VERSION_CODES.M)
            protected void onPostExecute(String daa){

                try {
                    JSONArray jsonArray = new JSONArray(daa);
                    JSONObject object = null;

                    for(int i = 0; i<jsonArray.length(); i++){
                        object = jsonArray.getJSONObject(i);
                        String stat = object.getString("Status");
                        String userName = object.getString("Name");
                        text3.setText(userName);
                        text.setText(stat);
                        //String na = object.getString("Bluetooth_Id");
                    }
                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(),daa,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... strings) {

                try{
                    URL url = new URL("https://fouled-rigs.000webhostapp.com/php/user.php?id="+imei);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuffer daa = new StringBuffer();
                    String line;

                    while((line =reader.readLine())!=null){
                        daa.append(line).append("\n");
                    }
                    reader.close();
                    return daa.toString();
                }catch(Exception e){
                    return e.getMessage();
                }
            }
        }

        back bad = new back();
        bad.execute();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)


    public void Data(){
        class back extends AsyncTask<String, Void, String> {

            @RequiresApi(api = Build.VERSION_CODES.M)
            protected void onPostExecute(String daa){

                try {
                    JSONArray jsonArray = new JSONArray(daa);
                    JSONObject object = null;

                    for(int i = 0; i<jsonArray.length(); i++){
                        object = jsonArray.getJSONObject(i);
                    }
                } catch (JSONException e) {
                   // Toast.makeText(getApplicationContext(),daa,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... strings) {

                try{
                    URL url = new URL("https://fouled-rigs.000webhostapp.com/php/date.php?");
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuffer daa = new StringBuffer();
                    String line;

                    while((line =reader.readLine())!=null){
                        daa.append(line).append("\n");
                    }
                    reader.close();
                    return daa.toString();
                }catch(Exception e){
                    return e.getMessage();
                }
            }
        }
        back bad = new back();
        bad.execute();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}