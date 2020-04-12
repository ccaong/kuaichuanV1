//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdView.Type;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class u extends a {
    private final v c;
    private Type d;
    private boolean e;
    private boolean f;
    private boolean g;
    private View h;
    private List<View> i;

    public u(Context var1, b var2, com.facebook.ads.internal.rewarded_video.a var3, v var4) {
        super(var1, var2, var3);
        this.c = var4;
    }

    public void a(Type var1) {
        this.d = var1;
    }

    public void a(View var1) {
        this.h = var1;
    }

    public void a(List<View> var1) {
        this.i = var1;
    }

    public void a(boolean var1) {
        this.e = var1;
    }

    public void b(boolean var1) {
        this.f = var1;
    }

    public void c(boolean var1) {
        this.g = var1;
    }

    protected void a(Map<String, String> var1) {
        if(this.c != null) {
            if(this.a != null) {
                var1.put("mil", String.valueOf(this.a.a()));
                var1.put("eil", String.valueOf(this.a.b()));
                var1.put("eil_source", this.a.c());
            }

            if(this.d != null) {
                var1.put("nti", String.valueOf(this.d.getValue()));
            }

            if(this.e) {
                var1.put("nhs", Boolean.TRUE.toString());
            }

            if(this.f) {
                var1.put("nmv", Boolean.TRUE.toString());
            }

            if(this.g) {
                var1.put("nmvap", Boolean.TRUE.toString());
            }

            if(this.h != null && this.c.e()) {
                var1.put("view", this.b(this.h));
            }

            if(this.h != null && this.c.d()) {
                var1.put("snapshot", this.d(this.h));
            }

            this.c.a(var1);
        }
    }

    private String b(View var1) {
        try {
            JSONObject var2 = this.c(var1);
            return var2.toString();
        } catch (JSONException var3) {
            return "Json exception";
        }
    }

    private JSONObject c(View var1) throws JSONException {
        JSONObject var2 = new JSONObject();
        var2.putOpt("id", Integer.valueOf(var1.getId()));
        var2.putOpt("class", var1.getClass());
        var2.putOpt("origin", String.format("{x:%d, y:%d}", new Object[]{Integer.valueOf(var1.getTop()), Integer.valueOf(var1.getLeft())}));
        var2.putOpt("size", String.format("{h:%d, w:%d}", new Object[]{Integer.valueOf(var1.getHeight()), Integer.valueOf(var1.getWidth())}));
        boolean var3 = this.i != null && this.i.contains(var1);
        var2.putOpt("clickable", Boolean.valueOf(var3));
        String var4 = "unknown";
        if(var1 instanceof Button) {
            var4 = "button";
        } else if(var1 instanceof TextView) {
            var4 = "text";
        } else if(var1 instanceof ImageView) {
            var4 = "image";
        } else if(var1 instanceof MediaView) {
            var4 = "mediaview";
        } else if(var1 instanceof ViewGroup) {
            var4 = "viewgroup";
        }

        var2.putOpt("type", var4);
        if(var1 instanceof ViewGroup) {
            ViewGroup var5 = (ViewGroup)var1;
            JSONArray var6 = new JSONArray();

            for(int var7 = 0; var7 < var5.getChildCount(); ++var7) {
                var6.put(this.c(var5.getChildAt(var7)));
            }

            var2.putOpt("list", var6);
        }

        return var2;
    }

    private String d(View var1) {
        if(var1.getWidth() > 0 && var1.getHeight() > 0) {
            try {
                Bitmap var2 = Bitmap.createBitmap(var1.getWidth(), var1.getHeight(), Config.ARGB_8888);
                var2.setDensity(var1.getResources().getDisplayMetrics().densityDpi);
                Canvas var3 = new Canvas(var2);
                var1.draw(var3);
                ByteArrayOutputStream var4 = new ByteArrayOutputStream();
                var2.compress(CompressFormat.JPEG, this.c.h(), var4);
                byte[] var5 = var4.toByteArray();
                return Base64.encodeToString(var5, 0);
            } catch (Exception var6) {
                return "";
            } catch (OutOfMemoryError error) {
                return "";
            }
        } else {
            return "";
        }
    }
}
