package com.voyager.sayara.landingpage.model.drawerList;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * Created by User on 15-May-18.
 */

public class DrawerItems {


    private int ID;
    private String Name;
    private String Discription;
    private String ImageUrl;
    private int ImageId;
    protected String type;
    private boolean enabled=true;
    private int notify;
    private MaterialDrawableBuilder.IconValue iconDraw;

    public DrawerItems() {
    }

    public int getNotify() {
        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MaterialDrawableBuilder.IconValue getIconDraw() {
        return iconDraw;
    }

    public void setIconDraw(MaterialDrawableBuilder.IconValue iconDraw) {
        this.iconDraw = iconDraw;
    }

}
