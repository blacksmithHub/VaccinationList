package com.torres.edgar.vaccinationlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    ListView lstviewBreed,lstviewStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        lstviewBreed = (ListView) findViewById(R.id.listBreed);
        lstviewStatus = (ListView) findViewById(R.id.listStatus);

        receiveData();


    }

    private void receiveData(){

        Intent i=this.getIntent();
        DoginfoCollection doginfoBreed = (DoginfoCollection) i.getSerializableExtra("DOGBREED");
        DoginfoCollection doginfoStatus = (DoginfoCollection) i.getSerializableExtra("STATUS");
        lstviewBreed.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, doginfoBreed.getDoginfo()));
        lstviewStatus.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,doginfoStatus.getDoginfo()));

    }




    public void onBackPressed(){
        super.onBackPressed();
        Toast.makeText(getApplicationContext(),"Asta la Vista",Toast.LENGTH_LONG).show();
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }


}
