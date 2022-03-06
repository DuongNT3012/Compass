package com.digital.compass.maps.digitalcompass.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.digital.compass.maps.digitalcompass.R;

public class AdapterCompass extends PagerAdapter {
    private Context context;

    @Override 
    public int getCount() {
        return 6;
    }

    public int getImageAt(int i) {
        if (i == 1) {
            return R.drawable.ic_compass5;
        }
        if (i != 2) {
            return i != 3 ? i != 4 ? i != 5 ? R.drawable.ic_compass3 : R.drawable.ic_compass_v2 : R.drawable.ic_compass1 : R.drawable.ic_compass4;
        }
        return -1;
    }

    @Override 
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public AdapterCompass(Context context2) {
        this.context = context2;
    }

    @Override 
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.image_compass, (ViewGroup) null);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.image_compass_slide);
        if (i != 2) {
            Glide.with(inflate).load(Integer.valueOf(getImageAt(i))).into(imageView);
        }
        viewGroup.addView(imageView);
        return imageView;
    }

    @Override 
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }
}
