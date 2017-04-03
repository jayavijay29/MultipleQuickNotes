package com.jayavijayjayavelu.multiplequicknotes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MultiAutoCompleteTextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    boolean canAddItem = false;
    MultiAutoCompleteTextView notes;
    ArrayList<String> notesList = new ArrayList<>();
    JSONArray user = null;
    static JSONObject temp1 = new JSONObject();
    RecyclerView recyclerView;
    SavingValues sValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        MainActivity.this.onResume();
        try {
            recyclerView.setAdapter(new CardsAdapter(generateCards(temp1),recyclerView,MainActivity.this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    private ArrayList<CardValues> generateCards(JSONObject c) throws IOException {
        ArrayList<CardValues> cards = new ArrayList<>();
        try {
            JSONArray user1 = c.getJSONArray("NotesList");
            for (int i = 0; i < user1.length() ; i++) {
                JSONObject c1 = user1.getJSONObject(i);
                cards.add(new CardValues(c1.getString("title"), c1.getString("notes"), c1.getString("time"),Color.parseColor("#D3D3D3")));
            }
        }
        catch (Exception e){

        }
        return cards;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addNotes:
                canAddItem = true;
                invalidateOptionsMenu();
                Bundle b = new Bundle();
                b.putString("message","your message");
                Intent i = new Intent(MainActivity.this,NewActivity.class);
                i.putExtras(b);
                startActivity(i);
                return true;
            case R.id.infoBar:
                invalidateOptionsMenu();
                Bundle b1 = new Bundle();
                b1.putString("message","your message");
                Intent i1 = new Intent(MainActivity.this,AboutActivity.class);
                i1.putExtras(b1);
                startActivity(i1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateData(JSONObject sampleObj) throws JSONException, IOException {

        JSONArray jArray = new JSONArray();
        jArray.put(sampleObj);
        if (jArray != null) {
            for (int i = 0; i < jArray.length(); i++) {
                notesList.add(jArray.getString(i));
            }
            SavingValues sv = new SavingValues(notesList);
        }
        temp1=sampleObj;
        new JSONParse().execute();
    }


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject json = temp1;
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                user = json.getJSONArray("NotesList");
                JSONObject c = user.getJSONObject(0);
                temp1=json;
                try {
                    generateCards(temp1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            temp1 = new JSONObject();
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
                    temp1.accumulate("NotesList", valuesObject);
                else {
                    list.put(valuesObject);
                    temp1.accumulate("NotesList", list);
                }
            }
            reader.endObject();
            reader.close();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp1;
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
            JSONArray user1 = new JSONArray(temp1.get("NotesList").toString());
            for (int i = 0; i < user1.length() ; i++) {
                JSONObject c1 = user1.getJSONObject(i);
                writer.name("title").value(c1.getString("title"));
                writer.name("notes").value(c1.getString("notes"));
                writer.name("time").value(c1.getString("time"));
            }
            writer.endObject();
            writer.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}



