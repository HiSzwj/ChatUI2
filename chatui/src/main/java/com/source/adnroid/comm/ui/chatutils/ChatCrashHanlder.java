package com.source.adnroid.comm.ui.chatutils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by zzw on 2018/4/20.
 */

public class ChatCrashHanlder implements Thread.UncaughtExceptionHandler {
   private String  TAG="ChatCrashHanlder";
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static ChatCrashHanlder INSTANCE = new ChatCrashHanlder();
    // 程序的Context对象
    private Context mContext;

    private ChatCrashHanlder() {
    }

    public static ChatCrashHanlder getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 异常捕获
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // 跳转到崩溃提示Activity
            Log.i(TAG,"需要跳转");
/*            Intent intent = new Intent(mContext, CrashDialog.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            System.exit(0);// 关闭已奔溃的app进程*/
        }
    }

    /**
     * 自定义错误捕获
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 收集错误信息
        getCrashInfo(ex);

        return true;
    }

    /**
     * 收集错误信息
     *
     * @param ex
     */
    private void getCrashInfo(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String errorMessage = writer.toString();
        Log.i(TAG, "errorMessage==>"+errorMessage);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
          /*  String mFilePath = Environment.getExternalStorageDirectory() + "/" + App.ERROR_FILENAME;
            FileTxt.WirteTxt(mFilePath, FileTxt.ReadTxt(mFilePath) + '\n' + errorMessage);*/
            Log.i(TAG, "哦豁，保存文件到本地...");
        } else {
            Log.i(TAG, "哦豁，说好的SD呢...");
        }

    }
}
