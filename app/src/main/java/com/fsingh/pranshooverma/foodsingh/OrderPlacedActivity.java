package com.fsingh.pranshooverma.foodsingh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderPlacedActivity extends AppCompatActivity {

    TextView tvOrderID, tvOrderAmount;
    Button btnHome,  btnTrack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        tvOrderID = (TextView) findViewById(R.id.order_id);
        tvOrderAmount = (TextView) findViewById(R.id.order_amount);
        btnHome = (Button) findViewById(R.id.btn_home);
        btnTrack = (Button) findViewById(R.id.btn_track);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("id");
        String orderAmount = intent.getStringExtra("amount");
        tvOrderID.setText("FS"+orderId);
        tvOrderAmount.setText("₹"+orderAmount);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderPlacedActivity.this, menu.class));
                finish();
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
