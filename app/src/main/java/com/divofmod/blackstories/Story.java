package com.divofmod.blackstories;

class Story {
    private String mId;
    private String mTitle;
    private String mStory;
    private String mFullStory;

    Story(int id, String title, String story, String fullStory) {
        mId = Integer.toString(id);
        mTitle = title;
        mStory = story;
        mFullStory = fullStory;
    }

    String getId() {
        return mId;
    }

    String getTitle() {
        return mTitle;
    }

    String getStory() {
        return mStory;
    }

    String getFullStory() {
        return mFullStory;
    }

    String[] toArray() {
        return new String[]{mId, mTitle, mStory, mFullStory};
    }
}
