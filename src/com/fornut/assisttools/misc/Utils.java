package com.fornut.assisttools.misc;

import java.lang.reflect.Field;

import android.content.Context;

public final class Utils {
    
    /**
     * get statusbar height
     */
    public static int getStatusbarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
            return sbar;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
