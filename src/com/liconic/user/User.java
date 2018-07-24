package com.liconic.user;

public class User {

    private int userID = 0;
    private String userName = "";
    private int userRole = 0;
    private String userSecName = "";
    private String login = "";
    private String userEmail = "";
    private int userActivity = 1;
    private String conError = "";

    public User(int id, String uname, int urole, String usecname, String ulogin, String uemail, int uact) {
        userID = id;

        if (uname != null) {
            userName = uname;
        }

        userRole = urole;

        if (usecname != null) {
            userSecName = usecname;
        }

        if (ulogin != null) {
            login = ulogin;
        }

        if (uemail != null) {
            userEmail = uemail;
        }

        userActivity = uact;
    }

    public User(String con_error) {
        if (con_error != null) {
            conError = con_error;
        }
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserRole() {
        return userRole;
    }

    public String getUserSecName() {
        return userSecName;
    }

    public String getLogin() {
        return login;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getUserActivity() {
        return userActivity;
    }

    public String getConError() {
        return conError;
    }

}
