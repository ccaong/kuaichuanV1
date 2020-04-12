package com.facebook.ads.internal.action;

import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.util.b;
import java.util.Iterator;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.facebook.ads.internal.util.f;
import android.content.ComponentName;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;
import org.json.JSONObject;
import java.util.ArrayList;
import com.facebook.ads.internal.util.i;
import java.util.List;
import android.text.TextUtils;
import java.util.Map;
import android.net.Uri;
import android.content.Context;

public class c extends a
{
    private static final String a;
    private final Context b;
    private final String c;
    private final Uri d;
    private final Map<String, String> e;
    
    public c(final Context b, final String c, final Uri d, final Map<String, String> e) {
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
    
    protected Uri c() {
        final String queryParameter = this.d.getQueryParameter("store_url");
        if (!TextUtils.isEmpty((CharSequence)queryParameter)) {
            return Uri.parse(queryParameter);
        }
        return Uri.parse(String.format("market://details?id=%s", this.d.getQueryParameter("store_id")));
    }
    
    private List<i> f() {
        final String queryParameter = this.d.getQueryParameter("appsite_data");
        if (TextUtils.isEmpty((CharSequence)queryParameter) || "[]".equals(queryParameter)) {
            return null;
        }
        final ArrayList<i> list = new ArrayList<i>();
        try {
            final JSONArray optJSONArray = new JSONObject(queryParameter).optJSONArray("android");
            if (optJSONArray != null) {
                for (int ii = 0; ii < optJSONArray.length(); ++ii) {
                    final i a = i.a(optJSONArray.optJSONObject(ii));
                    if (a != null) {
                        list.add(a);
                    }
                }
            }
        }
        catch (JSONException ex) {
            Log.w(com.facebook.ads.internal.action.c.a, "Error parsing appsite_data", (Throwable)ex);
        }
        return list;
    }
    
    private Intent a(final i i) {
        final Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(268435456);
        if (!TextUtils.isEmpty((CharSequence)i.a()) && !TextUtils.isEmpty((CharSequence)i.b())) {
            intent.setComponent(new ComponentName(i.a(), i.b()));
        }
        if (!TextUtils.isEmpty((CharSequence)i.c())) {
            intent.setData(Uri.parse(i.c()));
        }
        return intent;
    }
    
    private Intent b(final i i) {
        if (TextUtils.isEmpty((CharSequence)i.a())) {
            return null;
        }
        if (!f.a(this.b, i.a())) {
            return null;
        }
        final String c = i.c();
        if (!TextUtils.isEmpty((CharSequence)c) && (c.startsWith("tel:") || c.startsWith("telprompt:"))) {
            return new Intent("android.intent.action.CALL", Uri.parse(c));
        }
        final PackageManager packageManager = this.b.getPackageManager();
        if (TextUtils.isEmpty((CharSequence)i.b()) && TextUtils.isEmpty((CharSequence)c)) {
            return packageManager.getLaunchIntentForPackage(i.a());
        }
        final Intent a = this.a(i);
        final List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(a, PackageManager.MATCH_DEFAULT_ONLY);
        if (a.getComponent() == null) {
            for (final ResolveInfo resolveInfo : queryIntentActivities) {
                if (resolveInfo.activityInfo.packageName.equals(i.a())) {
                    a.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
                    break;
                }
            }
        }
        if (queryIntentActivities.isEmpty() || a.getComponent() == null) {
            return null;
        }
        return a;
    }
    
    protected List<Intent> d() {
        final List<i> f = this.f();
        final ArrayList<Intent> list = new ArrayList<Intent>();
        if (f != null) {
            final Iterator<i> iterator = f.iterator();
            while (iterator.hasNext()) {
                final Intent b = this.b(iterator.next());
                if (b != null) {
                    list.add(b);
                }
            }
        }
        return list;
    }
    
    @Override
    public b.a a() {
        return com.facebook.ads.internal.util.b.a.OPEN_STORE;
    }
    
    @Override
    public void b() {
        this.a(this.b, this.c, this.e);
        final List<Intent> d = this.d();
        if (d != null) {
            for (final Intent intent : d) {
                try {
                    this.b.startActivity(intent);
                    return;
                }
                catch (Exception ex) {
                    Log.d(com.facebook.ads.internal.action.c.a, "Failed to open app intent, falling back", (Throwable)ex);
                    continue;
                }
            }
        }
        this.e();
    }
    
    public void e() {
        try {
            g.a(this.b, this.c(), this.c);
        }
        catch (Exception ex) {
            Log.d(com.facebook.ads.internal.action.c.a, "Failed to open market url: " + this.d.toString(), (Throwable)ex);
            final String queryParameter = this.d.getQueryParameter("store_url_web_fallback");
            if (queryParameter != null && queryParameter.length() > 0) {
                try {
                    g.a(this.b, Uri.parse(queryParameter), this.c);
                }
                catch (Exception ex2) {
                    Log.d(com.facebook.ads.internal.action.c.a, "Failed to open fallback url: " + queryParameter, (Throwable)ex2);
                }
            }
        }
    }
    
    static {
        a = c.class.getSimpleName();
    }
}
