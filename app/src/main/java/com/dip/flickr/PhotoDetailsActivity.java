package com.dip.flickr;

import android.content.Intent;
import android.os.Bundle;
import com.squareup.picasso.Picasso;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoDetailsActivity extends BaseActivity {

    TextView txtPhotoTitle, txtAuthor, txtTags;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        activateToolbar(true);

        txtPhotoTitle = findViewById(R.id.txtTitle_photoDetails);
        txtAuthor = findViewById(R.id.txtAuthor_photoDetails);
        txtTags = findViewById(R.id.txtTags_photoDetails);
        imageView = findViewById(R.id.imgPhoto_photoDetails);

        Intent intentFromTap = getIntent();
        Photo photo = (Photo) intentFromTap.getSerializableExtra(PHOTO_TRANSFER);

        if (photo != null){
            txtPhotoTitle.setText(getResources().getString(R.string.photo_title, photo.getTitle()));
            txtTags.setText(getString(R.string.photo_tags, photo.getTags()));
            txtAuthor.setText(getString(R.string.photo_author, photo.getAuthor()));

            Picasso.with(this).load(photo.getLink())
                    .error(R.drawable.place_holder)
                    .placeholder(R.drawable.place_holder)
                    .into(imageView);
        }


    }

}
