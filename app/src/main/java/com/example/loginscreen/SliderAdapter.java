package com.example.loginscreen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;


    public SliderAdapter(Context context){
        this.context=context;
    }

    public int[] slide_images = {
            R.drawable.presstest,
            R.drawable.fall,
            R.drawable.pill1,
            R.drawable.zone,
            R.drawable.mic_on
    };

    public String[] slide_headings = {
            "PANIC BUTTON","FALL DETECTOR","PILL REMINDER","SAFE ZONE","VOICE COMMANDS"
    };

    public String[] slide_descs = {
            "In SOS section, by pressing Panic Button for 3 Sec, an SMS alert will send instantly to your added Emergency Contact",
            "In Fall Detector section, by pressing ON Button, it will send SMS alert as soon as it detects the Fall",
            "In Pill Reminder Section, adding Pill name, shape and other details will notify user in selected interval",
            "SafeZone will send notification to user as soon as it detect user outside of SafeZone",
            "By saying 'Help Me' will send an SMS alert to an Emergency Contacts in Interval of 15min and by saying 'Stop Update' will stop sending SMS updates to emergency contact"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout)o;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView =(ImageView)view.findViewById(R.id.imageView4);
        TextView slideHeading = (TextView) view.findViewById(R.id.textView2);
        TextView slideDescription  = (TextView)view.findViewById(R.id.textView3);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
