package com.gsrtc.mobileweb.utils.customdialog;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsrtc.mobileweb.R;

public class CustomisedProgressDialog extends AppCompatActivity
{
    private static ProgressDialog mCstPrgDia=null;
    private static Animation ANIM_CLC_WISE, ANIM_BOUNCE;

    public CustomisedProgressDialog(){

    }

    public static void SHOW_CUST_DIA (Context paramCon, String paramTitle)
    {
        if(mCstPrgDia==null)
        {
            mCstPrgDia = ProgressDialog.show(paramCon, null, null);
            mCstPrgDia.setContentView(R.layout.activity_customised_progress_dialog);
            mCstPrgDia.getWindow().setBackgroundDrawable(null);
            mCstPrgDia.setCancelable(false);
            ANIM_CLC_WISE = AnimationUtils.loadAnimation(paramCon, R.anim.rotate_indefinitely_clock_wise);
            ANIM_BOUNCE = AnimationUtils.loadAnimation(paramCon, R.anim.bounce);
        }
        else
        {
            mCstPrgDia=null;
            mCstPrgDia = ProgressDialog.show(paramCon, null, null);
            mCstPrgDia.setContentView(R.layout.activity_customised_progress_dialog);
            mCstPrgDia.getWindow().setBackgroundDrawable(null);
            mCstPrgDia.setCancelable(false);
            ANIM_CLC_WISE=null;
            ANIM_BOUNCE=null;
            ANIM_CLC_WISE = AnimationUtils.loadAnimation(paramCon, R.anim.rotate_indefinitely_clock_wise);
            ANIM_BOUNCE = AnimationUtils.loadAnimation(paramCon, R.anim.bounce);
        }
        TextView mTvTitle = (TextView)mCstPrgDia.findViewById(R.id.xTvTitle);
        mTvTitle.setText(paramTitle);

        ImageView mIvLoaderRed = (ImageView)mCstPrgDia.findViewById(R.id.xIvLoaderRed);
//  ImageView mIvLoaderBlue = (ImageView)mCstPrgDia.findViewById(R.id.xIvLoaderBlue);
        ImageView mIvgsrtcimage = (ImageView)mCstPrgDia.findViewById(R.id.xIvLoaderLogo);
        mIvLoaderRed.startAnimation(ANIM_CLC_WISE);
        mIvgsrtcimage.startAnimation(ANIM_BOUNCE);
// mIvLoaderGreen.startAnimation(ANIM_CLC_WISE);

    }
    public static void STOP_CUST_DIA()
    {
        try
        {
            ANIM_CLC_WISE.cancel();
        }
        catch(Exception e){}
        try
        {
            ANIM_BOUNCE.cancel();
        }
        catch(Exception e){}
        try
        {
            mCstPrgDia.dismiss();
            mCstPrgDia=null;
        }
        catch(Exception e){}
    }
}