package com.gsrtc.mobileweb.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SecureApplication extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSecurity(this);
    }



    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkSecurity(this);
    }

    private static boolean isDeveloperModeEnabled(Context context) {
        return Settings.Secure.getInt(
                context.getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1;
    }

    private static boolean isEmulator() {
        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        String brand = Build.BRAND;
        String hardware = Build.HARDWARE;
        String fingerprint = Build.FINGERPRINT;
        String product = Build.PRODUCT;
        String device = Build.DEVICE;

        return (product.contains("sdk") ||
                model.contains("Emulator") ||
                model.contains("Genymotion") ||
                manufacturer.contains("Genymotion") ||
                brand.contains("generic") ||
                hardware.contains("goldfish") ||
                hardware.contains("ranchu") ||
                fingerprint.contains("generic") ||
                device.contains("generic") ||
                product.contains("vbox86p"));
    }

    private static boolean isUSBConnected(Context context) {
        return Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.ADB_ENABLED, 0) == 1;
    }

    public static void checkSecurity(final Context context) {
        StringBuilder securityWarnings = new StringBuilder();

        if (isDeveloperModeEnabled(context)) {
            Log.e("Security", "Developer Mode is enabled!");
            securityWarnings.append("⚠ Developer Mode is enabled!\n");
        }

        if (isEmulator()) {
            Log.e("Security", "Running on an Emulator!");
            securityWarnings.append("⚠ App is running on an Emulator!\n");
        }

        if (isUSBConnected(context)) {
            Log.e("Security", "USB Debugging is enabled!");
            securityWarnings.append("⚠ USB Debugging is enabled!\n");
        }
//to run in debug mode
      securityWarnings = new StringBuilder();


        if (securityWarnings.length() > 0) {
            showSecurityDialog(context, securityWarnings.toString());
        }
    }

    private static void showSecurityDialog(final Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("Security Warning ⚠")
                .setMessage(message )
                .setCancelable(false)
               /* .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue using the app
                        dialog.dismiss();
                    }
                })*/
                .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Exit the app
                        
                        System.exit(0);
                    }
                })
                .show();
    }

}
