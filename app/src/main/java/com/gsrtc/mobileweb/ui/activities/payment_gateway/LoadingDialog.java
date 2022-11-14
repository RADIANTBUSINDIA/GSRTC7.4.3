package com.gsrtc.mobileweb.ui.activities.payment_gateway;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Ajith on 6/21/2019.
 */

public class LoadingDialog {
    static ProgressDialog progressDialog;


    public static void showLoadingDialog(Context context, String message) {

        if (!(progressDialog != null && progressDialog.isShowing())) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);

            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            progressDialog.show();
        }
    }

    public static void cancelLoading() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.cancel();

    }
}
