package com.digital.compass.maps.digitalcompass.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digital.compass.maps.digitalcompass.R;
import com.digital.compass.maps.digitalcompass.model.HelpGuidModel;

import java.util.ArrayList;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.TutorialAdapterHolder> {
    private ArrayList<HelpGuidModel> mHelpGuid;

    public TutorialAdapter(ArrayList<HelpGuidModel> mHelpGuid){
        this.mHelpGuid = mHelpGuid;
    }

    @Override
    public TutorialAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help_guide, parent, false);
        return new TutorialAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(TutorialAdapterHolder holder, int position) {
        HelpGuidModel helpGuidModel = mHelpGuid.get(position);
        if (helpGuidModel == null) {
            return;
        }
        holder.img_guide.setImageResource(helpGuidModel.getImg());
        holder.tv_content.setText(helpGuidModel.getContent());
    }

    @Override
    public int getItemCount() {
        if (mHelpGuid != null) {
            return mHelpGuid.size();
        } else {
            return 0;
        }
    }

    public class TutorialAdapterHolder extends RecyclerView.ViewHolder {
        private ImageView img_guide;
        private TextView tv_content;

        public TutorialAdapterHolder(View itemView) {
            super(itemView);
            img_guide = itemView.findViewById(R.id.img_guide);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }
}

