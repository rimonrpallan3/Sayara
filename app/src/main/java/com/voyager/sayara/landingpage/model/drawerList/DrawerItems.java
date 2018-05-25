package com.voyager.sayara.landingpage.model.drawerList;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * Created by User on 15-May-18.
 */

public class DrawerItems implements Parcelable {


    private int ID;
    private String Name;
    private String Discription;
    private String ImageUrl;
    private int ImageId;
    protected String type;
    private boolean enabled=true;
    private MaterialDrawableBuilder.IconValue iconDraw;

    public DrawerItems() {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeString(this.Name);
        dest.writeString(this.Discription);
        dest.writeString(this.ImageUrl);
        dest.writeInt(this.ImageId);
        dest.writeString(this.type);
        dest.writeByte(this.enabled ? (byte) 1 : (byte) 0);
        dest.writeInt(this.iconDraw == null ? -1 : this.iconDraw.ordinal());
    }

    protected DrawerItems(Parcel in) {
        this.ID = in.readInt();
        this.Name = in.readString();
        this.Discription = in.readString();
        this.ImageUrl = in.readString();
        this.ImageId = in.readInt();
        this.type = in.readString();
        this.enabled = in.readByte() != 0;
        int tmpIconDraw = in.readInt();
        this.iconDraw = tmpIconDraw == -1 ? null : MaterialDrawableBuilder.IconValue.values()[tmpIconDraw];
    }

    public static final Creator<DrawerItems> CREATOR = new Creator<DrawerItems>() {
        @Override
        public DrawerItems createFromParcel(Parcel source) {
            return new DrawerItems(source);
        }

        @Override
        public DrawerItems[] newArray(int size) {
            return new DrawerItems[size];
        }
    };
}
