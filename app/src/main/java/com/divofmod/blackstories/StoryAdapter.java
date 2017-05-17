package com.divofmod.blackstories;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

class StoryAdapter extends ArrayAdapter<Story> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int mLayout;
    private ArrayList<Story> mStories;

    StoryAdapter(Context context, int resource, ArrayList<Story> stories) {
        super(context, resource, stories);
        mContext = context;
        mStories = stories;
        mLayout = resource;
        mLayoutInflater = LayoutInflater.from(context);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mLayout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        Story story = mStories.get(position);

        InputStream inputStream = null;
        try {
            inputStream = mContext.getAssets().open(story.getId() + "s.png");
            viewHolder.mImage.setImageDrawable(Drawable.createFromStream(inputStream, null));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        viewHolder.mTitle.setText(story.getTitle());
        viewHolder.mStory.setText(story.getStory());

        return convertView;
    }

    private class ViewHolder {
        final ImageView mImage;
        final TextView mTitle, mStory;

        ViewHolder(View view) {
            mImage = (ImageView) view.findViewById(R.id.image);
            mTitle = (TextView) view.findViewById(R.id.title);
            mStory = (TextView) view.findViewById(R.id.story);
        }
    }
}
