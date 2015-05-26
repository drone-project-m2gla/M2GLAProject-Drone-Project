package fr.m2gla.istic.projet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.m2gla.istic.projet.activity.R;
import fr.m2gla.istic.projet.model.GeoImage;

/**
 * Created by baptiste on 26/05/15.
 */
public class CarouselAdapter extends BaseAdapter {
    private ArrayList<GeoImage> data = new ArrayList<>(0);
    private Context context;

    public CarouselAdapter(Context context) {
        this.context = context;
    }

    public void addItems(List<GeoImage> items) {
        data.addAll(items);
    }

    public void addItems(GeoImage[] items) {
        for (GeoImage item : items) {
            data.add(item);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.getLong(data.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_carousel, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.label);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        }

        return convertView;
    }

    static class ViewHolder {
        public TextView text;
        public ImageView image;
    }
}
