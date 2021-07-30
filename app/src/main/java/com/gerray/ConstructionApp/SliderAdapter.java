package com.gerray.ConstructionApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.jetbrains.annotations.NotNull;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    //arrays
    public int[] slide_images = {
            R.drawable.bird_icon,
            R.drawable.eat_icon,
            R.drawable.person_icon
    };

    public String[] slide_heading = {
            "BIRD", "EAT", "CODE"
    };

    public String[] slide_description = {
        "This is a Sample Splash screen view pager that will help the user while using this Construction Application System",
            "This is a Sample Splash screen view pager that will help the user while using this Construction Application System",
            "This is a Sample Splash screen view pager that will help the user while using this Construction Application System"
    };

    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImage = view.findViewById(R.id.sImageView);
        TextView slideHeader = view.findViewById(R.id.tv_heading);
        TextView slideDescription = view.findViewById(R.id.tv_body);

        slideImage.setImageResource(slide_images[position]);
        slideHeader.setText(slide_heading[position]);
        slideDescription.setText(slide_description[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
