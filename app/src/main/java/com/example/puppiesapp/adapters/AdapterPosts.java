package com.example.puppiesapp.adapters;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puppiesapp.ChatActivity;
import com.example.puppiesapp.R;
import com.example.puppiesapp.ThereProfileActivity;
import com.example.puppiesapp.models.ModelPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {
     Context context;
     List<ModelPost> postList;

      String myUid;
    public AdapterPosts(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        final String hisUID = postList.get(position).getUid();
        final String uid = postList.get(position).getUid();
        String uEmail = postList.get(position).getuEmail();
        String uName = postList.get(position).getuName();
        String uDp = postList.get(position).getuDp();
        final String pId = postList.get(position).getpId();
        String pNome_do_animal = postList.get(position).getpNome_do_animal();
        String uRaca = postList.get(position).getuRaca();
        final String pImage = postList.get(position).getpImage();
        String pTimeStamp= postList.get(position).getpTime();
        String pTags = postList.get(position).getpTags();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();


        holder.uNameTv.setText(uName);
        holder.pTimeTv.setText(pTime);
        holder.pNome_do_animalTv.setText(pNome_do_animal);
        holder.pRacaTv.setText("Raça: "+uRaca);

        String tipo = "Tipo: ";
        String sexo = "Sexo: ";
        String cor = "Cor: ";
        if(pTags.contains("#cao")) tipo += "Cão";
        else if(pTags.contains("#gato")) tipo += "Gato";
        if(pTags.contains("#femea")) sexo += "Fêmea";
        else if(pTags.contains("macho")) sexo += "Macho";
        if(pTags.contains("#claro")) cor += "Claro";
        else if(pTags.contains("#escuro")) cor += "Escuro";
        else if(pTags.contains("#mestico")) cor += "Mestiça";

        holder.pTipoTv.setText(tipo);
        holder.pSexoTv.setText(sexo);
        holder.pCorTv.setText(cor);



        try {
            Picasso.get().load(uDp).placeholder(R.drawable.ic_launcher_foreground).into(holder.uPictureIv);

        }catch (Exception e){

        }
             if(pImage.equals("noImage")){

                 holder.pImageIv.setVisibility(View.GONE);

             }else{
                 try {
                     Picasso.get().load(pImage).into(holder.pImageIv);

                 }catch (Exception e){

                 }
             }




        holder.adotarBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid",hisUID);
                context.startActivity(intent);
            }
        });

             holder.moreBtn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     showMoreOptions(holder.moreBtn, uid , myUid, pId,pImage);
                 }
             });
holder.profileLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {



                Intent intent  = new Intent(context, ThereProfileActivity.class);
                intent.putExtra("uidemail",uid);
                context.startActivity(intent);


    }
});

    }

    private void showMoreOptions(ImageButton  moreBtn, String uid, String myUid, final String pId, final String pImage) {

        PopupMenu popupMenu = new PopupMenu(context,moreBtn, Gravity.END);


        if(uid.equals(myUid)){
            popupMenu.getMenu().add(Menu.NONE,0,0,"Excluir");
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == 0){
                  beginDelete(pId,pImage);
                }
                return false;
            }
        });

        popupMenu.show();
    }

    private void beginDelete(String pId, String pImage) {

        if(pImage.equals("noImage")){

        }else{
            deleteWithImage(pId,pImage);
        }

    }

    private void deleteWithImage(final String pId, String pImage) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Excluindo...");

        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Query fquery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
                fquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                          for(DataSnapshot ds: snapshot.getChildren()){
                              ds.getRef().removeValue();

                          }
                        Toast.makeText(context, "Excluido com Sucesso", Toast.LENGTH_SHORT).show();
                          pd.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            pd.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class  MyHolder extends RecyclerView.ViewHolder{

        ImageView uPictureIv, pImageIv;
        TextView uNameTv, pTimeTv, pNome_do_animalTv, pRacaTv, pTipoTv, pSexoTv, pCorTv;
            Button adotarBnt;
            ImageButton moreBtn;
        LinearLayout profileLayout;

        public  MyHolder(@NonNull View itemView){
            super(itemView);
            uPictureIv = itemView.findViewById(R.id.uPictureIv);
            pImageIv = itemView.findViewById(R.id.pImageIV);
            uNameTv = itemView.findViewById(R.id.uNameTv);
            pTimeTv = itemView.findViewById(R.id.pTimeTv);
            pNome_do_animalTv = itemView.findViewById(R.id.pNome_animalTV);
            pRacaTv = itemView.findViewById(R.id.pRacaTV);
            pTipoTv = itemView.findViewById(R.id.pTipoTV);
            pSexoTv = itemView.findViewById(R.id.pSexoTV);
            pCorTv = itemView.findViewById(R.id.pCorTV);
            adotarBnt =itemView.findViewById(R.id.quero_adotarBtn);
            moreBtn = itemView.findViewById(R.id.morebtn);
            profileLayout =itemView.findViewById(R.id.profileLayout);


        }
    }
}
