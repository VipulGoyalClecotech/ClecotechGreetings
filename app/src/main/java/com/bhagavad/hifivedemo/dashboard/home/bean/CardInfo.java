/*
 * Copyright (c) 2017 Bambora
 */

package com.bhagavad.hifivedemo.dashboard.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dlight on 2016-08-09.
 */
public class CardInfo implements Parcelable {

    public static final Creator<CardInfo> CREATOR
            = new Creator<CardInfo>() {

        @Override
        public CardInfo createFromParcel(Parcel in) {
            return new CardInfo(in);
        }

        @Override
        public CardInfo[] newArray(int size) {
            return new CardInfo[size];
        }
    };

    private String code; //Token
    private String email;
    private String name;
    private String saveCard="";

    public CardInfo() {
    }

    private CardInfo(Parcel parcel) {
        code = parcel.readString();
        email = parcel.readString();
        name = parcel.readString();
        saveCard = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(code);
        parcel.writeString(email);
        parcel.writeString(name);
        parcel.writeString(saveCard);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //region Getters and Setters

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSaveCard() {
        return saveCard;
    }

    public void setSaveCard(String saveCard) {
        this.saveCard = saveCard;
    }




    //endregion

    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("email", email);
            json.put("name", name);
            json.put("saveCard", saveCard);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
