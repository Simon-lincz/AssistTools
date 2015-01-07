package com.fornut.assisttools.misc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.fornut.assisttools.R;
import com.fornut.assisttools.views.QuickSwitchBase;

public class QuickSwitchesUtils {

    private static boolean DBG = false;
    private static String TAG = "AssistTools-QuickSwitchesUtils";

    public final static String SWITCH_SPLIT = ",";

    /**
     * 获取所有到开关，但是这些开关都只能在一个地方显示
     * 
     * @param context
     * @return HashMap<String, QuickSwitchBase>
     */
    public static HashMap<String, QuickSwitchBase> loadAllQuickSwitches(
            Context context) {
        String[] allSwitchNames = context.getResources().getStringArray(
                R.array.allquickswitches);
        HashMap<String, QuickSwitchBase> map = new HashMap<String, QuickSwitchBase>();
        for (int index = 0; index < allSwitchNames.length; index++) {
            Log.d(TAG, "index " + index + " switchName "
                    + allSwitchNames[index]);
            QuickSwitchBase base = getQuickSwitchByName(context,
                    allSwitchNames[index]);
            map.put(allSwitchNames[index], base);
        }
        return map;
    }

    /**
     * 通过反射机制加载开关
     * 
     * @param context
     * @param classname
     * @return QuickSwitchBase
     */
    public static QuickSwitchBase getQuickSwitchByName(Context context,
            String classname) {
        String classFullName = QuickSwitchBase.class.getPackage().getName()
                + "." + classname;
        Log.d(TAG, "getQuickSwitchByName: " + classFullName);
        try {
            Class t = Class.forName(classFullName);
            // 利用反射机制获取开关,其中getConstructor的参数就是构造函数的参数
            Constructor con = t.getConstructor(Context.class);
            QuickSwitchBase switchBase = (QuickSwitchBase) con
                    .newInstance(context);
            if (!switchBase.isQuickSwitchSupported()) {
                return null;
            }
            Log.d(TAG, "getQuickSwitchByName success.");
            return switchBase;
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "NoSuchMethodException ");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "ClassNotFoundException ");
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "InstantiationException ");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "IllegalAccessException ");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "IllegalArgumentException ");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "InvocationTargetException ");
            e.printStackTrace();
        }
        // 之后这边要返回一个默认按钮！！
        return null;
    }

    public static ArrayList<String> getDisplaySwitchList(Context context) {
        ArrayList<String> switchlist = new ArrayList<String>();
        String switchList_Sequence = SharedPreferenceUtils.getInstance(context)
                .getDisplaySwitchList();
        String[] display_switchlist;
        if (switchList_Sequence == null) {
            display_switchlist = context.getResources().getStringArray(
                    R.array.default_display_switchlist);
        } else {
            display_switchlist = switchList_Sequence.split(SWITCH_SPLIT);
        }
        for (int index = 0; index < display_switchlist.length; index++) {
            switchlist.add(display_switchlist[index]);
        }
        return switchlist;
    }

    public static void saveDisplaySwitchList(Context context,
            ArrayList<String> switchNames) {
        StringBuffer displayList = new StringBuffer();
        for (int i = 0; i < switchNames.size(); i++) {
            String classFullName = switchNames.get(i);
            displayList.append(classFullName);
            displayList.append(SWITCH_SPLIT);
        }
        Log.d(TAG, "displayList " + displayList.toString());
        SharedPreferenceUtils.getInstance(context).saveDisplaySwitchList(
                displayList.toString());
    }
}
