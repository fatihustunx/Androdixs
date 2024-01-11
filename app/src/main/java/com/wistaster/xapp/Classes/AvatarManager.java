package com.wistaster.xapp.Classes;

import com.wistaster.xapp.R;

public class AvatarManager {
    public static int getAvatarMan(int r){
        switch (r){
            case 1 : return R.drawable.man;
            case 2 : return R.drawable.man2;
            case 3 : return R.drawable.man3;
            case 4 : return R.drawable.man4;
            case 5 : return R.drawable.man5;

            default: return R.drawable.heart_on;
        }
    }

    public static int getAvatarWoman(int r){
        switch (r){
            case 1 : return R.drawable.woman;
            case 2 : return R.drawable.woman2;
            case 3 : return R.drawable.woman3;
            case 4 : return R.drawable.woman4;
            case 5 : return R.drawable.woman5;

            default: return R.drawable.heart_on;
        }
    }
}
