package com.thinkd.xshare.server;

import android.app.Activity;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.ui.activity.Firebase;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * 下载Xshare安装包
 * @author CCAONG
 */
public class DownloadResUriHandler implements ResUriHandler {

    public static final String DOWNLOAD_PREFIX = "/download/";

    private Activity mActivity;

    public DownloadResUriHandler(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public boolean matches(String uri) {
        return uri.startsWith(DOWNLOAD_PREFIX);
    }
    @Override
    public void handler(Request request) {

        File file = new File("/sdcard/Xshare.apk");

        FileInfo fileInfo = new FileInfo(file.getPath(), file.length());

        Socket socket = request.getUnderlySocket();
        OutputStream os = null;
        PrintStream printStream = null;
        try {
            os = socket.getOutputStream();
            printStream = new PrintStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fileInfo == null) {
            //not exist this file
            printStream.println("HTTP/1.1 404 NotFound");
            printStream.println();
        } else {
            printStream.println("HTTP/1.1 200 OK");
            printStream.println("Content-Length:" + fileInfo.getSize());
            printStream.println("Content-Type:application/octet-stream");
            printStream.println();

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 发送文件给客户端
            try {
                int len = 0;
                byte[] bytes = new byte[2048];
                while ((len = fis.read(bytes)) != -1) {
                    printStream.write(bytes, 0, len);
                }
                Firebase.getInstance(getApplicationContext()).logEvent("hotspot页面", "传输成功");
            } catch (IOException e) {
                Firebase.getInstance(getApplicationContext()).logEvent("hotspot页面", "传输失败");
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                        fis = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        printStream.flush();
        printStream.close();
    }
    @Override
    public void destroy() {
        this.mActivity = null;
    }

}
