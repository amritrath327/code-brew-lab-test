package com.example.yelowflash.test11;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by YELOWFLASH on 05/28/2016.
 */
public class FeedFragment extends Fragment {
    ImageView img;
    TextView tvName, tvAddress, tvdate, tvFavCount, tvCommentCount, tvDevName;
    String imageUrl, name, address, date, favCount, commentCount, devName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        name = b.getString("property_name");
        imageUrl = b.getString("image");
        address = b.getString("address");
        date = b.getString("sale_date");
        favCount = b.getString("favor_count");
        commentCount = b.getString("comment_count");
        devName = b.getString("developer_name");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        tvAddress = (TextView) view.findViewById(R.id.tv_address);
        tvCommentCount = (TextView) view.findViewById(R.id.tv_comment_count);
        tvdate = (TextView) view.findViewById(R.id.tv_date);
        tvDevName = (TextView) view.findViewById(R.id.tv_developer);
        tvFavCount = (TextView) view.findViewById(R.id.tv_fav_count);
        tvName = (TextView) view.findViewById(R.id.tvprp_name);
        img = (ImageView) view.findViewById(R.id.img);
        Log.i("imge",imageUrl);
        Glide.with(getActivity())
                .load(imageUrl).override(720,800)
                .into(img);
        tvName.setText(name);
        tvFavCount.setText(favCount);
        tvCommentCount.setText(commentCount);
        tvDevName.setText("Developer:"+devName);
        tvdate.setText(date);
        tvAddress.setText(address);
        return view;
    }
}
