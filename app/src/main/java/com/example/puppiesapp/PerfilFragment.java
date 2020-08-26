package com.example.puppiesapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {
   FirebaseAuth firebaseAuth;
   FirebaseUser user;
   FirebaseDatabase firebaseDatabase;
   DatabaseReference databaseReference;

   ImageView avatarTv,coverIv;
   TextView userTv, emailTv,tipouserTv,phoneTv;
   FloatingActionButton fab;
ProgressDialog pd;

private  static  final int CAMERA_REQUEST_CODE = 100;
    private  static  final int STORAGE_REQUEST_CODE = 200;
    private  static  final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
    private  static  final int IMAGE_PICK_CAMERAREQUEST_CODE = 400;

    String cameraPermissions[];
    String storagePermissions[];





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        View view =  inflater.inflate(R.layout.fragment_perfil, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
     firebaseDatabase = FirebaseDatabase.getInstance();
     databaseReference = firebaseDatabase.getReference("Users");

     cameraPermissions = new  String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};


     avatarTv = view.findViewById(R.id.avatarIv);
        userTv = view.findViewById(R.id.userTv);
        tipouserTv = view.findViewById(R.id.tipouserTv);
         fab = view.findViewById(R.id.fab);
pd = new ProgressDialog(getActivity());

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for (DataSnapshot ds:dataSnapshot.getChildren() ){
                  String user = ""+ds.child("name").getValue();
                  String tipouser = ""+ds.child("tipo_de_user").getValue();
                  String image = ""+ds.child("image").getValue();

                  userTv.setText(user);
                  tipouserTv.setText(tipouser);

                  try{
                      Picasso.get().load(image).into(avatarTv);
                  }catch (Exception e)
                  {
                      Picasso.get().load(R.drawable.ic_addphoto).into(avatarTv);
                  }


              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditProfile();
            }


        });

        return view;
    }

    private void showEditProfile() {
        String options[]= {"Editar foto de perfil","Editar nome de usuário","Editar descrição"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Editar");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
               pd.setMessage("Atualizando foto de perfil");
              showImagePicDialog();
                }else if (i ==1)
                {
                    pd.setMessage("Atualizando nome de usuário");
                }else if (i ==2)
                {
                    pd.setMessage("Atualizando descrição");
                }
            }


        });

        builder.create().show();

    }

    private void showImagePicDialog() {

        String options[]= {"Câmera","Galeria"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Escolher a imagem de");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){

                }else if (i ==1)
                {

                }
            }


        });

        builder.create().show();
    }
}