package com.digital.compass.maps.digitalcompass.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.digital.compass.maps.digitalcompass.MyDiffUntil;
import com.digital.compass.maps.digitalcompass.R;
import com.digital.compass.maps.digitalcompass.model.ModelCompass;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class AdapterCompass_V2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_1 = 1;
    private static final int TYPE_2 = 2;
    private Activity context;
    private ArrayList<ModelCompass> list;
    private OnClick onClick;

    public interface OnClick {
        void Click(View view, int i);
    }

    public AdapterCompass_V2(Activity activity, ArrayList<ModelCompass> arrayList) {
        this.context = activity;
        this.list = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1)
            return new VH1(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_change_compass_v2, (ViewGroup) null, false));
        else
            return new VH1(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_native_theme, (ViewGroup) null, false));
    }

    public void setOnClickListener(OnClick onClick2) {
        this.onClick = onClick2;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        int[] bgs = new int[]{R.drawable.bg_theme1, R.drawable.bg_theme3, R.drawable.bg_theme4, R.drawable.bg_theme5};
        VH1 vh1 = (VH1) viewHolder;
        if (list.get(i).getType() == 1) {
            for (int i1 = 0; i1 < bgs.length; i1++) {
                if (i % (i1 + 1) == 0) {
                    vh1.setCompass(this.list.get(i), bgs[i1]);
                }
            }
            vh1.buttonApply.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    AdapterCompass_V2.this.customOne(i, view);
                }
            });
            return;
        }
    }

    public void customOne(int i, View view) {
        this.onClick.Click(view, i);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    @Override
    public int getItemViewType(int i) {
        return list.get(i).getType();
    }

    public class VH1 extends RecyclerView.ViewHolder {
        Button buttonApply;
        ImageView imageViewCompass;
        TextView tvCompass;
        FrameLayout fr_native;
        ShimmerFrameLayout shimmerFrameLayout;
        RelativeLayout layoutAds;

        VH1(View view) {
            super(view);
            this.tvCompass = (TextView) view.findViewById(R.id.name_compass);
            this.imageViewCompass = (ImageView) view.findViewById(R.id.image_compass_v2);
            this.buttonApply = (Button) view.findViewById(R.id.button_apply);
            this.fr_native = (FrameLayout) view.findViewById(R.id.fr_native);
            this.shimmerFrameLayout = view.findViewById(R.id.shimmer_container_native);
            this.layoutAds = view.findViewById(R.id.layout_ads);
        }


        public void setCompass(ModelCompass modelCompass, int bg) {
            this.tvCompass.setText(modelCompass.getNameCompass());
            Glide.with(AdapterCompass_V2.this.context.getApplicationContext()).load(Integer.valueOf(modelCompass.getImageCOmpass())).into(this.imageViewCompass);
            //if (modelCompass.isActive()) {
            this.buttonApply.setText(R.string.apply);
            //this.buttonApply.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            this.buttonApply.setBackgroundResource(bg);
            return;
            /*}
            this.buttonApply.setText(R.string.unlock);
            this.buttonApply.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlock, 0, 0, 0);*/
        }
    }

    public void setList(ArrayList<ModelCompass> arrayList) {
        this.list = arrayList;
        notifyDataSetChanged();
    }

    public void setData(ArrayList<ModelCompass> arrayList) {
        DiffUtil.DiffResult calculateDiff = DiffUtil.calculateDiff(new MyDiffUntil(arrayList, this.list));
        this.list.clear();
        this.list.addAll(arrayList);
        calculateDiff.dispatchUpdatesTo(this);
    }


}
