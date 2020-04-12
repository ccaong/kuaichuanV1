package com.thinkd.xshare.server;


import android.app.Activity;

import com.thinkd.xshare.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;

/**
 * @author CCAONG
 */

public class AndroidDownloadServer extends NanoHTTPD {

    private Activity mActivity;
    public static final String DOWNLOAD_URL = "/download/Xshare.apk";

    public AndroidDownloadServer(int port, Activity activity) {
        super(port);
        this.mActivity = activity;
    }

    public AndroidDownloadServer(String hostname, int port, Activity activity) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = null;
        if(DOWNLOAD_URL.equals(session.getUri())) {
            //请求是文件，则下载文件
            return responseFile(session);

        } else {
            //如果请求的不是下载，则返回主页
            try {
                InputStream is = this.mActivity.getAssets().open("index.html");
                msg = IOStreamUtils.inputStreamToString(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            msg = msg.replaceAll("\\{file_share\\}", this.mActivity.getResources().getString(R.string.app_name));
            msg = msg.replaceAll("\\{file_body\\}", this.mActivity.getResources().getString(R.string.html_1));
            msg = msg.replaceAll("\\{file_download\\}", this.mActivity.getResources().getString(R.string.download));
            return newFixedLengthResponse(msg);
        }
    }

    /**
     *
     * @param session
     * @return
     */
    public Response responseFile(IHTTPSession session) {
        try {
            //文件输入流
            File file = new File("/sdcard/Xshare.apk");
            FileInputStream fis = new FileInputStream(file);
            // 返回OK，同时传送文件
            return newFixedLengthResponse(Response.Status.OK,
                    "application/octet-stream", fis, fis.available());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response404(session, null);
    }

    public Response response404(IHTTPSession session, String url) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append("Sorry,Can't Found" + url + " !");
        builder.append("</body></html>\n");
        return newFixedLengthResponse(builder.toString());
    }
}
