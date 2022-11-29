package com.example.enrutame;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import android.os.Bundle;

public class RegistrarActivity extends AppCompatActivity {

    private Button registrarbtn3;
    private EditText emailetxt,passwordetxt;

    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        firebaseAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.et_mail3, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.et_pass3,".{6,}",R.string.invalid_password);

        emailetxt = findViewById(R.id.et_mail3);
        passwordetxt = findViewById(R.id.et_pass3);
        registrarbtn3 = findViewById(R.id.btn_Registrar3);

        registrarbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = emailetxt.getText().toString();
                String pass = passwordetxt.getText().toString();

                if (awesomeValidation.validate()){
                    firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegistrarActivity.this, "Usuario creado con exito", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                dameToastdeerror(errorCode);
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegistrarActivity.this, "Por favor de rellenar todos los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(RegistrarActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentaciÃ³n", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(RegistrarActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(RegistrarActivity.this, "La credencial de autenticaciÃ³n proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(RegistrarActivity.this, "La direcciÃ³n de correo electrÃ³nico estÃ¡ mal formateada.", Toast.LENGTH_LONG).show();
                emailetxt.setError("La direcciÃ³n de correo electrÃ³nico estÃ¡ mal formateada.");
                emailetxt.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(RegistrarActivity.this, "La contraseÃ±a no es vÃ¡lida o el usuario no tiene contraseÃ±a.", Toast.LENGTH_LONG).show();
                passwordetxt.setError("la contraseÃ±a es incorrecta ");
                passwordetxt.requestFocus();
                passwordetxt.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(RegistrarActivity.this, "Las credenciales proporcionadas no corresponden al usuario que iniciÃ³ sesiÃ³n anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(RegistrarActivity.this, "Esta operaciÃ³n es sensible y requiere autenticaciÃ³n reciente. Inicie sesiÃ³n nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(RegistrarActivity.this, "Ya existe una cuenta con la misma direcciÃ³n de correo electrÃ³nico pero diferentes credenciales de inicio de sesiÃ³n. Inicie sesiÃ³n con un proveedor asociado a esta direcciÃ³n de correo electrÃ³nico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(RegistrarActivity.this, "La direcciÃ³n de correo electrÃ³nico ya estÃ¡ siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                emailetxt.setError("La direcciÃ³n de correo electrÃ³nico ya estÃ¡ siendo utilizada por otra cuenta.");
                emailetxt.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(RegistrarActivity.this, "Esta credencial ya estÃ¡ asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(RegistrarActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(RegistrarActivity.this, "La credencial del usuario ya no es vÃ¡lida. El usuario debe iniciar sesiÃ³n nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(RegistrarActivity.this, "No hay ningÃºn registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(RegistrarActivity.this, "La credencial del usuario ya no es vÃ¡lida. El usuario debe iniciar sesiÃ³n nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(RegistrarActivity.this, "Esta operaciÃ³n no estÃ¡ permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(RegistrarActivity.this, "La contraseÃ±a proporcionada no es vÃ¡lida..", Toast.LENGTH_LONG).show();
                passwordetxt.setError("La contraseÃ±a no es vÃ¡lida, debe tener al menos 6 caracteres");
                passwordetxt.requestFocus();
                break;

        }
    }

}