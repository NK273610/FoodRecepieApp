package com.example.nikhildhirmalani.assignment4;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nikhildhirmalani on 23/03/18.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Data_Class> dataSet;
    private Context c;
    int count=0;

    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName;
        ImageView imageViewIcon;
        Button but,edit;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
            this.but=(Button)itemView.findViewById(R.id.Delete);
            this.edit=(Button)itemView.findViewById(R.id.Edit);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Data_Class obj= dataSet.get(getAdapterPosition());//we take the url
            Intent intent = new Intent(itemView.getContext(),ViewActivity.class);//intent used to go to this activity to show url
            intent.putExtra("DataObj", obj);
            itemView.getContext().startActivity(intent);// start activity this code is to send data from fragment to activity
        }
    }

    public CustomAdapter(List<Data_Class> data) {
        this.dataSet = data;
        this.c=c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        final Data_Class dc=dataSet.get(listPosition);
        ImageView imageView = holder.imageViewIcon;
        textViewName.setText(dataSet.get(listPosition).getName());
        PicassoClient.downloadImage(c,dataSet.get(listPosition).getImage(),imageView);

        holder.but.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                        // setup the alert builder
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.AlertDialogStyle);
                        builder.setTitle("Notice");

                        builder.setMessage("It will permanently remove your recepie. Is this what you intended to do?");

                        // add the buttons
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dataSet.remove(holder.getAdapterPosition());
                                FirebaseDatabase.getInstance().getReference("recipes").child(dataSet.get(holder.getAdapterPosition()).getName()).removeValue();
                                notifyItemRemoved(listPosition);
                                 notifyItemRangeChanged(0,dataSet.size());
                            }
                        });
                        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });

                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();

                        dialog.show();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Data_Class obj= dataSet.get(listPosition);//we take the url
                Intent intent = new Intent(view.getContext(),EditActivity.class);//intent used to go to this activity to show url
                intent.putExtra("DataObj", obj);
                view.getContext().startActivity(intent);// st

            }
        });


    }

public void setFilter(ArrayList<Data_Class> newlist)
{
    dataSet=new ArrayList<>();
    dataSet.addAll(newlist);
    notifyDataSetChanged();

}

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

class PicassoClient {

    public static void downloadImage(Context c,String url,ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Picasso.get().load(url).placeholder(R.drawable.placeholder).into(img);
        }else {

        }
    }
}

