package com.cloudsourcing.android.cloudsourcing;


import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by arbass on 7/2/15.
 */
@ParseClassName("Clouds")
public class Cloud extends ParseObject{

    public String getCloudId() {
        return this.getObjectId();
    }

    public String getUrl() {
        return (String) this.get("url");
    }

    public void setUrl(URL imageUrl) {
         this.put("url", imageUrl);
    }


    public void addResult(String answer) {

        HashMap<String, Integer> results;
        results = (HashMap<String, Integer>) this.get("results");
        if (results == null) {
            results = new HashMap<>();
        }
        Integer count = results.get(answer);
        if (count == null) {
            results.put(answer, 1);
        } else {
            results.put(answer, count + 1);
        }
        this.put("results", results);

        try {
            this.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



}

