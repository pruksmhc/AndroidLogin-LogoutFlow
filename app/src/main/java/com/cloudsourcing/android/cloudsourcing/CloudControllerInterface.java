package com.cloudsourcing.android.cloudsourcing;

/**
 * Created by arbass on 7/2/15.
 */
public interface CloudControllerInterface {

    Cloud getCloud();

    String submitAndGetResults(Cloud cloud, String answer);

}
