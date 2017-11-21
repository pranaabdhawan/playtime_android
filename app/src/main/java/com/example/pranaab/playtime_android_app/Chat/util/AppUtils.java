package com.example.pranaab.playtime_android_app.Chat.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by pranaab on 2017-11-20.
 */

public class AppUtils {

    public static void showToast(Context context, @StringRes int text, boolean isLong) {
        showToast(context, context.getString(text), isLong);
    }

    public static void showToast(Context context, String text, boolean isLong) {
        Toast.makeText(context, text, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }
}
