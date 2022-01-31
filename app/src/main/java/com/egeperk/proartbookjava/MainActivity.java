package com.egeperk.proartbookjava;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    static ArrayList<Bitmap> artImageList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.art_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_art) {
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = findViewById(R.id.list_view);

        ArrayList<String> artNameList = new ArrayList<>();
        artImageList = new ArrayList<>();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,artNameList);
        listview.setAdapter(arrayAdapter);

        String Url = "content://com.egeperk.proartbookjava.ArtContentProvider";
        Uri artUri = Uri.parse(Url);

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(artUri,null,null,null,"name");

        if (cursor != null) {

            while (cursor.moveToNext()) {
                artNameList.add(cursor.getString(cursor.getColumnIndex(ArtContentProvider.NAME)));
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(ArtContentProvider.IMAGE));
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                artImageList.add(image);

                arrayAdapter.notifyDataSetChanged();
            }
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
                intent.putExtra("info","old");
                intent.putExtra("name", artNameList.get(position));
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }





































}