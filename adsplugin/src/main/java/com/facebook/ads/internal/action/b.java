package com.facebook.ads.internal.action;

import java.util.Iterator;
import org.json.JSONException;
import android.util.Log;
import org.json.JSONObject;
import android.text.TextUtils;
import java.util.Map;
import android.net.Uri;
import android.content.Context;

public class b
{
    private static final String a;

    public static a a(final Context context, final String s, final Uri uri, final Map<String, String> map) {
        final String authority = uri.getAuthority();
        final String queryParameter = uri.getQueryParameter("video_url");
        if (!TextUtils.isEmpty((CharSequence)uri.getQueryParameter("data"))) {
            try {
                final JSONObject jsonObject = new JSONObject(uri.getQueryParameter("data"));
                final Iterator keys = jsonObject.keys();
                while (keys.hasNext()) {
                    final String s2 = (String)keys.next();
                    map.put(s2, jsonObject.getString(s2));
                }
            }
            catch (JSONException ex) {
                Log.w(b.a, "Unable to parse json data in AdActionFactory.", (Throwable)ex);
            }
        }
        final String s3 = authority;
        if (s3 == null) {
            return null;
        }
        switch (s3) {
            case "store": {
                if (queryParameter != null) {
                    return null;
                }
                return new c(context, s, uri, map);
            }
            case "open_link": {
                return new d(context, s, uri, map);
            }
            case "passthrough": {
                return new e(context, s, uri, map);
            }
            default: {
                return new f(context, s, uri);
            }
        }
    }

    public static boolean a(final String s) {
        return "store".equalsIgnoreCase(s) || "open_link".equalsIgnoreCase(s);
    }

    static {
        a = b.class.getSimpleName();
    }
}
