package com.allever.daymatter.utils

import android.annotation.TargetApi
import android.app.ActivityManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import android.provider.Settings
import android.util.Log
import android.widget.Toast

object SystemUtils {
    private val isDebug = true
    fun log(message: String) {
        if (isDebug) {
            Log.e("breeze", message)
        }
    }

    @TargetApi(11)
    fun enableStrictMode() {
        if (hasGingerbread()) {
            val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyLog()
            val vmPolicyBuilder = StrictMode.VmPolicy.Builder()
                    .detectAll().penaltyLog()

            if (hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen()
                // TODO : Add our activities later
                // vmPolicyBuilder
                // .setClassInstanceLimit(ImageGridActivity.class, 1)
                // .setClassInstanceLimit(ImageDetailActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build())
            StrictMode.setVmPolicy(vmPolicyBuilder.build())
        }
    }

    fun hasFroyo(): Boolean {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
    }

    fun hasGingerbread(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
    }

    fun hasHoneycomb(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
    }

    fun hasHoneycombMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1
    }

    fun hasICS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
    }

    fun hasJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }


    /**
     * 获取是否自动调节亮度
     *
     * @param context
     * @return
     */
    fun isAutoBrightness(context: Context): Boolean {
        val contentResolver = context.contentResolver

        var autoBrightness = false
        try {
            autoBrightness = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        } catch (e: Settings.SettingNotFoundException) {

        }

        return autoBrightness

    }

    /**
     * 获取屏幕亮度
     *
     * @param context
     * @return
     */
    fun getBrightness(context: Context): Int {
        var brightness = 200
        try {
            brightness = Settings.System.getInt(context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Settings.SettingNotFoundException) {
        }

        return brightness
    }

    /**
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     *
     * @param context
     * @param mode
     */
    fun setBrightnessMode(context: Context, mode: Int) {
        Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, mode)
    }

    /**
     * 设置当前屏幕亮度值 0--255，并使之生效
     */
    fun setBrightness(context: Context, bright: Int) {
        // 保存设置的屏幕亮度值
        Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, bright)
    }

    /**
     * 保存亮度设置状态
     *
     * @param resolver
     * @param brightness
     */
    fun saveBrightness(resolver: ContentResolver, brightness: Int) {
        val uri = Settings.System
                .getUriFor("screen_brightness")
        Settings.System.putInt(resolver, "screen_brightness",
                brightness)
        resolver.notifyChange(uri, null)
    }


    fun startWebView(context: Context, uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Sorry, Your mobile can't be supported", Toast.LENGTH_LONG).show()
        }

    }

    /**
     * 获取应用详情
     *
     * @param context
     * @param packageName
     */
    fun showAppDetails(context: Context, packageName: String) {
        val mSCHEME = "package"
        /**
         * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
         */
        val appPkgName21 = "com.android.settings.ApplicationPkgName"
        /**
         * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
         */
        val appPkgName22 = "pkg"
        /**
         * InstalledAppDetails所在包名
         */
        val aPPDefaultPackageName = "com.android.settings"
        /**
         * InstalledAppDetails类名
         */
        val aPPDefailsClassName = "com.android.settings.InstalledAppDetails"

        val intent = Intent()
        val apiLevel = Build.VERSION.SDK_INT
        if (apiLevel >= 9) {
            // 2.3（ApiLevel 9）以上，使用SDK提供的接口
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            val uri = Uri.fromParts(mSCHEME, packageName, null)
            intent.data = uri
        } else {
            // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
            // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
            val appPkgName = if (apiLevel == 8) appPkgName22 else appPkgName21
            intent.action = Intent.ACTION_VIEW
            intent.setClassName(aPPDefaultPackageName, aPPDefailsClassName)
            intent.putExtra(appPkgName, packageName)
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 检测服务是否已启动
     *
     * @param context
     * @param className
     * @return
     */
    fun isServiceRunning(context: Context, className: String): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val list = am.getRunningServices(50)

        for (info in list) {
            if (info.service.className == className) {
                return true
            }
        }

        return false
    }

    /**
     * 检查是否安装某包
     *
     * @param context
     * @param packageName 包名
     * @return
     */
    fun isAppExist(context: Context, packageName: String): Boolean {
        try {
            context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        } catch (e: Exception) {
            return false
        }

        return true
    }

    /**
     * 判断程序是否安装（通过intent-filter验证）
     *
     * @param context
     * @param intent
     * @return
     */
    fun isAppExist(context: Context, intent: Intent): Boolean {
        var infos: List<ResolveInfo>? = null
        try {
            infos = context.packageManager.queryIntentActivities(intent, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return infos != null && infos.size > 0
    }

}
