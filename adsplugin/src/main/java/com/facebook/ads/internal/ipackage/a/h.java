// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.ipackage.a;

import android.os.AsyncTask;

public class h extends AsyncTask<l, Void, n> implements c
{
    private a a;
    private b b;
    private Exception c;
    
    public h(final a a, final b b) {
        this.a = a;
        this.b = b;
    }
    
    protected n doInBackground(final l... array) {
        try {
            if (array != null && array.length > 0) {
                return this.a.a(array[0]);
            }
            throw new IllegalArgumentException("DoHttpRequestTask takes exactly one argument of type HttpRequest");
        }
        catch (Exception c) {
            this.c = c;
            this.cancel(true);
            return null;
        }
    }

    protected void onPostExecute(final n n) {
        this.b.a(n);
    }

    protected void onCancelled() {
        this.b.a(this.c);
    }
    
    public void a(final l l) {
        super.execute(new l[] { l });
    }
}
