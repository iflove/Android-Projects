package com.androidz.base_modules.lib_baseAndroid.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.androidz.base_modules.lib_logger.Logg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 资源获取工具类
 *
 * @author rentianlong
 */
public final class ResUtil {

    private static final String TAG = "ResUtil";
    @SuppressLint("StaticFieldLeak")
    private static Application sApplication = null;
    private static Resources sResources = null;
    private static String sPackageName = null;

    public static void inject(@NonNull Application application, String pkgName) {
        ResUtil.sApplication = application;
        sResources = application.getResources();
        sPackageName = pkgName;
    }

    public static int getViewId(String idName) {
        int id = View.NO_ID;
        try {
            Class<?> cls = Class.forName(sPackageName + ".R$" + "id");
            id = cls.getField(idName).getInt(cls);
        } catch (NoSuchFieldException e) {
            Logg.e(TAG, "NoSuchFieldException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int getDrawableId(String idName) {
        int id = View.NO_ID;
        try {
            Class<?> cls = Class.forName(sPackageName + ".R$" + "drawable");
            id = cls.getField(idName).getInt(cls);
        } catch (Exception ignored) {
        }
        return id;
    }

    public static int getStringId(String idName) {
        int id = View.NO_ID;
        try {
            Class<?> cls = Class.forName(sPackageName + ".R$" + "string");
            id = cls.getField(idName).getInt(cls);
        } catch (NoSuchFieldException e) {
            Logg.e(TAG, "NoSuchFieldException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int getXmlId(String idName) {
        int id = View.NO_ID;
        try {
            Class<?> cls = Class.forName(sPackageName + ".R$" + "xml");
            id = cls.getField(idName).getInt(cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int[] getArrayRes(@ArrayRes int id) {
        return sApplication.getResources().getIntArray(id);
    }

    public static String[] getStringArray(@ArrayRes int id) {
        return sApplication.getResources().getStringArray(id);
    }

    public static int[] getResourcesIdArray(@ArrayRes int id) {
        TypedArray typedArray = sApplication.getResources().obtainTypedArray(id);
        int length = typedArray.length();
        int[] resArray = new int[length];
        for (int i = 0; i < length; i++) {
            int resourceId = typedArray.getResourceId(i, 0);
            resArray[i] = resourceId;
        }
        typedArray.recycle();
        return resArray;
    }

    public static Integer[] getResourcesIdIntegerArray(@ArrayRes int id) {
        TypedArray typedArray = sApplication.getResources().obtainTypedArray(id);
        int length = typedArray.length();
        Integer[] resArray = new Integer[length];
        for (int i = 0; i < length; i++) {
            int resourceId = typedArray.getResourceId(i, 0);
            resArray[i] = resourceId;
        }
        typedArray.recycle();
        return resArray;
    }

    public static List<Integer> getResourcesIdList(@ArrayRes int id) {
        return Arrays.asList(getResourcesIdIntegerArray(id));
    }

    public static List<String> getListString(@ArrayRes int id) {
        return Arrays.asList(sApplication.getResources().getStringArray(id));
    }

    public static List<String> getMutableListString(@ArrayRes int id) {
        List<String> l = new ArrayList<>();
        l.addAll(Arrays.asList(sApplication.getResources().getStringArray(id)));
        return l;
    }

    public static String getString(@StringRes int id) {
        return sApplication.getResources().getString(id);
    }

    public static String getString(String idName) {
        int id = getStringId(idName);
        if (id == View.NO_ID) {
            return "";
        }
        return getString(id);
    }

    public static String getStringFormat(@StringRes int id, Object... args) {
        return String.format(sApplication.getResources().getString(id), args);
    }

    public static String getStringFormat(String string, Object... args) {
        return String.format(string, args);
    }

    public static int getColor(@ColorRes int id) {
        return sApplication.getResources().getColor(id);
    }

    public static int getColorId(String idName) {
        int id = View.NO_ID;
        try {
            Class<?> cls = Class.forName(sPackageName + ".R$" + "color");
            id = cls.getField(idName).getInt(cls);
        } catch (Exception ignored) {
        }
        return id;
    }

    public static Drawable getDrawable(@DrawableRes int id) {
        return sApplication.getResources().getDrawable(id);
    }

    public static int getDimensionPixelOffset(@DimenRes int id) {
        return sResources.getDimensionPixelOffset(id);
    }

    public static float getDimension(@DimenRes int id) {
        return sResources.getDimension(id);
    }

    public static Resources getResources() {
        return sResources;
    }

    public static int getDimensionPixelSize(@DimenRes int id) {
        return sResources.getDimensionPixelSize(id);
    }
}
