package com.example.enrutame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Button loginbtn, signupbtn,recuperarbtn;
    private EditText et_mail2, et_pass2;

    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_mail2 = findViewById(R.id.et_email);
        et_pass2 = findViewById(R.id.et_passw);

        loginbtn = findViewById(R.id.btn_login);
        signupbtn = findViewById(R.id.btn_registrar);
        recuperarbtn = findViewById(R.id.btn_recuperar);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user  = mAuth.getCurrentUser();


        if(user != null){
            irahome();
        }

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.et_email, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.et_passw,".{6,}",R.string.invalid_password);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegistrarActivity.class);
                startActivity(i);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()){
                    String mail = et_mail2.getText().toString();
                    String pass = et_pass2.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                irahome();
                            }else{
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                dameToastdeerror(errorCode);
                            }
                        }
                    });
                }
            }
        });

        recuperarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, recuperarPassActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void irahome() {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("null", et_mail2.getText().toString());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(MainActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentaciÃ³n", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(MainActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(MainActivity.this, "La credencial de autenticaciÃ³n proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(MainActivity.this, "La direcciÃ³n de correo electrÃ³nico estÃ¡ mal formateada.", Toast.LENGTH_LONG).show();
                et_mail2.setError("La direcciÃ³n de correo electrÃ³nico estÃ¡ mal formateada.");
                et_mail2.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(MainActivity.this, "La contraseÃ±a no es vÃ¡lida o el usuario no tiene contraseÃ±a.", Toast.LENGTH_LONG).show();
                et_pass2.setError("la contraseÃ±a es incorrecta ");
                et_pass2.requestFocus();
                et_pass2.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(MainActivity.this, "Las credenciales proporcionadas no corresponden al usuario que iniciÃ³ sesiÃ³n anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(MainActivity.this, "Esta operaciÃ³n es sensible y requiere autenticaciÃ³n reciente. Inicie sesiÃ³n nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(MainActivity.this, "Ya existe una cuenta con la misma direcciÃ³n de correo electrÃ³nico pero diferentes credenciales de inicio de sesiÃ³n. Inicie sesiÃ³n con un proveedor asociado a esta direcciÃ³n de correo electrÃ³nico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(MainActivity.this, "La direcciÃ³n de correo electrÃ³nico ya estÃ¡ siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                et_mail2.setError("La direcciÃ³n de correo electrÃ³nico ya estÃ¡ siendo utilizada por otra cuenta.");
                et_mail2.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(MainActivity.this, "Esta credencial ya estÃ¡ asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(MainActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(MainActivity.this, "La credencial del usuario ya no es vÃ¡lida. El usuario debe iniciar sesiÃ³n nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(MainActivity.this, "No hay ningÃºn registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(MainActivity.this, "La credencial del usuario ya no es vÃ¡lida. El usuario debe iniciar sesiÃ³n nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(MainActivity.this, "Esta operaciÃ³n no estÃ¡ permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(MainActivity.this, "La contraseÃ±a proporcionada no es vÃ¡lida..", Toast.LENGTH_LONG).show();
                et_pass2.setError("La contraseÃ±a no es vÃ¡lida, debe tener al menos 6 caracteres");
                et_pass2.requestFocus();
                break;

        }
    }

}