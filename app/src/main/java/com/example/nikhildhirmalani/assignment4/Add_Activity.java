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
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Add_Activity extends AppCompatActivity {

    //variables declared
    private static final int CAMERA_REQUEST = 1888;
    ImageView mimageView;;
    Button buttonView;
    LinearLayout layout;
    EditText Category;
    FloatingActionButton fab,add_data;
    LinearLayout layout2,a;
    DatabaseReference firebase;
    public static final String FIREBASE_CHILD_PRODUCTS = "recipes";
    EditText title,description,ingredients,serve,cooktime,preptime,summary,url,text,text2;
    RatingBar rating;
    Data_Class new_data;
    List<String>str;
    String x,y;
    int i=0;
    RatingBar rb;

    private StorageReference mStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //variables initialized
        setContentView(R.layout.activity_add_);
        Button button = (Button) this.findViewById(R.id.take_image_from_camera);
        Category=findViewById(R.id.Category);
        layout2=findViewById(R.id.llayout);
        new_data=new Data_Class();
        fab=findViewById(R.id.fab_ingrident);
        title=findViewById(R.id.Title);
        description=findViewById(R.id.Description);
        cooktime=findViewById(R.id.cooktime);
        preptime=findViewById(R.id.Preptime);
        url=findViewById(R.id.url);
        serve=findViewById(R.id.Serves);
        ingredients=findViewById(R.id.Ingredients);
        summary=findViewById(R.id.Summary);
        add_data=findViewById(R.id.add_data);
        mStorage = FirebaseStorage.getInstance().getReference();
        rb=findViewById(R.id.ratingBar);

        str=new ArrayList<>();
        fab.setOnClickListener(new View.OnClickListener() {//fab button on click listner
        int i=0;
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                if(i==0)
                {
                    str.add(ingredients.getText().toString());
                }
                a = new LinearLayout(Add_Activity.this);
                a.setOrientation(LinearLayout.HORIZONTAL);
                if(i>0)
                {
                    str.add(text2.getText().toString());
                }
                text=new EditText(Add_Activity.this);
                text.setTextColor(Color.BLACK);
                text.setHint("Ingredients");
                text.setWidth(Category.getWidth()-100);
                Typeface face= Typeface.createFromAsset(getAssets(), "Acme-Regular.ttf");
                text.setTypeface(face);
                text2=text;
                final FloatingActionButton fab_but=new FloatingActionButton(Add_Activity.this);
                fab_but.setClickable(false);
                i=i+1;
                fab_but.setSize(FloatingActionButton.SIZE_MINI);
                fab_but.setImageResource(R.mipmap.ic_remove_white_24dp);
                fab_but.setBackgroundColor(Color.RED);
                a.addView(text);
                a.addView(fab_but);
                layout2.addView(a);
                fab_but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout l= (LinearLayout) view.getParent();
                        l.setVisibility(View.GONE);
                    }
                });

            }
        });


        Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence items[] = new CharSequence[] {"Appetizer", "Dinner", "Lunch"};

                AlertDialog.Builder builder = new AlertDialog.Builder(Add_Activity.this,R.style.AlertDialogStyle);
                builder.setTitle("Please select Category");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Category.setText(items[which]);
                    }
                });
                builder.show();
            }
        });
        firebase=FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_PRODUCTS);

        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new_data.setSummary(description.getText().toString());
                new_data.setRating((int) rb.getRating());
                new_data.setCook_time(Integer.parseInt(cooktime.getText().toString()));
                new_data.setPrep_time(Integer.parseInt(preptime.getText().toString()));
                new_data.setServes(Integer.parseInt(serve.getText().toString()));
                new_data.setDirections(summary.getText().toString());
                new_data.setName(title.getText().toString());
                int c = Integer.parseInt(getIntent().getStringExtra("df"));
                DatabaseReference k = firebase.child(new_data.getName()).child("image");
                uploadFile(k);
                new_data.setCategory(Category.getText().toString());
                new_data.setIngredients(str);
                new_data.setUrl(url.getText().toString());
                firebase.child(new_data.getName()).setValue(new_data);
                Toast.makeText(getApplicationContext(),"Data Added Successfully Go Back",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void takeImageFromCamera(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            layout= (LinearLayout) findViewById(R.id.layout);
            mimageView = new ImageView(this);
            mimageView.setImageBitmap(mphoto);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(500,500
            );
            layout.addView(mimageView, p);
            buttonView= new Button(this);
            buttonView.setText("Delete");
            buttonView.setOnClickListener(mThisButtonListener);
            LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(100,100
            );
            layout.addView(buttonView, p2);
        }
    }
    private void uploadFile(final DatabaseReference k) {


        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference strref = storageRef.child("images/" + new_data.getName() + ".jpg");
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
        private View.OnClickListener mThisButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
           mimageView.setImageBitmap(null);
            layout.removeView(buttonView);
            layout.removeView(mimageView);
        }
    };
}
