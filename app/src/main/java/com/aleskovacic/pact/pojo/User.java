package com.aleskovacic.pact.pojo;

/**
 * Created by Alex on 03.02.2017.
 */

public class User {

    private String id;
    private String username;
    private String usersurname;
    private String email;
    private String password;
    private String fbId;
    private Object blokingstate;
    private String photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsersurname() {
        return usersurname;
    }

    public void setUsersurname(String usersurname) {
        this.usersurname = usersurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public Object getBlokingstate() {
        return blokingstate;
    }

    public void setBlokingstate(Object blokingstate) {
        this.blokingstate = blokingstate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}

