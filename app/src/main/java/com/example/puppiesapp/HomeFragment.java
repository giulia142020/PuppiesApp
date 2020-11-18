package com.example.puppiesapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puppiesapp.adapters.AdapterPosts;
import com.example.puppiesapp.models.ModelPost;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    FirebaseAuth firebaseAuth;
 RecyclerView recyclerView;
 List<ModelPost> postList;
 TextView chips,chips2,chips3;
 AdapterPosts adapterPosts;
    FloatingActionButton fab;
    ImageView btnFilter;
    ArrayList<String> tagsPerfil;
    int i=0,femea=0, macho=0;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        tagsPerfil = new ArrayList<>();
            firebaseAuth = FirebaseAuth.getInstance();
        fab = view.findViewById(R.id.hfab);
        btnFilter = view.findViewById(R.id.filterBtn);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=0;
                femea=0;
                macho=0;
                showFilterPopup();
            }
        });


        recyclerView = view.findViewById(R.id.postsRecyclerview);
        chips = view.findViewById(R.id.chipfalso);
        chips2 = view.findViewById(R.id.chipfalso2);
        chips3 = view.findViewById(R.id.chipfalso3);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);
        postList = new ArrayList<>();
        
        loadPosts();

        
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentActivity act = getActivity();

                        if (act != null) {
                            startActivity(new Intent(act, AddPostActivity.class));
                        }
                    }
                });
        return view;

    }

    public void showFilterPopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        View mView = getLayoutInflater().inflate(R.layout.activity_popup_filter,null);

        builder.setView(mView);

        final AlertDialog alert = builder.create();
        alert.show();

        Button btnOk = mView.findViewById(R.id.btnOkFilter);
        final RadioButton femeaRBFilter = mView.findViewById(R.id.femeaRBFilter);
        final RadioButton machoRBFilter = mView.findViewById(R.id.machoRBFilter);
        final RadioButton gatoRBFilter = mView.findViewById(R.id.gatoRBFilter);
        final RadioButton caoRBFilter = mView.findViewById(R.id.caoRBFilter);
        final RadioButton claroRBFilter = mView.findViewById(R.id.claroRBFilter);
        final RadioButton escuroRBFilter = mView.findViewById(R.id.escuroRBFilter);
        final RadioButton mesticaRBFilter = mView.findViewById(R.id.mesticaRBFilter);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsPerfil.clear();
                if(femeaRBFilter.isChecked()){

                    tagsPerfil.add("#femea");
                    if (i==0){
                        femea=femea+1;
                        i=i+1;
                        chips.setVisibility(View.VISIBLE);
                        chips.setText("Fêmea X");
                        chips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                chips.setVisibility(View.INVISIBLE);
                                loadPostsOneTime();
                                 tagsPerfil.clear();

                            }
                        });
                    }


                }
                if(machoRBFilter.isChecked()){

                    tagsPerfil.add("#macho");

                    if (i==0){
                        macho = macho+1;
                        i=i+1;
                        chips.setVisibility(View.VISIBLE);
                        chips.setText("Macho X");
                        chips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                chips.setVisibility(View.INVISIBLE);
                                loadPostsOneTime();
                                tagsPerfil.clear();

                            }
                        });
                    }

                }

                if(caoRBFilter.isChecked()){
                    tagsPerfil.add("#cao");

                    if (i==0){
                        chips.setVisibility(View.VISIBLE);
                        chips.setText("Cão X");
                        chips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadPostsOneTime();

                                chips.setVisibility(View.INVISIBLE);


                            }
                        });
                    }
                    if(i==1){
                        i=i+1;
                        chips2.setVisibility(View.VISIBLE);
                        chips2.setText("Cão X");
                        chips2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                chips2.setVisibility(View.INVISIBLE);



                            }
                        });
                    }

                }
                if(gatoRBFilter.isChecked()){
                    tagsPerfil.add("#gato");
                    if (i==0){

                        chips.setVisibility(View.VISIBLE);
                        chips.setText("Gato X");
                        chips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadPostsOneTime();
                                chips.setVisibility(View.INVISIBLE);

                                tagsPerfil.clear();

                            }
                        });
                    }
                    if(i==1){
                        i=i+1;
                        chips2.setVisibility(View.VISIBLE);
                        chips2.setText("Gato X");
                        chips2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(femea==1) {
                                    tagsPerfil.add("#femea");

                                }
                                if (macho==1){
                                    tagsPerfil.add("#macho");

                                }
                                chips2.setVisibility(View.INVISIBLE);

                                tagsPerfil.clear();

                            }
                        });
                    }

                }
                if(claroRBFilter.isChecked()){
                    tagsPerfil.add("#claro");
                    if (i==0){
                        i=i+1;
                        chips.setVisibility(View.VISIBLE);
                        chips.setText("Claro X");
                        chips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                chips.setVisibility(View.INVISIBLE);
                                loadPostsOneTime();
                                tagsPerfil.clear();

                            }
                        });
                    }

                }
                if(escuroRBFilter.isChecked()){
                    tagsPerfil.add("#escuro");
                    if (i==0){
                        i=i+1;
                        chips.setVisibility(View.VISIBLE);
                        chips.setText("Escuro X");
                        chips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                chips.setVisibility(View.INVISIBLE);
                                loadPostsOneTime();
                                tagsPerfil.clear();

                            }
                        });
                    }

                }
                if(mesticaRBFilter.isChecked()){
                    tagsPerfil.add("#mestico");
                    if (i==0){
                        i=i+1;
                        chips.setVisibility(View.VISIBLE);
                        chips.setText("Mestiço X");
                        chips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                chips.setVisibility(View.INVISIBLE);
                                loadPostsOneTime();
                                tagsPerfil.clear();

                            }
                        });
                    }

                }
                loadPostsOneTime();
                alert.dismiss();
            }
        });


    }


    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               postList.clear();
               for(DataSnapshot ds: snapshot.getChildren()){
                   ModelPost modelPost = ds.getValue(ModelPost.class);
                   Boolean valid = true;
                   for(String tag : tagsPerfil){
                       if(!modelPost.getpTags().contains(tag)) valid = false;
                   }
                   if(valid) postList.add(modelPost);
                   adapterPosts = new AdapterPosts(getActivity(),postList);
                   recyclerView.setAdapter(adapterPosts);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void checkUserStatus(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){

        }else{
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }
    }

    private void loadPostsOneTime(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    Boolean valid = true;
                    for(String tag : tagsPerfil){
                        if(!modelPost.getpTags().contains(tag)) valid = false;
                    }
                    if(valid) postList.add(modelPost);
                    adapterPosts = new AdapterPosts(getActivity(),postList);
                    recyclerView.setAdapter(adapterPosts);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }






}

