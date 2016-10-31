package com.androidadvance.androidsurvey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hein Htet on 10/27/2016.
 */

public class Choice implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("goTo")
    @Expose
    private String goTo;
    @SerializedName("new_suggestion")
    @Expose
    private boolean allowNewSuggestion = false;

    public Choice(){

    }

    public Choice(String value){
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGoTo() {
        return goTo;
    }

    public void setGoTo(String goTo) {
        this.goTo = goTo;
    }

    public boolean allowNewSuggestion() {
        return allowNewSuggestion;
    }

    public void setAllowNewSuggestion(boolean allowNewSuggestion) {
        this.allowNewSuggestion = allowNewSuggestion;
    }
}
