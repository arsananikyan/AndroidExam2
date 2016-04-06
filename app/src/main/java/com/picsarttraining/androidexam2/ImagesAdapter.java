package com.picsarttraining.androidexam2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Arsen on 23.03.2016.
 */
public class ImagesAdapter extends BaseAdapter {
    ArrayList<Bitmap> images;
    Context context;
    private LayoutInflater inflater;

    public ImagesAdapter(Context context) {
        this.context = context;
        this.images = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void addImage(Bitmap bitmap) {
        images.add(bitmap);
        notifyDataSetChanged();
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.images.clear();
        this.images = new ArrayList<>(images);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Bitmap getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bitmap imageBitmap = images.get(position);
        ImageView imageView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_image, parent, false);
            imageView = (ImageView) convertView.findViewById(R.id.item_image_view);
            convertView.setTag(imageView);
        } else
            imageView = (ImageView) convertView.getTag();
        imageView.setImageBitmap(imageBitmap);

        return convertView;
    }

}
