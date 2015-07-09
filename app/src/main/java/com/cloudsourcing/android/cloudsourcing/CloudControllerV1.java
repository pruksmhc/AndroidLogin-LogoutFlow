package com.cloudsourcing.android.cloudsourcing;

import android.content.Context;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arbass on 7/6/15.
 */
public class CloudControllerV1 implements CloudControllerInterface {

    ArrayList<Cloud> mClouds;

    private static CloudControllerV1 sCloudController;

    public static CloudControllerV1 get(Context context) {
        if (sCloudController == null) {
            sCloudController = new CloudControllerV1(context);
        }
        return sCloudController;
    }

    private CloudControllerV1(Context context) {
        mClouds = new ArrayList<>();
    }

    @Override
    public Cloud getCloud() {
        if (mClouds == null || mClouds.size() == 0) {
            fetchClouds();
        }
        return mClouds.remove(0);
    }


    @Override
    public String submitAndGetResults(Cloud cloud, String answer) {
        return "You scored 10 points!";
    }

    private void fetchClouds() {
        ParseQuery<Cloud> query = ParseQuery.getQuery("Clouds");
        query.setLimit(5);
        //TODO This will need to be async
        try {
            List<Cloud> newClouds = query.find();
            mClouds.addAll(newClouds);
        } catch (ParseException e) {
            Log.e("CloudControllerV1", e.getMessage());
        }
    }
}
