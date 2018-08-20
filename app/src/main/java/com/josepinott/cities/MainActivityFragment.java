package com.josepinott.cities;

import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.josepinott.cities.adapter.CityRecyclerViewAdapter;
import com.josepinott.cities.model.City;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeSet;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SearchView.OnQueryTextListener {
    /**
     * Tag used on log messages.
     */
    private static final String TAG = MainActivityFragment.class.getName();
    /**
     * Cities will be held in a TreeSet.
     * Structure implementation provides guaranteed log(n) time cost for the basic operations (add, remove and contains)
     */
    private TreeSet<City> mCitiesMap;
    /**
     * Handle UI update
     */
    private Handler mHandler;
    /**
     * Text field for search input
     */
    private SearchView mSearchView;
    /**
     * Progress bar
     */
    ProgressBar mProgressBar;

    private RecyclerView mRecyclerView;
    private CityRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MainActivityFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCitiesMap = new TreeSet<>(new CityComparator());
        mHandler = new Handler();
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchView = view.findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(this);
        mProgressBar = view.findViewById(R.id.search_progress_bar);
        mRecyclerView = view.findViewById(R.id.search_results_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the recycler_view_city_item size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CityRecyclerViewAdapter(getActivity(), mCitiesMap);
        mRecyclerView.setAdapter(mAdapter);

        // show loading spinner
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        mAdapter.getFilter().filter(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mAdapter.getFilter().filter(s);
        return true;
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
                            // perform insertion Sort for any Comparable Objects
                            Comparable temp;
                            for(int i=0;i< arraySize; i++) {
                                City city = gson.fromJson(jsonArray.getJSONObject(i).toString(), City.class);
                                city.mDisplayLabel = city.mName + ", " + city.mCountry;
                                // fix single quote prefix
                                if(city.mName.charAt(0) == '‘' || city.mName.charAt(0) == '\'')
                                    city.mName = city.mName.substring(1);
                                mCitiesMap.add(city);
                            }
                            Log.d(TAG, "ending...loadData finished successfully, updating UI.");
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // update UI
                                    mAdapter.setCitiesList(mCitiesMap);
                                    mAdapter.notifyDataSetChanged();
                                    mProgressBar.setVisibility(View.INVISIBLE);
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

    /**
     * TreeSet elements are ordered using this Comparator at creation time.
     */
    public class CityComparator implements Comparator<City> {
        @Override
        public int compare(City o1, City o2) {
            int result = o1.mName.compareTo(o2.mName);
            if (result != 0) return result;
            return o1.mCountry.compareTo(o2.mCountry);
        }
    }
}
