package com.torres.edgar.vaccinationlist;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.sqlitelib.DataBaseHelper;
import com.sqlitelib.SQLite;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    Button btnSend, btnAddDoginfo;
    ArrayList dogbreedinfo =new ArrayList();
    ArrayList dogstatusinfo =new ArrayList();
    EditText txtDogname;
    Spinner spinBreed;
    Switch swVaccinated;
    TextView txtVaccinated;
    ListView lstViewdoginfo;
    Button btnAdddogInfo,btnDeletedoginfo,btnOpen;
    public String dogBreed[]={"Labrador Retriever","German Shepherd","BullDog","Boxer","Beagle","Siberian Huskey","Great Dane"};

    public Integer cntrBreed=0,cntrDog=0;
    ArrayAdapter adapterDoginfo;
    public String mdogname="brownie",mbreed="Rotweiller",mvaccinated="Vaccinated";
    public int valueId[] ;

    public List<String> listBreed;

    private DataBaseHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstViewdoginfo = (ListView) findViewById(R.id.ListViewdog);
        btnAddDoginfo =(Button) findViewById(R.id.buttonAdd);
        btnSend=(Button) findViewById(R.id.buttonSend);
        txtDogname=(EditText)findViewById(R.id.editTextDogName);
        spinBreed=(Spinner)findViewById(R.id.spinnerBreed);
        swVaccinated=(Switch)findViewById(R.id.switchVaccinated);
        txtVaccinated=(TextView)findViewById(R.id.textViewVaccinated);
        txtVaccinated.setTextColor(0XFF27C408);
        dbhelper = new DataBaseHelper(MainActivity.this, "DogInfoDatabase",2);
