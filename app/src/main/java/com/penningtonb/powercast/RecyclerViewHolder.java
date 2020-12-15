package com.penningtonb.powercast;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    Button rowButton;
    TextView rowText;
    ImageView rowImage;
    private ConstraintLayout view;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.fragSearch);
        rowText = (TextView) view.findViewById(R.id.searchResultRowTextBox);
        rowButton = (Button) view.findViewById(R.id.searchResultRowButton);
        rowImage = (ImageView) view.findViewById(R.id.searchResultRowImageView);
    }

    public View getView(){
        return view;
    }
    public TextView getRowText() {return rowText;}
    public Button getRowButton() {return rowButton;}
    public ImageView getRowImage() {return rowImage;}
}

