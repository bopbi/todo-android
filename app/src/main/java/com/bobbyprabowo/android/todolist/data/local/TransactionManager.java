package com.bobbyprabowo.android.todolist.data.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bobbyadiprabowo on 26/02/17.
 */

public class TransactionManager {

    private static final String TIMELINE_TRANSACTION_STATUS = "timeline-transaction-status";
    private static final String TIMELINE_LAST_REMOTE_ID = "timeline-last-remote-id";

    public static void saveTimelineTransactionStatus(Context context, boolean status){
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.INTENT_UPDATE_TIMELINE, Context.MODE_PRIVATE);

        sharedPref.edit().putBoolean(TIMELINE_TRANSACTION_STATUS, status)
                .apply();
    }

    public static boolean getTimelineTransactionStatus(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.INTENT_UPDATE_TIMELINE, Context.MODE_PRIVATE);

        return sharedPref.getBoolean(TIMELINE_TRANSACTION_STATUS, false);
    }
}
