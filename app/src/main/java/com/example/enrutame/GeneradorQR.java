package com.example.enrutame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GeneradorQR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generador_qr);

        EditText txtDatos = findViewById(R.id.txtDatos);
        Button btnGenera = findViewById(R.id.btnGenera);
        ImageView imgQr = findViewById(R.id.qrCode);

        btnGenera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder(); //este es el que nos ayuda a generar un codigo de barras
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(txtDatos.getText().toString(), BarcodeFormat.QR_CODE, 750, 750);
                    imgQr.setImageBitmap(bitmap);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}