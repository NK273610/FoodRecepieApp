package com.example.nikhildhirmalani.assignment4;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    EditText ingredients,Category,desc,summ,cooktime,prep,serve;
    LinearLayout a,linlayout,layout;
    private static final int CAMERA_REQUEST = 1888;
    List<String> str;
    ImageView mimageView;
    Button buttonView;
    Button edit;
    RatingBar rb;
    Data_Class obj;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ingredients=findViewById(R.id.ingredient);
        layout=findViewById(R.id.imlayout);
        desc=findViewById(R.id.desc);
        summ=findViewById(R.id.summ);
        cooktime=findViewById(R.id.cook);
        prep=findViewById(R.id.prep);
        serve=findViewById(R.id.serv);
        mimageView = new ImageView(this);
        edit=findViewById(R.id.edit_done);
        Intent intent=getIntent();
        obj=  (Data_Class) intent.getSerializableExtra("DataObj");
        Category=findViewById(R.id.category);
        rb=findViewById(R.id.rb);
        Category.setText(obj.getCategory());
        str=new ArrayList<>();
        final CharSequence[] items = {" Easy "," Medium "," Hard "};
        final ArrayList seletedItems=new ArrayList();
        linlayout=findViewById(R.id.linlayout);
        desc.setText(obj.getSummary());
        summ.setText(obj.getDirections());
        cooktime.setText(String.valueOf(obj.getCook_time()));
        prep.setText(String.valueOf(obj.getPrep_time()));
        serve.setText(String.valueOf(obj.getServes()));
        rb.setRating(obj.getRating());
        Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog dialog = new AlertDialog.Builder(EditActivity.this)
                        .setTitle("Select The Difficulty Level")
                        .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    seletedItems.add(indexSelected);
                                } else if (seletedItems.contains(indexSelected)) {
                                    // Else, if the item is already in the array, remove it
                                    seletedItems.remove(Integer.valueOf(indexSelected));
                                }
                            }
                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on OK
                                //  You can write the code  to save the selected item here
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on Cancel
                            }
                        }).create();
                dialog.show();

            }
        });
        ingredients.setText(obj.getIngredients().get(0));
        str.add(ingredients.getText().toString());
        for(int i=1;i<obj.getIngredients().size();i++) {
            a = new LinearLayout(EditActivity.this);
            a.setOrientation(LinearLayout.HORIZONTAL);
            final EditText text = new EditText(EditActivity.this);
            text.setWidth(900);
            text.setText(obj.getIngredients().get(i));
            text.setTextColor(Color.BLACK);
            Typeface face= Typeface.createFromAsset(getAssets(), "Acme-Regular.ttf");
            text.setTypeface(face);
            final FloatingActionButton fab_but = new FloatingActionButton(EditActivity.this);
            fab_but.setClickable(false);
            fab_but.setSize(FloatingActionButton.SIZE_MINI);
            fab_but.setImageResource(R.mipmap.ic_remove_white_24dp);

            a.addView(text);
            a.addView(fab_but);
            linlayout.addView(a);

            fab_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LinearLayout l = (LinearLayout) view.getParent();
                    l.setVisibility(View.GONE);
                    str.add(text.getText().toString());
                }
            });
        }

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(500,500);
        layout.addView(mimageView, p);
        Picasso.get().load(obj.getImage()).into(mimageView);
        buttonView= new Button(this);
        buttonView.setText("Delete");
        buttonView.setOnClickListener(mThisButtonListener);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(100,100
        );
        layout.addView(buttonView, p2);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data_Class newob=new Data_Class();
                newob.setName(obj.getName());
                DatabaseReference k = FirebaseDatabase.getInstance().getReference("recipes").child(obj.getName()).child("image");
                uploadFile(k);
                newob.setServes(Integer.parseInt(serve.getText().toString()));
                newob.setRating((int) rb.getRating());
                newob.setCook_time(Integer.parseInt(cooktime.getText().toString()));
                newob.setPrep_time(Integer.parseInt(prep.getText().toString()));
                newob.setCategory(Category.getText().toString());
                newob.setDirections(summ.getText().toString());
                newob.setSummary(desc.getText().toString());
                newob.setIngredients(str);
                FirebaseDatabase.getInstance().getReference("recipes").child(obj.getName()).removeValue();

                FirebaseDatabase.getInstance().getReference("recipes").child(obj.getName()).setValue(newob);

                Toast.makeText(getApplicationContext(),"Data Editted Successfully Go Back",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void uploadFile(final DatabaseReference k) {


        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference strref = storageRef.child("images/" + obj.getName() + ".jpg");
        mimageView.setDrawingCacheEnabled(true);
        mimageView.buildDrawingCache();
        Bitmap bitmap = mimageView.getDrawingCache();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = strref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                k.setValue(taskSnapshot.getDownloadUrl().toString());


            }
        });


    }

    public void takeImageCamera(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            layout= (LinearLayout) findViewById(R.id.layout);
            mimageView.setImageBitmap(mphoto);

        }
    }

    private View.OnClickListener mThisButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            mimageView.setImageBitmap(null);
//            layout.removeView(buttonView);
//            layout.removeView(mimageView);
        }
    };
}
