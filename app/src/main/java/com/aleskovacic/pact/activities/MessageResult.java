package com.aleskovacic.pact.activities;

/**
 * Created by Alex on 03.12.2016.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class MessageResult implements Parcelable {
    final static String LOG_TAG = "myLogs";
    public static final String EXTRA_LIST_ARRAY = "extralist";
    public static final String EXTRA_OBJECT = "extraobject";
    private static SimpleDateFormat inputFormat;
    private static SimpleDateFormat outputFormat;
    private String id;
    private String fromUserId;
    private String lastname;
    private String usersurname;
    private String mtext;
    private String messagelat;
    private String messagelong;
    private String messagetime;

    public MessageResult() {
        this.inputFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:ss");
        inputFormat.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));
        outputFormat = new SimpleDateFormat("MMM d, HH:ss", Locale.getDefault());
    }

    // конструктор, считывающий данные из Parcel
    protected MessageResult(Parcel in) {
        Log.d(LOG_TAG, "MyObject(Parcel parcel)");
        id = in.readString();
        fromUserId = in.readString();
        lastname = in.readString();
        usersurname = in.readString();
        mtext = in.readString();
        messagelat = in.readString();
        messagelong = in.readString();
        messagetime = in.readString();
    }


    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     *
     * @return
     * The fromUserId
     */
    public String getFromUserId() {
        return fromUserId;
    }

    /**
     *
     * @param fromUserId
     * The fromUserId
     */
    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }
    /**
     *
     * @return
     * The lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     *
     * @param lastname
     * The lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    /**
     *
     * @return
     * The usersurname
     */
    public String getUsersurname() {
        return usersurname;
    }

    /**
     *
     * @param usersurname
     * The usersurname
     */
    public void setUsersurname(String usersurname) {
        this.usersurname = usersurname;
    }

    /**
     * @return The mtext
     */
    public String getMtext() {
        return mtext;
    }

    /**
     * @param mtext The mtext
     */
    public void setMtext(String mtext) {
        this.mtext = mtext;
    }



    /**
     * @return The messagelat
     */
    public String getMessagelat() {
        return messagelat;
    }

    /**
     * @param messagelat The messagelat
     */
    public void setMessagelat(String messagelat) {
        this.messagelat = messagelat;
    }

    /**
     * @return The messagelong
     */
    public String getMessagelong() {
        return messagelong;
    }

    /**
     * @param messagelong The messagelong
     */
    public void setMessagelong(String messagelong) {
        this.messagelong = messagelong;
    }

    /**
     * @return The messagetime
     */
    public String getMessagetime() {
        try {
            return outputFormat.format(inputFormat.parse(messagetime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
       // return messagetime;
    }



    /**
     * @param messagetime The messagetime
     */
    public void setMessagetime(String messagetime) {
        this.messagetime = messagetime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(LOG_TAG, "writeToParcel");
        dest.writeString(id);
        dest.writeString(fromUserId);
        dest.writeString(lastname);
        dest.writeString(lastname);
        dest.writeString(usersurname);
        dest.writeString(mtext);
        dest.writeString(messagelat);
        dest.writeString(messagelong);
        dest.writeString(messagetime);
    }

    public static final Parcelable.Creator<MessageResult> CREATOR = new Parcelable.Creator<MessageResult>() {
        // распаковываем объект из Parcel
        public MessageResult createFromParcel(Parcel in) {
            Log.d(LOG_TAG, "createFromParcel");
            return new MessageResult(in);
        }

        @Override
        public MessageResult[] newArray(int size) {
            return new MessageResult[size];
        }
    };



}
