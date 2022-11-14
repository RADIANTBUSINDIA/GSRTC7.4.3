package com.gsrtc.mobileweb.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bala on 02-01-2018.
 */

public class RouteListAdapter extends ArrayAdapter<BusGetRouteDetails> {

    List<BusGetRouteDetails> routeList = new ArrayList<>();

    public RouteListAdapter(Context context, int textViewResourceId, List<BusGetRouteDetails> objects) {
        super(context, textViewResourceId, objects);
        routeList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.route_details_listview_item, null);
        TextView textViewTime = (TextView) v.findViewById(R.id.textViewRouteDetailTime);
        TextView textViewRoute = (TextView) v.findViewById(R.id.textViewRouteDetailRoute);
        textViewTime.setText(routeList.get(position).getPickupPointTime()+"-");
        textViewRoute.setText(routeList.get(position).getPickupPointName());

        return v;

    }
}
