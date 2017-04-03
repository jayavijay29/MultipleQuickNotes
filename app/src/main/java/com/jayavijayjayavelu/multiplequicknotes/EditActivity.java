package com.jayavijayjayavelu.multiplequicknotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jayavijayjayavelu on 2/24/17.
 */

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity";
    Menu menu;
    public static EditText title;
    static MultiAutoCompleteTextView notes;
    private SavingValues sValues;
    public static String titleTemp;
    public static String noteTemp;
    MainActivity mainActivity = new MainActivity();
    static JSONObject mainObject = new JSONObject();
    static JSONObject tempObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
        Intent intent = getIntent();
        titleTemp = intent.getStringExtra("title");
        noteTemp = intent.getStringExtra("notes");
        title = (EditText) findViewById(R.id.editText);
        notes = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView);
        title.setText(titleTemp);
        notes.setText(noteTemp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNotes:
                JSONObject sampleObject = new JSONObject();
                sampleObject = parseJSON();
                try {
                    mainActivity.updateData(sampleObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(EditActivity.this,MainActivity.class);
                startActivity(i);
                finish();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static JSONObject parseJSON() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd, hh:mm a");
        Date now = new Date();
        String strDate = sdf.format(now);
        try {
            JSONObject valuesObject = new JSONObject();
            tempObject = new JSONObject();
            int l = mainObject.length();
            {
                JSONArray user1 = mainObject.getJSONArray("NotesList");
                int count =0;
                if(count==0) {
                    valuesObject.put("title", title.getText().toString());
                    valuesObject.put("notes", notes.getText().toString());
                    valuesObject.put("time", strDate);
                    JSONObject tempObj = new JSONObject();
                    user1.put(valuesObject);
                    tempObj.accumulate("NotesList", user1);
                    mainObject = tempObj;
                    JSONArray tempArray = new JSONArray();
                    tempArray.put(valuesObject);
                    tempObject.accumulate("NotesList", tempArray);
                }
            }
            return mainObject;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    protected void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to save the note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            sValues = new SavingValues(title.getText().toString(),notes.getText().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sValues.setTitle(title.getText().toString());
                        sValues.setNotes(notes.getText().toString());
                        JSONObject sampleObject = new JSONObject();
                        sampleObject = parseJSON();
                        try {
                            mainActivity.updateData(sampleObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(EditActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        noteTemp="";
                        titleTemp="";
                        Intent i = new Intent(EditActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .show();

    }
    @Override
    protected void onPause() {
        super.onPause();

        saveProduct();
    }

    public void saveProduct() {
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("MultiNotes.json", Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
            writer.setIndent("  ");
            writer.beginObject();
            int countT =0;
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd, hh:mm a");
            Date now = new Date();
            String strDate = sdf.format(now);
            JSONArray user1 = new JSONArray(mainObject.get("NotesList").toString());
            for (int i = 0; i < user1.length(); i++) {
                JSONObject c1 = user1.getJSONObject(i);
                if(!c1.getString("title").trim().equals("")&&!c1.getString("notes").trim().equals("")) {
                    if (!c1.getString("title").equals(titleTemp) && c1.getString("notes").equals(noteTemp)) {
                        writer.name("title").value(c1.getString("title"));
                        writer.name("notes").value(c1.getString("notes"));
                        writer.name("time").value(c1.getString("time"));
                        countT++;
                    } else if (!c1.getString("notes").equals(noteTemp) && c1.getString("title").equals(titleTemp)) {
                        writer.name("title").value(c1.getString("title"));
                        writer.name("notes").value(c1.getString("notes"));
                        writer.name("time").value(c1.getString("time"));
                        countT++;
                    } else if (!c1.getString("title").equals(titleTemp) && !c1.getString("notes").equals(noteTemp)) {
                        writer.name("title").value(c1.getString("title"));
                        writer.name("notes").value(c1.getString("notes"));
                        writer.name("time").value(c1.getString("time"));
                    } else if (c1.getString("title").equals(titleTemp) && c1.getString("notes").equals(noteTemp)) {
                        if (countT == 0 && i == (user1.length() - 1)) {
                            writer.name("title").value(c1.getString("title"));
                            writer.name("notes").value(c1.getString("notes"));
                            writer.name("time").value(c1.getString("time"));
                        }
                    }
                }else{
                    Toast.makeText(EditActivity.this, "No Title or Note to Save !!!",
                            Toast.LENGTH_LONG).show();
                }
            }
            writer.endObject();
            writer.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onStart();
        loadFile();
        if (sValues != null) {

        }
    }

    public JSONObject loadFile() {
        try {
            sValues = new SavingValues();
            mainObject = new JSONObject();
            InputStream is = getApplicationContext().openFileInput("MultiNotes.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
            JSONObject valuesObject = new JSONObject();
            JSONArray list = new JSONArray();

            reader.beginObject();
            int count = 0;

            while (reader.hasNext()) {
                try {
                    valuesObject = new JSONObject();
                    reader.nextName();
                    String title = reader.nextString();
                    valuesObject.put("title", title);
                    reader.nextName();
                    String name = reader.nextString();
                    valuesObject.put("notes", name);
                    reader.nextName();
                    String time = reader.nextString();
                    valuesObject.put("time",time);
                    count++;
                } catch (Exception e) {

                }
                if (count > 1)
                    mainObject.accumulate("NotesList", valuesObject);
                else {
                    list.put(valuesObject);
                    mainObject.accumulate("NotesList", list);
                }
            }
            reader.endObject();
            reader.close();

        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainObject;
    }
}
