package com.jayavijayjayavelu.multiplequicknotes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jayavijayjayavelu on 2/25/17.
 */

public class CompositionJso extends JSONObject {


    public JSONObject makeJSONObject(String title, String notes, String time) {

        JSONObject obj = new JSONObject();

        try {
            obj.put("title", title);
            obj.put("notes", notes);
            obj.put("time",time);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
