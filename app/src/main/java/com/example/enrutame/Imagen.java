package com.example.enrutame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Imagen extends AppCompatActivity {

    Button btnTomarFoto, btnGuardarImagen;
    ImageView imgPicture;
    Bitmap bitmap;
    ImageView img;
    Button btn;
    Uri uriImg;
    private static final  int REQUEST_PERMISSION_CAMERA =100;
    private static final int TAKE_PICTURE = 101;

    private static final int REQUEST_PERMISSION_WRITE_STORAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);

        //UI
        initUI();

        btnTomarFoto.setOnClickListener(this::onClick);
        btnGuardarImagen.setOnClickListener(this::onClick);

        img = findViewById(R.id.imageView);
        btn = findViewById(R.id.button);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(ActivityCompat.checkSelfPermission(Imagen.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.GET_PERMISSIONS){

                ActivityCompat.requestPermissions(Imagen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }

        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentW = new Intent(Intent.ACTION_SEND);
                intentW.setType("image/*");
                intentW.setPackage("com.whatsapp"); //para compartir en wasap

                if(uriImg != null){

                    intentW.putExtra(Intent.EXTRA_STREAM, uriImg);

                    try {
                        startActivity(intentW);
                    }catch (Exception e){
                        Toast.makeText(Imagen.this, "Error al enviar\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Imagen.this, "No se selecciono una imagen", Toast.LENGTH_SHORT).show();
                }

            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                try {
                    startActivityForResult(intenGaleria, 1);
                }catch (Exception e){
                    Toast.makeText(Imagen.this, "Error al abrir galeria", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    private void initUI(){
        btnGuardarImagen = findViewById(R.id.btnGuardarFoto);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        imgPicture = findViewById(R.id.imgPicture);
    }

    /**Override*/
    public void onClick(View view){
        int id = view.getId();

        if(id== R.id.btnTomarFoto){
            checkPermissionCamera();
        }else if(id == R.id.btnGuardarFoto){
            checkPermissionStorage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == TAKE_PICTURE){
            if(resultCode == Activity.RESULT_OK && data!=null){
                bitmap = (Bitmap) data.getExtras().get("data");
                imgPicture.setImageBitmap(bitmap);
            }
        }

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            uriImg = data.getData();
            img.setImageURI(uriImg);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_CAMERA){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePicture();
            }
        }else if(requestCode == REQUEST_PERMISSION_WRITE_STORAGE){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveImage();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkPermissionCamera() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                takePicture();
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
            }
        }else{
            takePicture();
        }
    }

    private void checkPermissionStorage() {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    saveImage();
                }else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_STORAGE);
                }
            }else{
                saveImage();
            }
        }else{
            saveImage();
        }
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, TAKE_PICTURE);
        }
    }

    private void saveImage(){
        OutputStream fos = null;
        //File file;
        File file = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentResolver resolver = getContentResolver();
            ContentValues values = new ContentValues();

            String fileName = System.currentTimeMillis() + "Image_example";

            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp");
            values.put(MediaStore.Images.Media.IS_PENDING,1);

            Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri imageUri = resolver.insert(collection,values);

            try {
                fos = resolver.openOutputStream(imageUri);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }

            values.clear();
            values.put(MediaStore.Images.Media.IS_PENDING,0);
            resolver.update(imageUri,values, null, null);
        }else {
            String imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

            String filename  = System.currentTimeMillis() + ".jpg";

            file = new File(imageDir, filename);

            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

        boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        if(saved){
            Toast.makeText(this, "Picture was saved successfully", Toast.LENGTH_LONG).show();
        }
        if(fos!=null){
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file!=null) { //API < 29
                MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, null);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId(); ;

        if(id == R.id.action_settings){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(Imagen.this, "Session Cerrada", Toast.LENGTH_SHORT).show();
            gologing();
        }else{

        }
        return super.onOptionsItemSelected(item);
    }

    private void gologing() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}