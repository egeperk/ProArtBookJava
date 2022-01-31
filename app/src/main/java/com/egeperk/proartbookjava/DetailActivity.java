package com.egeperk.proartbookjava;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    EditText editText;
    Button saveButton;
    Button updateButton;
    Button deleteButton;
    Bitmap selectedImage;

    String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        imageView = findViewById(R.id.imageView);
        editText = findViewById(R.id.edit_tv);
        saveButton = findViewById(R.id.saveButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);


        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if (info.matches("new")) {

            Bitmap background = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.select);
            imageView.setImageBitmap(background);
            editText.setText("");
            saveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
            updateButton.setVisibility(View.GONE);
        } else {

            String name = intent.getStringExtra("name");
            editText.setText(name);
            firstName = name;

            int position = intent.getIntExtra("position", 0);
            imageView.setImageBitmap(MainActivity.artImageList.get(position));
            saveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.VISIBLE);
        }

    }

    public void saveData(View view) {

        String artName = editText.getText().toString();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
        byte[] bytes = outputStream.toByteArray();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ArtContentProvider.NAME, artName);
        contentValues.put(ArtContentProvider.IMAGE, bytes);

        getContentResolver().insert(ArtContentProvider.CONTENT_URI, contentValues);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    public void updateData(View view) {

        String artName = editText.getText().toString();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] bytes = outputStream.toByteArray();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ArtContentProvider.NAME,artName);
        contentValues.put(ArtContentProvider.IMAGE,bytes);

        String[] selectionArgs = {firstName};

        getContentResolver().update(ArtContentProvider.CONTENT_URI,contentValues,"name=?",selectionArgs);

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    public void deleteData(View view) {

        String recordName = editText.getText().toString();


        String[] selectionArgs = {recordName};

        getContentResolver().delete(ArtContentProvider.CONTENT_URI, "name=?", selectionArgs);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }


    public void selectImage(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            Uri image = data.getData();

            try {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                imageView.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
