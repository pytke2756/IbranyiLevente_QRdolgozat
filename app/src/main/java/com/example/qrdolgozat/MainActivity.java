package com.example.qrdolgozat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button scanBtn;
    private Button kiirBtn;
    private TextView tvAdatok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setPrompt("QR code olvasás dolgozathoz");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.initiateScan();
            }
        });

        kiirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvAdatok.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Nincs mit beleírni a fileba!", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        fileba(tvAdatok.getText().toString());
                        Toast.makeText(MainActivity.this, "Sikeres fileba írás!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Kiléptél", Toast.LENGTH_SHORT).show();
            }
            else{
                tvAdatok.setText(result.getContents());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    //scannedCodes.csv
    //yyyy-MM-dd HH:mm:ss
    public void fileba(String adat) throws IOException {
        Date datum = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formazottDatum = df.format(datum);
        String sor = String.format("%s, %s", formazottDatum, adat);

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            File file = new File(Environment.getExternalStorageDirectory(), "scannedCodes.csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

            bw.append(sor);
            bw.append(System.lineSeparator());
            bw.close();
        }
    }



    private void init(){
        scanBtn = findViewById(R.id.btn_scan);
        kiirBtn = findViewById(R.id.btn_kiir);
        tvAdatok = findViewById(R.id.tv_adatok);
    }
}