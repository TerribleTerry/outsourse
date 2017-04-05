
package com.aleskovacic.pact.pojo;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;



public class Comment {
    private static SimpleDateFormat inputFormat;
    private static SimpleDateFormat outputFormat;
    private String id;
    private String fromUserId;
    private String replytoUserid;
    private String replyonpostId;
    private String username;
    private String usersurname;
    private String commentText;
    private String messagelat;
    private String messagelong;
    private String commenttime;



    public Comment() {
        this.inputFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:ss");
        inputFormat.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));
        outputFormat = new SimpleDateFormat("MMM d, HH:ss", Locale.getDefault());
    }


    /**
     *
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     *     The fromUserId
     */
    public String getFromUserId() {
        return fromUserId;
    }

    /**
     *
     * @param fromUserId
     *     The fromUserId
     */
    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    /**
     *
     * @return
     *     The replytoUserid
     */
    public String getReplytoUserid() {
        return replytoUserid;
    }

    /**
     *
     * @param replytoUserid
     *     The replytoUserid
     */
    public void setReplytoUserid(String replytoUserid) {
        this.replytoUserid = replytoUserid;
    }

    /**
     *
     * @return
     *     The replyonpostId
     */
    public String getReplyonpostId() {
        return replyonpostId;
    }

    /**
     *
     * @param replyonpostId
     *     The replyonpostId
     */
    public void setReplyonpostId(String replyonpostId) {
        this.replyonpostId = replyonpostId;
    }

    /**
     *
     * @return
     *     The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     *     The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     *     The usersurname
     */
    public String getUsersurname() {
        return usersurname;
    }

    /**
     *
     * @param usersurname
     *     The usersurname
     */
    public void setUsersurname(String usersurname) {
        this.usersurname = usersurname;
    }

    /**
     *
     * @return
     *     The commentText
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     *
     * @param commentText
     *     The commentText
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    /**
     *
     * @return
     *     The messagelat
     */
    public String getMessagelat() {
        return messagelat;
    }

    /**
     *
     * @param messagelat
     *     The messagelat
     */
    public void setMessagelat(String messagelat) {
        this.messagelat = messagelat;
    }

    /**
     *
     * @return
     *     The messagelong
     */
    public String getMessagelong() {
        return messagelong;
    }

    /**
     *
     * @param messagelong
     *     The messagelong
     */
    public void setMessagelong(String messagelong) {
        this.messagelong = messagelong;
    }

    /**
     *
     * @return
     *     The commenttime
     */
    public String getCommenttime() {

        try {
            return outputFormat.format(inputFormat.parse(commenttime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

        //return commenttime;


    }

    /**
     *
     * @param commenttime
     *     The commenttime
     */
    public void setCommenttime(String commenttime) {
        this.commenttime = commenttime;
    }

}
