// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal;

import com.facebook.ads.AdError;
import android.text.TextUtils;

public class b
{
    private final AdErrorType a;
    private final String b;
    
    public b(final int n, final String s) {
        this(AdErrorType.adErrorTypeFromCode(n), s);
    }
    
    public b(final AdErrorType a, String defaultErrorMessage) {
        if (TextUtils.isEmpty((CharSequence)defaultErrorMessage)) {
            defaultErrorMessage = a.getDefaultErrorMessage();
        }
        this.a = a;
        this.b = defaultErrorMessage;
    }
    
    public AdErrorType a() {
        return this.a;
    }
    
    public AdError b() {
        if (this.a.a()) {
            return new AdError(this.a.getErrorCode(), this.b);
        }
        return new AdError(AdErrorType.UNKNOWN_ERROR.getErrorCode(), AdErrorType.UNKNOWN_ERROR.getDefaultErrorMessage());
    }
}
