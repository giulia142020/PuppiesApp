package com.example.puppiesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {
  FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar= findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
actionBar = getSupportActionBar();
actionBar.setTitle("");
     firebaseAuth = FirebaseAuth.getInstance();

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

       // actionBar.setTitle("Perfil");
        PerfilFragment fragment1 = new PerfilFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment1,"");
        ft1.commit();

    }
 private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
         new BottomNavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                 switch (item.getItemId()){


                     case R.id.nav_perfil:

                         PerfilFragment fragment1 = new PerfilFragment();
                         FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                         ft1.replace(R.id.content,fragment1,"");
                         ft1.commit();
                         return true;
                     case R.id.nav_home:

                         HomeFragment fragment2 = new HomeFragment();
                         FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                         ft2.replace(R.id.content,fragment2,"");
                         ft2.commit();
                         return true;

                     case R.id.nav_perdidos:

                         LostFragment fragment4 = new LostFragment();
                         FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                         ft4.replace(R.id.content,fragment4,"");
                         ft4.commit();
                         return true;

                     case R.id.nav_chat:

                         ChatFragment fragment3 = new ChatFragment();
                         FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                         ft3.replace(R.id.content,fragment3,"");
                         ft3.commit();
                         return true;

                 }

                 return false;
             }
         };

    private  void  checkUserStatus()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
           // mperfilTv.setText(user.getEmail());


        }else{
           startActivity(new Intent(DashboardActivity.this,MainActivity.class));
           finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       int id = item.getItemId();
       if(id== R.id.action_logout)
       {
           firebaseAuth.signOut();
           checkUserStatus();
       }
       if(id==R.id.action_aboutus){
           Intent intent = new Intent(this, AboutUsActivity.class);
           startActivity(intent);
       }
        return super.onOptionsItemSelected(item);
    }


}