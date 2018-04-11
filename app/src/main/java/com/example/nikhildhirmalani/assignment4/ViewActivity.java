package com.example.nikhildhirmalani.assignment4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ViewActivity extends AppCompatActivity  {

    TextView description,ingredient,category,direction,serve,prep,cooktime,name,url;
    ImageView img;
    RatingBar rb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Intent intent=getIntent();
        Data_Class obj=  (Data_Class) intent.getSerializableExtra("DataObj");
        description=findViewById(R.id.des);
        ingredient=findViewById(R.id.ingri);
        rb=findViewById(R.id.rating);
        url=findViewById(R.id.Url);
        url.setText(obj.getUrl());
        category=findViewById(R.id.cat);
        img=findViewById(R.id.imgview);
        description.setText(obj.getSummary());
        direction=findViewById(R.id.direc);
        serve=findViewById(R.id.ser);
        prep=findViewById(R.id.pt);
        cooktime=findViewById(R.id.ct);
        name=findViewById(R.id.text_name);
        name.setText(obj.getName());
        Picasso.get().load(obj.getImage()).into(img);
        rb.setRating(obj.getRating());
        for(int i=0;i<obj.getIngredients().size();i++)
        {
            ingredient.append("  "+obj.getIngredients().get(i));
        }
        category.setText(obj.getCategory());
        direction.setText(obj.getDirections());
        prep.setText(String.valueOf(obj.getPrep_time()));
        cooktime.setText(String.valueOf(obj.getCook_time()));
        serve.setText(String.valueOf(obj.getServes()));
        Linkify.addLinks(url, Linkify.ALL);

    }
}
