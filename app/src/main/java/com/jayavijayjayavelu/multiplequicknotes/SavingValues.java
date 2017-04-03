package com.jayavijayjayavelu.multiplequicknotes;


import org.json.JSONArray;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jayavijayjayavelu on 2/25/17.
 */

public class SavingValues {
    private String time;
    private String notes;
    private String title;
    private static ArrayList<String> jsonList = new ArrayList<>();

    public ArrayList<String> getJsonList() {
        return jsonList;
    }

    public void setJsonList(ArrayList<String> jsonList) {
        this.jsonList = jsonList;
    }

    public SavingValues(String tt, String no) throws IOException {
        title = tt;
        notes = no;
    }
    public SavingValues() throws IOException {

    }
    public SavingValues(ArrayList<String> jL) throws IOException {
        jsonList.add(jL.get(0));
        JSONArray jArray = new JSONArray(jsonList);
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

}
