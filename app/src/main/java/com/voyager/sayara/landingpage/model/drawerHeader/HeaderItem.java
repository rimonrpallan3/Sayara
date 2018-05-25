package com.voyager.sayara.landingpage.model.drawerHeader;

import com.voyager.sayara.landingpage.model.drawerList.DrawerItems;

/**
 * Created by User on 25-May-18.
 */

public class HeaderItem extends DrawerItems{

    public final static String TAG_NAME = "HeaderItem";

    public HeaderItem(){
        super();
        this.type = TAG_NAME;
    }

    private String userName;
    private String ImageUrl;

    @Override
    public String getImageUrl() {
        return ImageUrl;
    }

    @Override
    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
