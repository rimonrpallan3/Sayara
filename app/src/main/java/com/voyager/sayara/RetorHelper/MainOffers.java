package com.voyager.sayara.RetorHelper;

import java.util.List;

/**
 * Created by User on 10-Apr-18.
 */

public class MainOffers {

    String id_offertype;
    String name;
    String parent_id;
    String status;
    List<Child> childList;

    public MainOffers() {
    }

    public String getId_offertype() {
        return id_offertype;
    }

    public void setId_offertype(String id_offertype) {
        this.id_offertype = id_offertype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }
}
