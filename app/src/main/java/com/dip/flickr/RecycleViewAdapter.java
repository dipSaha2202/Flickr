package com.dip.flickr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ImageViewHolder> {
    private Context context;
    private List<Photo> photos;

    public RecycleViewAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (photos == null || photos.size() ==0){
            holder.thumbnail.setImageResource(R.drawable.place_holder);
            holder.txtTitle.setText(context.getString(R.string.no_result));
        } else {
            Photo photoItem = photos.get(position);
            Picasso.with(context).load(photoItem.getImage())
                    .error(R.drawable.place_holder)
                    .placeholder(R.drawable.place_holder)
                    .into(holder.thumbnail);

            holder.txtTitle.setText(photoItem.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return ((photos != null && photos.size() > 0 ? photos.size() : 1));
    }

    void loadNewData(List<Photo> photoList){
        this.photos = photoList;
        notifyDataSetChanged();
    }

    public Photo getPhotoOnTap(int position){
        return ((photos != null) && photos.size() > 0 ? photos.get(position) : null);
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView txtTitle;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.thumbnail = itemView.findViewById(R.id.imgThumbnail);
            this.txtTitle = itemView.findViewById(R.id.txtTitle);
        }
    }
}
