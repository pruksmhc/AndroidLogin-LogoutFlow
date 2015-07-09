// Copyright 2004-present Facebook. All Rights Reserved.

package com.cloudsourcing.android.cloudsourcing;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by angelmaredia on 7/4/15.
 */

public class CloudFrag extends Fragment {

    Cloud mCloud;
    CloudControllerInterface mCloudController;  //TODO Should this be in the activity?

    Button mSubmitButton;
    View mInputView;
    View mOutputView;
    Button mNextButton;
    EditText mInputEditText;

    TextView mResultsTextView;


    public static CloudFrag newInstance() {

        return new CloudFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_cloud, container, false);

        mCloudController = CloudControllerV1.get(getActivity());
        mCloud = mCloudController.getCloud();
        mNextButton.setText(mCloud.getUrl());

        mInputView = view.findViewById(R.id.input_layout);
        mInputEditText = (EditText) view.findViewById(R.id.input_edit_text);
        mOutputView = view.findViewById(R.id.output_layout);
        mResultsTextView = (TextView) view.findViewById(R.id.results_text_view);

        final ImageView mCloudPic = (ImageView) view.findViewById(R.id.cloud_image_view);
        final View loadScreen = view.findViewById(R.id.loadingPanel);

        mCloudPic.setVisibility(View.INVISIBLE);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(mCloud.getUrl(), mCloudPic, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                loadScreen.setVisibility(View.GONE);
                mCloudPic.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        mSubmitButton = (Button) view.findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }


                mInputView.setVisibility(View.INVISIBLE);
                mResultsTextView.setText(mCloudController.submitAndGetResults(mCloud, "Answer"));
                mCloud.addResult(mInputEditText.getText().toString());
                mOutputView.setVisibility(View.VISIBLE);

            }
        });

        mNextButton = (Button) view.findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ((GameActivity) getActivity()).replaceFrag(CloudFrag.this);
                ((GameActivity) getActivity()).addFrag();


            }
        });

        return view;
    }


}
