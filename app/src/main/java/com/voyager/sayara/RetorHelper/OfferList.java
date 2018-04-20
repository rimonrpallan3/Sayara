package com.voyager.sayara.RetorHelper;

import java.util.List;

/**
 * Created by User on 10-Apr-18.
 */

public class OfferList {
    int success;
    List<Result> result;
    List<Args> args;

    public OfferList() {
    }

    public OfferList(int success, List<Result> result, List<Args> args) {
        this.success = success;
        this.result = result;
        this.args = args;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public List<Args> getArgs() {
        return args;
    }

    public void setArgs(List<Args> args) {
        this.args = args;
    }
}
