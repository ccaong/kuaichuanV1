// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.cpackage;

import android.graphics.Canvas;
import android.content.ActivityNotFoundException;
import android.util.Log;

import java.util.HashMap;
import android.net.Uri;
import android.view.View;
import android.graphics.Typeface;
import android.widget.TextView;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.Paint;
import com.facebook.ads.internal.view.n;
import android.widget.RelativeLayout;

public class a extends RelativeLayout
{
    private final String a;
    private n b;
    private final Paint c;
    private final RectF d;
    
    public a(final Context context, final String a, final String text, final int color, final n b) {
        super(context);
        this.a = a;
        this.b = b;
        final TextView textView = new TextView(context);
        textView.setTextColor(-1);
        textView.setTextSize(16.0f);
        textView.setText((CharSequence)text);
        textView.setTypeface(Typeface.defaultFromStyle(1));
        this.setGravity(17);
        this.addView((View)textView);
        (this.c = new Paint()).setStyle(Paint.Style.FILL);
        this.c.setColor(color);
        this.d = new RectF();
        this.setBackgroundColor(0);
        this.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                try {
                    final Uri parse = Uri.parse(com.facebook.ads.internal.view.cpackage.a.this.a);
                    com.facebook.ads.internal.view.cpackage.a.this.b.getEventBus().a(new com.facebook.ads.internal.view.dpackage.a.a(parse));
                    final com.facebook.ads.internal.action.a a = com.facebook.ads.internal.action.b.a(com.facebook.ads.internal.view.cpackage.a.this.getContext(), "", parse, new HashMap<String, String>());
                    if (a != null) {
                        a.b();
                    }
                }
                catch (ActivityNotFoundException ex) {
                    Log.e(String.valueOf(a.class), "Error while opening " + com.facebook.ads.internal.view.cpackage.a.this.a, (Throwable)ex);
                }
                catch (Exception ex2) {
                    Log.e(String.valueOf(a.class), "Error executing action", (Throwable)ex2);
                }
            }
        });
    }
    
    protected void onDraw(final Canvas canvas) {
        final float density = this.getContext().getResources().getDisplayMetrics().density;
        this.d.set(0.0f, 0.0f, (float)this.getWidth(), (float)this.getHeight());
        canvas.drawRoundRect(this.d, 10.0f * density, 10.0f * density, this.c);
        super.onDraw(canvas);
    }
}
