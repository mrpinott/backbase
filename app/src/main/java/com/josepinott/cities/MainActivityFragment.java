package com.josepinott.cities;

import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.josepinott.cities.model.City;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    /**
     * Tag used on log messages.
     */
    private static final String TAG = MainActivityFragment.class.getName();
    /**
     * Cities will be held in a LinkedHashMap.
     * Hash map will allow look up complexity of O(1)
     * vs a list O(n). LinkedHashMap will be used for
     * consistent ordering.
     */
    private LinkedHashMap<String, City> mCitiesMap;
    /**
     * Handle UI update
     */
    private Handler mHandler;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCitiesMap = new LinkedHashMap<>();
        mHandler = new Handler();
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void loadData() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d(TAG, "start loadData job");
                    Resources resources = getActivity().getResources();
                    if(resources != null) {
                        try {
                            InputStream inputStream = resources.openRawResource(R.raw.cities);
                            int size = inputStream.available();
                            byte[] buffer = new byte[size];
                            int totalBytes = inputStream.read(buffer);
                            inputStream.close();
                            // Consideration: Optimize for fast searches, loading time of the app is not so important
                            JSONArray jsonArray = new JSONArray(new String(buffer, "UTF-8"));
                            Gson gson = new Gson();
                            int arraySize = jsonArray.length();
                            Log.d(TAG, "Total bytes: " + totalBytes + ", array length: " + arraySize);
                            for(int i=0;i< arraySize; i++) {
                                City city = gson.fromJson(jsonArray.getJSONObject(i).toString(), City.class);
//todo: sorting consideration
                                mCitiesMap.put(city.mName, city);
                            }
                            Log.d(TAG, "ending...loadData finished successfully, updating UI.");
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // update UI
                                }
                            });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else
                        Log.e(TAG, "Cannot load file, resources reference is null.");
                } catch (Exception e) {
                    Log.e(TAG, "Absolute catastrophe, error: " + e.getMessage());
                }
            }
        }).start();
    }
}
