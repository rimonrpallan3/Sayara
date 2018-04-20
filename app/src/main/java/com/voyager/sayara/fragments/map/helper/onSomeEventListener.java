package com.voyager.sayara.fragments.map.helper;

import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by User on 04-Oct-17.
 */

public interface onSomeEventListener {
        public void someEvent(int s, LinearLayout serviceLayout, FrameLayout searchLoc);
        public void mapDetailBuff(String nameStart,
                                  String nameEnd,
                                  String distanceKm,
                                  String addressStart,
                                  String addressEnd);
}
