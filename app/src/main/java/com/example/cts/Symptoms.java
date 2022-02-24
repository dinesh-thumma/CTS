package com.example.cts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Symptoms extends AppCompatActivity {

    Button submit;
    CardView one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen;
    RadioButton positive, negative;
    final float x = 2.25f;
    int i = 0;
    float sum = 0;
    String status = "";
    String hint;
    String date;
    ArrayList<Float> arr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        one = findViewById(R.id.c1);
        two = findViewById(R.id.c2);
        three = findViewById(R.id.c3);
        four = findViewById(R.id.c4);
        five = findViewById(R.id.c5);
        six = findViewById(R.id.c6);
        seven = findViewById(R.id.c7);
        eight = findViewById(R.id.c8);
        nine = findViewById(R.id.c9);
        ten = findViewById(R.id.c10);
        eleven = findViewById(R.id.c11);
        twelve = findViewById(R.id.c12);
        thirteen = findViewById(R.id.c13);
        submit = findViewById(R.id.button);
        positive = findViewById(R.id.radioButton);
        negative = findViewById(R.id.radioButton2);



        one.setOnClickListener(v -> {

            float item = 2*x;
            if(arr.add(item)){
                one.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });

        two.setOnClickListener(v -> {
            float item = 2*x;
            if(arr.add(item)){
                two.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });

        three.setOnClickListener(v -> {
            float item = 3*x;
            if(arr.add(item)){
                three.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });

        four.setOnClickListener(v -> {
            float item = 3*x;
            if(arr.add(item)){
                four.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });

        five.setOnClickListener(v -> {
            float item = 4*x;
            if(arr.add(item)){
                five.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });

        six.setOnClickListener(v -> {
            float item = 4*x;
            if(arr.add(item)){
                six.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });

        seven.setOnClickListener(v -> {
            float item = 2*x;
            if(arr.add(item)){
                seven.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });

        eight.setOnClickListener(v -> {
            float item = 2*x;
            if(arr.add(item)){
                eight.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });

        nine.setOnClickListener(v -> {
            float item = 4*x;
            if(arr.add(item)){
                nine.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });

        ten.setOnClickListener(v -> {
            float item = 4*x;
            if(arr.add(item)){
                ten.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });

        eleven.setOnClickListener(v -> {

            float item = 4*x;
            if(arr.add(item)){
                eleven.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });

        twelve.setOnClickListener(v -> {
            float item = 3*x;
            if(arr.add(item)){
                twelve.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });

        thirteen.setOnClickListener(v -> {
            float item = 3*x;
            if(arr.add(item)){
                thirteen.setCardBackgroundColor(Color.parseColor("#a86032"));
            }
        });
        positive.setOnClickListener(v -> status = "Infected");

        negative.setOnClickListener(v -> status = "Normal");
        submit.setOnClickListener(v -> {


            for (i = 0; i < arr.size(); i++) {
                sum = sum + arr.get(i);
            }
            if (sum >= 25) {
                hint = "Please Consider the Doctor";
            }
            else if(sum<25 && status.equals("Normal")){
                hint="you are safe";
            }else if(sum<25 && status.equals("Infected")){
                hint = "Please contact doctor and maintain social distance";
            }
           // System.out.println(sum);
            insertData();
            Intent intent = new Intent(Symptoms.this, Homepage.class);
            intent.putExtra("percent", sum);
            intent.putExtra("hin", hint);
            startActivity(intent);
            finish();

        });
    }

    // this method is to insert the data into the mysql database
    public void insertData(){

        String username  = getIntent().getStringExtra("uname");
        String em = getIntent().getStringExtra("email");
        String age = getIntent().getStringExtra("uage");
        String gender = getIntent().getStringExtra("ugender");
        date = getIntent().getStringExtra("ddt");
        String id1 = getIntent().getStringExtra("ime");
        String bll = getIntent().getStringExtra("bluet");

        StringRequest request = new StringRequest(Request.Method.POST, "https://fouled-rigs.000webhostapp.com/php/index.php", response -> {

            if (response.equalsIgnoreCase("Data Submit Successfully")){
               // Toast.makeText(Symptoms.this,"Data inserted",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(Symptoms.this,response,Toast.LENGTH_LONG).show();
            }

        }, error -> Toast.makeText(Symptoms.this, error.getMessage(),Toast.LENGTH_LONG).show()
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("id_no",id1);
                params.put("email",em);
                params.put("bluet",bll);
                params.put("name",username);
                params.put("gender",gender);
                params.put("Age",age);
                params.put("statu",status);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Symptoms.this);
        requestQueue.add(request);

    }

}