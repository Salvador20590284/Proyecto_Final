package com.example.enrutame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import android.os.Bundle;

public class recuperarPassActivity extends AppCompatActivity {

    private Button recuperarboton2;
    private EditText emailEdit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_pass);

        recuperarboton2 = findViewById(R.id.btn_recuperar2);
        emailEdit2 = findViewById(R.id.et_email2);

        recuperarboton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(recuperarPassActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void validate() {
        String email = emailEdit2.getText().toString();
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdit2.setError("Correo Invalido");
            return;
        }

        sendEmail(email);

    }

    private void sendEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;
        auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(recuperarPassActivity.this, "Correo Enviado!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(recuperarPassActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(recuperarPassActivity.this, "Correo Invalido!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}