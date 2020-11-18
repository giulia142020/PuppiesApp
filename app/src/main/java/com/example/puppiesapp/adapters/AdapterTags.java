package com.example.puppiesapp.adapters;

import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puppiesapp.R;

public class AdapterTags extends RecyclerView.ViewHolder {

    public Button tagBtn;

    public AdapterTags (View itemView){
        super(itemView);

        tagBtn = (Button) itemView.findViewById(R.id.tagbtn);
    }

}
