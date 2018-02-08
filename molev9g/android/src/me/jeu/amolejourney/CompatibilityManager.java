package me.jeu.amolejourney;

import android.os.Build;

public class CompatibilityManager {

    /**
     * Get the current Android API level.
     */
    public static int getSdkVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }
    /**
     * Determine if the device is running API level 19 or higher.
     */
    public static boolean isKitKat() {
        return getSdkVersion() >= Build.VERSION_CODES.KITKAT;
    }
}
