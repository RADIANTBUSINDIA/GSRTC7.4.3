package com.gsrtc.mobileweb.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.gsrtc.mobileweb.R;
import com.gsrtc.mobileweb.utils.net.NetworkException;


/**
 * Created by Ajith on 3/22/2017.
 */

public class ActivityUtil {

    public enum ActivityTransitionAnimation{
        DEFAULT, NO_ANIMATION, SLIDE_LEFT_RIGHT;
    }

    public static void openNewActivity(Activity currentActivity, Class targetActivity){
        Intent intent = new Intent(currentActivity, targetActivity);
        currentActivity.startActivity(intent);
    }

    /**
     * Clears all previous activities from android stack
     * @param activity
     * @param newActivity
     * @param extras
     */
    public static void openNewActivityAsOnlyActivity(Activity activity, Class newActivity, Bundle extras){
        Intent intent = new Intent(activity, newActivity);
        if(extras!=null)
            intent.putExtras(extras);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void openNewActivity(Activity activity, Class newActivity, Bundle extras){
        Intent intent = new Intent(activity, newActivity);
        if(extras!=null)
            intent.putExtras(extras);
        activity.startActivity(intent);
    }

    public static void openNewActivityForResult(Activity activity, Class newActivity, Bundle extras, int request_code){
        Intent intent = new Intent(activity, newActivity);
        if(extras!=null)
            intent.putExtras(extras);
        activity.startActivityForResult(intent, request_code);
    }

    public static void openNewActivity(Context activity, Class newActivity, Bundle extras){
        Intent intent = new Intent(activity, newActivity);
        if(extras!=null)
            intent.putExtras(extras);
        activity.startActivity(intent);
    }

    public static void showToast(Context context, String message, int duration){
        Toast.makeText(context, message, duration).show();
    }

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showErrorToast(Activity activity, Exception e){
        if(e instanceof NetworkException){
            showNoNetworkToast(activity);
        }
        else{
            showToast(activity, "Server Error");
        }
    }

    public static void showErrorToast(Activity activity, Exception e, String defaultMsg){
        if(e instanceof NetworkException){
            showNoNetworkToast(activity);
        }
        else{
            showToast(activity,defaultMsg);
        }
    }

    public static void showNoNetworkToast(Activity context){
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.no_internet_toast,
                (ViewGroup) context.findViewById(R.id.toast_container));

        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static void showSnackBar(View containerView, String message){
        final Snackbar snackbar = Snackbar.make(containerView, message, Snackbar.LENGTH_LONG).setDuration(5000);
        snackbar.show();
    }

    public static void loadLoadingImage(Context context, ImageView imageView){
        Glide.with(context)
                .load(R.drawable.loading)
                .into(imageView);
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AppBarLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
