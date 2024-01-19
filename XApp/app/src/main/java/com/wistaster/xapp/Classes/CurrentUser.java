package com.wistaster.xapp.Classes;

import com.google.firebase.auth.FirebaseUser;

public class CurrentUser {
    private static FirebaseUser currUser;

    public static FirebaseUser getCurrUser() {
        return currUser;
    }

    public static void setCurrUser(FirebaseUser currUser) {
        CurrentUser.currUser = currUser;
    }
}
