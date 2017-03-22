package com.bobbyprabowo.android.todolist.data;

/**
 * Created by bobbyadiprabowo on 22/03/17.
 */

public class Utils {

    public static boolean BooleanFromInt(int value) {
        return value > 0 ? true : false ;
    }

    public static int IntFromBoolean(boolean value) {
        return value == true ? 1 : 0;
    }
}
