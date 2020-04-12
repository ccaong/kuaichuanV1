// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.view.dpackage.b;

import android.content.ActivityNotFoundException;
import android.util.Log;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.view.dpackage.a.a;
import android.net.Uri;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.View;
import android.widget.RelativeLayout;
import android.graphics.RectF;
import android.graphics.Paint;
import android.widget.TextView;
import android.content.Context;

public class e extends n
{
    private final Context b;
    private final String c;
    private final TextView d;
    private final String e;
    private final Paint f;
    private final RectF g;
    
    public e(final Context b, final String c, final String e, final String text) {
        super(b);
        this.b = b;
        this.c = c;
        this.e = e;
        final DisplayMetrics displayMetrics = b.getResources().getDisplayMetrics();
        (this.d = new TextView(this.getContext())).setTextColor(-3355444);
        this.d.setTextSize(16.0f);
        this.d.setPadding((int)(6.0f * displayMetrics.density), (int)(4.0f * displayMetrics.density), (int)(6.0f * displayMetrics.density), (int)(4.0f * displayMetrics.density));
        (this.f = new Paint()).setStyle(Paint.Style.FILL);
        this.f.setColor(-16777216);
        this.f.setAlpha(178);
        this.g = new RectF();
        this.setBackgroundColor(0);
        this.d.setText((CharSequence)text);
        this.addView((View)this.d, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-2, -2));
    }
    
    protected void onDraw(final Canvas canvas) {
        this.g.set(0.0f, 0.0f, (float)this.getWidth(), (float)this.getHeight());
        canvas.drawRoundRect(this.g, 0.0f, 0.0f, this.f);
        super.onDraw(canvas);
    }
    
    @Override
    protected void a_(final com.facebook.ads.internal.view.n n) {
        this.d.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                try {
                    n.getEventBus().a(new a(Uri.parse(com.facebook.ads.internal.view.dpackage.b.e.this.c)));
                    com.facebook.ads.internal.util.g.a(com.facebook.ads.internal.view.dpackage.b.e.this.b, Uri.parse(com.facebook.ads.internal.view.dpackage.b.e.this.c), com.facebook.ads.internal.view.dpackage.b.e.this.e);
                }
                catch (ActivityNotFoundException ex) {
                    Log.e("LearnMorePlugin", "Error while opening " + com.facebook.ads.internal.view.dpackage.b.e.this.c, (Throwable)ex);
                }
            }
        });
    }
}
