package com.josepinott.cities.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.josepinott.cities.R;
import com.josepinott.cities.model.City;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CityRecyclerViewAdapter extends RecyclerView.Adapter<CityRecyclerViewAdapter.ViewHolder> implements Filterable {
    private TreeSet<City> mCities;
    private Context mContext;
    // using view holder w/RecyclerView
    private static City[] myArray = {};
    private ValueFilter valueFilter;

    /**
     * Overloaded Constructor
     * @param context activity context
     * @param cities tree of cities
     */
    public CityRecyclerViewAdapter(Context context, TreeSet<City> cities) {
        mContext = context;
        mCities = cities;
        myArray = cities.toArray(new City[cities.size()]);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_city_item, null);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the recycler_view_city_item manager)

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // - get element from data set and replace the contents of the view with that element

        City city = myArray[i];
        viewHolder.mTitle.setText(city.mDisplayLabel);

    }

    // Return the size of your dataset (invoked by the recycler_view_city_item manager)
    @Override
    public int getItemCount() {
        return myArray.length;
    }

    public City getItem(int pos) {
        return myArray[pos];
    }

    /**
     * TreeSet setter
     * @param citiesList new list of cities
     */
    public void setCitiesList(TreeSet<City> citiesList) {
        mCities = citiesList;
        myArray = citiesList.toArray(new City[citiesList.size()]);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTitle;

        private ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(CityRecyclerViewAdapter.class.getName(), "city: " + myArray[getLayoutPosition()].mName);
                }
            });
            mTitle = v.findViewById(R.id.city);
        }
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<City> cityList = new ArrayList<>();
                for (City city : mCities) {
                    if (city.mName.toLowerCase().startsWith(constraint.toString().toLowerCase()))
                        cityList.add(city);
                }
                City[] tmp = cityList.toArray(new City[cityList.size()]);
                results.count = tmp.length;
                results.values = tmp;
            } else {
                results.count = mCities.size();
                results.values = mCities.toArray(new City[mCities.size()]);;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            myArray = (City[]) results.values;
            notifyDataSetChanged();
        }
    }
}