//       populateData();
        spinBreedpopulate();
        swVaccinatedlistener();
        spinBreedListener();
        refreshall();
        openSecond();
        buttonAddlistener();
    }

    private void spinBreedpopulate() {

        listBreed = new ArrayList<String>();
        for (int x=0;x<dogBreed.length;x++) {
            listBreed.add(dogBreed[x]);
        }
        ArrayAdapter<String> dataAdapterStatus = new ArrayAdapter<String>(this,
                R.layout.spinner_item, listBreed);
        dataAdapterStatus.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinBreed.setAdapter(dataAdapterStatus);
        spinBreed.setPrompt("Select Breed");

    }

    private void spinBreedListener() {

        spinBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mbreed=spinBreed.getItemAtPosition(i).toString();
//                refreshall();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


    }

    private void  swVaccinatedlistener(){

        swVaccinated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true) {
                    txtVaccinated.setTextColor(0XFF080EC4);
                    txtVaccinated.setText("Vaccinated");
                    swVaccinated.setText("Change to Not Vaccinated");

                }

                else {
                    txtVaccinated.setTextColor(0XFF27C408);
                    txtVaccinated.setText("Not Vaccinated");
                    swVaccinated.setText("Change to Vaccinated");
                }
            }
        });

    }

    private void openSecond() {

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lstViewdoginfo.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,new ArrayList<String>()));

                if (dogbreedinfo.size()!=0){
                    sendData();
                }


            }
        });
    }

    private void refreshall() {

        reloaddata();
        reloadbreedsummary();
        reloadstatussummary();
    }



    private void buttonAddlistener() {

        final SQLiteDatabase dbAdd = dbhelper.getWritableDatabase();
        btnAddDoginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sqlStr = "INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('"
                        + txtDogname.getText() + "', '" + mbreed + "','"
                        + txtVaccinated.getText() + "')";



                dbAdd.execSQL(sqlStr);

               refreshall();
                                        }
        });


    }

    private void reloadbreedsummary() {


        SQLiteDatabase dbDoginfo = dbhelper.getWritableDatabase();
        //get table from sqlite_master
        Cursor cDoginfo = dbDoginfo.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tbldoginfo'", null);
        cDoginfo.moveToNext();
        if (cDoginfo.getCount() == 0) { //check if the database is exisitng
            SQLite.FITCreateTable("DogInfoDatabase", this, "tbldoginfo", "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "dogname VARCHAR(90),breed VARCHAR(90),vaccstatus VARCHAR(90)"); //create table

            String sqlStr = "INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol','Askal','Not Vaccinated')";


            try {
                dbDoginfo.execSQL(sqlStr);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


//       cDoginfo = dbDoginfo.rawQuery("SELECT id, dogname, breed,vaccstatus  FROM tbldoginfo where breed = '" + mbreed + "' order by id desc", null);
        cDoginfo = dbDoginfo.rawQuery("SELECT breed,count(breed) as tcount  FROM tbldoginfo group by breed", null);
//        cDoginfo= dbDoginfo.rawQuery("SELECT vaccstatus  FROM tbldoginfo  ", null);

        String valueDoginfo[] = new String[cDoginfo.getCount()];
        int valueCurrentId[] = new int[cDoginfo.getCount()];


        int ctrl = 0;

        dogbreedinfo.clear();

        while (cDoginfo.moveToNext()) {
            String strFor = "";
//                Integer strId=0;


            strFor += System.lineSeparator() + "Breed : " + cDoginfo.getString(cDoginfo.getColumnIndex("breed"));
            strFor += System.lineSeparator() + "Total : " + cDoginfo.getString(cDoginfo.getColumnIndex("tcount"));

            valueDoginfo[ctrl] = strFor;

            dogbreedinfo.add(strFor);

            ctrl++;
        }


    }


    private void reloadstatussummary() {

        SQLiteDatabase dbDoginfo = dbhelper.getWritableDatabase();
        //get table from sqlite_master
        Cursor cDoginfo = dbDoginfo.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tbldoginfo'", null);
        cDoginfo.moveToNext();
        if (cDoginfo.getCount() == 0) { //check if the database is exisitng
            SQLite.FITCreateTable("DogInfoDatabase", this, "tbldoginfo", "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "dogname VARCHAR(90),breed VARCHAR(90),vaccstatus VARCHAR(90)"); //create table

            String sqlStr = "INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol','Askal','Not Vaccinated')";


            try {
                dbDoginfo.execSQL(sqlStr);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


//       cDoginfo = dbDoginfo.rawQuery("SELECT id, dogname, breed,vaccstatus  FROM tbldoginfo where breed = '" + mbreed + "' order by id desc", null);
        cDoginfo = dbDoginfo.rawQuery("SELECT vaccstatus,count(vaccstatus) as tcount  FROM tbldoginfo group by vaccstatus", null);
//        cDoginfo= dbDoginfo.rawQuery("SELECT vaccstatus  FROM tbldoginfo  ", null);

        String valueDoginfo[] = new String[cDoginfo.getCount()];
        int valueCurrentId[] = new int[cDoginfo.getCount()];


        int ctrl = 0;

        dogstatusinfo.clear();

        while (cDoginfo.moveToNext()) {
            String strFor = "";
//                Integer strId=0;


            strFor += System.lineSeparator() + "Status: " + cDoginfo.getString(cDoginfo.getColumnIndex("vaccstatus"));
            strFor += System.lineSeparator() + "Total : " + cDoginfo.getString(cDoginfo.getColumnIndex("tcount"));

            valueDoginfo[ctrl] = strFor;

            dogstatusinfo.add(strFor);

            ctrl++;
        }


    }


    private void reloaddata() {


        SQLiteDatabase dbDoginfo = dbhelper.getWritableDatabase();
        //get table from sqlite_master
        Cursor cDoginfo = dbDoginfo.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tbldoginfo'", null);
        cDoginfo.moveToNext();

        if (cDoginfo.getCount() == 0) { //check if the database is exisitng
            SQLite.FITCreateTable("DogInfoDatabase", this, "tbldoginfo", "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "dogname VARCHAR(90),breed VARCHAR(90),vaccstatus VARCHAR(90)"); //create table

            String sqlStr = "INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol','Askal','Not Vaccinated')";


            try {
                dbDoginfo.execSQL(sqlStr);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            dbDoginfo.execSQL("INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol1','Askal555','Not Vaccinated')");
            dbDoginfo.execSQL("INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol2','Askal','Not Vaccinated')");
            dbDoginfo.execSQL("INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol3','Askal','Dengvaxia')");
            dbDoginfo.execSQL("INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol4','Askal','Not Vaccinated')");
            dbDoginfo.execSQL("INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol5','Askal','Not Vaccinated')");
            dbDoginfo.execSQL("INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol6','Askal',' DengVaxia')");
            dbDoginfo.execSQL("INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol6','Askal',' Vaccinated')");
            dbDoginfo.execSQL("INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol6','Askal',' DengVaxia')");
            dbDoginfo.execSQL("INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol6','Askal',' Vaccinated')");
            dbDoginfo.execSQL("INSERT INTO tbldoginfo (dogname, breed, vaccstatus) VALUES ('tukmol6','Askal',' Vaccinated')");

        }


//       cDoginfo = dbDoginfo.rawQuery("SELECT id, dogname, breed,vaccstatus  FROM tbldoginfo where breed = '" + mbreed + "' order by id desc", null);
        cDoginfo = dbDoginfo.rawQuery("SELECT id, dogname, breed,vaccstatus  FROM tbldoginfo  order by id desc", null);
//        cDoginfo= dbDoginfo.rawQuery("SELECT vaccstatus  FROM tbldoginfo  ", null);

        String valueDoginfo[] = new String[cDoginfo.getCount()];
        int valueCurrentId[] = new int[cDoginfo.getCount()];


        int ctrl = 0;



        while (cDoginfo.moveToNext()) {
            String strFor = "";
//                Integer strId=0;

            strFor += "Dog Name : " + cDoginfo.getString(cDoginfo.getColumnIndex("dogname"));
            strFor += System.lineSeparator() + "Breed  : " + cDoginfo.getString(cDoginfo.getColumnIndex("breed"));
            strFor += System.lineSeparator() + "Status : " + cDoginfo.getString(cDoginfo.getColumnIndex("vaccstatus"));

            valueCurrentId[ctrl]= cDoginfo.getInt(cDoginfo.getColumnIndex("id"));
            valueDoginfo[ctrl] = strFor;

            ctrl++;
        }
//
//        valueId = Arrays.copyOf(valueCurrentId, cDoginfo.getCount());//transfer content array to a public array


//            adapterDoginfo = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, valueDoginfo);
//
//            try {
//                lstViewdoginfo.setAdapter(adapterDoginfo);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        lstViewdoginfo.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, valueDoginfo));


    }



    private DoginfoCollection getBreed()
    {

        DoginfoCollection doginfoCollection =new DoginfoCollection();
        doginfoCollection.setDoginfo(dogbreedinfo);
        return doginfoCollection;
    }

    private DoginfoCollection getStatus()
    {

        DoginfoCollection doginfoCollection =new DoginfoCollection();
        doginfoCollection.setDoginfo(dogstatusinfo);
        return doginfoCollection;
    }



    private void sendData() {

        Intent intent=new Intent(this,SecondActivity.class);
        intent.putExtra("DOGBREED",this.getBreed());
       intent.putExtra("STATUS",this.getStatus());

        startActivity(intent);
    }
}
