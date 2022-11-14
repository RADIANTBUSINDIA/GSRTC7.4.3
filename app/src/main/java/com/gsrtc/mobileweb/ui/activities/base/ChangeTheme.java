package com.gsrtc.mobileweb.ui.activities.base;

import android.annotation.SuppressLint;
import android.view.Window;

import com.gsrtc.mobileweb.R;

import java.util.Calendar;

/**
 * Created by Ajith on 2/22/2019.
 */

public class ChangeTheme
{

    public static int GET_BG_DRAWABLE(Window paramWindows)
    {
        Calendar cal = Calendar.getInstance();
        int mDrawable;
        @SuppressLint("WrongConstant") int hourofday=cal.get(Calendar.HOUR_OF_DAY);

        if(hourofday>=6 & hourofday <18)
        {
            mDrawable = (R.drawable.background_img);
        }
        else if(hourofday>=18 & hourofday <6)
        {
            mDrawable =  (R.drawable.background_img);
        }
        else
        {
            mDrawable = (R.drawable.background_img);
        }

        return mDrawable;
    }
}
