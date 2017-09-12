package com.mobile_computing;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.Arrays;


public class ResultDisplayActivity extends AppCompatActivity implements View.OnClickListener
    {
    SharedPreferences sp;
    String title,id,date,text,image;
    Button favBut;
    boolean isfav=false;
    ArrayList<String> fList;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title=getIntent().getStringExtra("title");
        id=getIntent().getStringExtra("id");
        text=getIntent().getStringExtra("text");
        date=getIntent().getStringExtra("date");
        image=getIntent().getStringExtra("img");

        TextView tv = (TextView)findViewById(R.id.title);
        tv.setText(title);
        TextView tv2= (TextView)findViewById(R.id.text);
        tv2.setText(text);
        TextView tv3= (TextView)findViewById(R.id.date);
        tv3.setText(date);

        NetworkImageView img   = (NetworkImageView) findViewById(R.id.image);
        ImageLoader imgLoad=VolleySingleton.getInstance(this).getImageLoader();
        img.setImageUrl(image, imgLoad);

        setTitle("Book: "+title);

        favBut=(Button)findViewById(R.id.starButton);
        favBut.setOnClickListener(this);

        sp= getSharedPreferences("Fav", MODE_PRIVATE);
        String list = sp.getString("list", "");
        //Toast.makeText(this, "List is: " + list, Toast.LENGTH_SHORT).show();
        String[] fArray = list.split(",");
        fList = new ArrayList<String>(Arrays.asList(fArray));
        if(fList.contains(id))
        {
            isfav = true;
            ((TextView) findViewById(R.id.starButton)).setText("Remove");
        }
    }
    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = sp.edit();

        //Two cases.
        if(isfav)  //Set it to False after the click. Change text, and remove the SharedPreference String.
        {
            isfav = false;
            favBut.setText("Save");
            //editor.putString(id, null);
            fList.remove(id);
            editor.remove(id);
            //Toast.makeText(ResultDisplayActivity.this, "Removing ID " + id, Toast.LENGTH_SHORT).show();
        }
        else        //Add Favorite
        {
            isfav = true;
            favBut.setText("Remove");
            fList.add(id);
        }

        String string="";
        for(String i: fList)
            {
            string=string+i+",";
            }
        editor.putString("list", string.substring(0,string.length()-1));

        editor.commit();
    }
    }
