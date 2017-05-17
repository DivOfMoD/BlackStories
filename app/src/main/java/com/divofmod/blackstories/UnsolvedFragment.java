package com.divofmod.blackstories;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;

public class UnsolvedFragment extends Fragment {

    private static final String APP_PREFERENCES = "blacks_stories";
    private static final int STORY = 1;

    private int[] mIds;
    private String[] mTitles;
    private String[] mStories;
    private String[] mFullStories;

    private SharedPreferences mSharedPreferences;
    private ArrayList<String> mListOfSolved;

    private TextView mNotUnsolvedStories;

    private ArrayList<Story> mStoriesList = new ArrayList<>();

    private StoryAdapter mStoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mIds = getResources().getIntArray(R.array.ids);
        mTitles = getResources().getStringArray(R.array.titles);
        mStories = getResources().getStringArray(R.array.stories);
        mFullStories = getResources().getStringArray(R.array.full_stories);

        mSharedPreferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSharedPreferences.getString("list_of_solved", "").equals(""))
            mListOfSolved = new ArrayList<>();
        else
            mListOfSolved = new ArrayList<>(Arrays.asList(mSharedPreferences.getString("list_of_solved", "").split(",")));

        View rootView = inflater.inflate(R.layout.fragment_common, container, false);

        mNotUnsolvedStories = (TextView) rootView.findViewById(R.id.textView);

        setInitialData();

        if (!mStoriesList.isEmpty()) {

            ListView storiesListView = (ListView) rootView.findViewById(R.id.listView);

            mStoryAdapter = new StoryAdapter(getActivity(), R.layout.item_stories, mStoriesList);
            storiesListView.setAdapter(mStoryAdapter);

            storiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                    Story selectedStory = (Story) parent.getItemAtPosition(position);

                    startActivityForResult(new Intent(getActivity(), StoryActivity.class)
                            .putExtra("story", selectedStory.toArray())
                            .putExtra("is_solved", false), STORY);
                }
            });
        }

        return rootView;
    }

    private void setInitialData() {
        mStoriesList.clear();
        for (int i = 0; i < mIds.length; i++)
            if (!mListOfSolved.contains(Integer.toString(mIds[i])))
                mStoriesList.add(new Story(mIds[i], mTitles[i], mStories[i], mFullStories[i]));

        if (mStoriesList.isEmpty()) {
            mNotUnsolvedStories.setText(R.string.not_unsolved_stories);
            new AlertDialog.Builder(getActivity())
                    .setCancelable(true)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(R.string.not_unsolved_stories)
                    .setMessage(R.string.more_stories_in_the_full)
                    .setPositiveButton(R.string.buy, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=com.divofmod.blackstoriesfull")));
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STORY) {
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra(StoryActivity.IS_SOLVED, false))
                    mListOfSolved.add(data.getStringExtra(StoryActivity.ID));
                mSharedPreferences.edit().putString("list_of_solved", mListOfSolved.toString().substring(1, mListOfSolved.toString().length() - 1).replace(" ", "")).apply();
                setInitialData();
                mStoryAdapter.notifyDataSetChanged();
            }
        }
    }
}