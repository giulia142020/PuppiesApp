package com.example.puppiesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
    }
    public void Voltar (View view){

        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
    public void intencao (View view){
        if(view.getId()==R.id.batalhao)
        {

            Uri uri = Uri.parse("tel:32148904");
            Intent it = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(it);
        }
        if(view.getId()==R.id.ccz)
        {

            Uri uri = Uri.parse("tel:36252655");
            Intent it = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(it);
        }
        if(view.getId()==R.id.delegacia)
        {

            Uri uri = Uri.parse("tel:30871943");
            Intent it = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(it);
        }
        if(view.getId()==R.id.semmas)
        {

            Uri uri = Uri.parse("tel:32367060");
            Intent it = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(it);
        }
        if(view.getId()==R.id.ibama)
        {

            Uri uri = Uri.parse("tel:38787150 ");
            Intent it = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(it);
        }
    }
}