package com.gsrtc.mobileweb.ui.adapters;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.utils.SliderUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ajith on 7/13/2019.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;
    private List<SliderUtils> sliderImg;

    public ViewPagerAdapter(List<SliderUtils> sliderImg, Context context) {
        this.sliderImg = sliderImg;
        this.context = context;
    }


    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout, null);

        SliderUtils utils = sliderImg.get(position);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        Picasso.get()
                .load(utils.getImageUrl())
                .resize(400, 200)                        // optional
                .into(imageView);

       // imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
      //  imageLoader.get(utils.getImageUrl(), ImageLoader.getImageListener(imageView,400,200));

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}