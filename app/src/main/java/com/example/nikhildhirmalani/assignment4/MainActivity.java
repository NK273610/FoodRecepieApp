package com.example.nikhildhirmalani.assignment4;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView rv;
    FloatingActionButton fab;
    Toolbar tool;
    FireBase_Fetcher df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab=(FloatingActionButton) findViewById(R.id.fab);
        df=new FireBase_Fetcher();
        rv= findViewById(R.id.my_recycler_view);
        tool =findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        df.FireBase_Data(rv);
//        Toast.makeText(getApplicationContext(),String.valueOf(c),Toast.LENGTH_SHORT).show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Add_Activity.class);
                intent.putExtra("df", String.valueOf(df.count));
//                intent.putExtra("count",String.valueOf(c));
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_card, menu);
        MenuItem menuitem=menu.findItem(R.id.action_search);
        SearchView searchview= (SearchView) MenuItemCompat.getActionView(menuitem);
        searchview.setOnQueryTextListener(this);

        return true;

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        s= s.toLowerCase();
        Log.e("TAG",String.valueOf(df.list.size()));
        ArrayList<Data_Class> newlist= (ArrayList<Data_Class>) df.list;
        ArrayList<Data_Class> newlist2= new ArrayList<>();

        for(Data_Class dt:newlist)
        {
            String name=dt.getName().toLowerCase();
            if(name.contains(s))
            {
                newlist2.add(dt);


            }
        }

        df.cv.setFilter(newlist2);


        return false;
    }
}
