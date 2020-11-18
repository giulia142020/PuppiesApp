package com.example.puppiesapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.puppiesapp.adapters.AdapterLost;
import com.example.puppiesapp.adapters.AdapterPosts;
import com.example.puppiesapp.models.ModelLost;
import com.example.puppiesapp.models.ModelPost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
 * Use the {@link LostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LostFragment extends Fragment {
    FloatingActionButton fab;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelLost> postList;
    AdapterLost adapterLost;
    ImageView btnFilter;
    ArrayList<String> tagsPerfil;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LostFragment newInstance(String param1, String param2) {
        LostFragment fragment = new LostFragment();
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
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_lost, container, false);
        fab = view.findViewById(R.id.hfab);

        tagsPerfil = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        btnFilter = view.findViewById(R.id.filterBtn);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterPopup();
            }
        });


        recyclerView = view.findViewById(R.id.postsRecyclerview);
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
                    startActivity(new Intent(act, LostActivity.class));
                }
            }
        });
        return  view;
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
                if(femeaRBFilter.isChecked())tagsPerfil.add("#femea");
                if(machoRBFilter.isChecked())tagsPerfil.add("#macho");
                if(caoRBFilter.isChecked())tagsPerfil.add("#cao");
                if(gatoRBFilter.isChecked())tagsPerfil.add("#gato");
                if(claroRBFilter.isChecked())tagsPerfil.add("#claro");
                if(escuroRBFilter.isChecked())tagsPerfil.add("#escuro");
                if(mesticaRBFilter.isChecked())tagsPerfil.add("#mestico");
                loadPostsOneTime();
                alert.dismiss();
            }
        });


    }

    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("PostsLost");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelLost modelLost = ds.getValue(ModelLost.class);
                    Boolean valid = true;
                    for(String tag : tagsPerfil){
                        if(!modelLost.getpTags().contains(tag)) valid = false;
                    }
                    if(valid) postList.add(modelLost);
                    adapterLost = new AdapterLost(getActivity(),postList);
                    recyclerView.setAdapter(adapterLost);
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("PostsLost");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelLost modelLost = ds.getValue(ModelLost.class);
                    Boolean valid = true;
                    for(String tag : tagsPerfil){
                        if(!modelLost.getpTags().contains(tag)) valid = false;
                    }
                    if(valid) postList.add(modelLost);
                    adapterLost = new AdapterLost( getActivity(),postList);
                    recyclerView.setAdapter(adapterLost);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}