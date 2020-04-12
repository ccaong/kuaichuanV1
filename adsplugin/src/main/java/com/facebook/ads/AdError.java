// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

import android.text.TextUtils;

public class AdError
{
    public static final int NETWORK_ERROR_CODE = 1000;
    public static final int NO_FILL_ERROR_CODE = 1001;
    public static final int LOAD_TOO_FREQUENTLY_ERROR_CODE = 1002;
    public static final int SERVER_ERROR_CODE = 2000;
    public static final int INTERNAL_ERROR_CODE = 2001;
    public static final int MEDIATION_ERROR_CODE = 3001;
    public static final AdError NETWORK_ERROR;
    public static final AdError NO_FILL;
    public static final AdError LOAD_TOO_FREQUENTLY;
    public static final AdError SERVER_ERROR;
    public static final AdError INTERNAL_ERROR;
    public static final AdError MEDIATION_ERROR;
    @Deprecated
    public static final AdError MISSING_PROPERTIES;
    private final int a;
    private final String b;
    
    public AdError(final int a, String b) {
        if (TextUtils.isEmpty((CharSequence)b)) {
            b = "unknown error";
        }
        this.a = a;
        this.b = b;
    }
    
    public int getErrorCode() {
        return this.a;
    }
    
    public String getErrorMessage() {
        return this.b;
    }
    
    static {
        NETWORK_ERROR = new AdError(1000, "Network Error");
        NO_FILL = new AdError(1001, "No Fill");
        LOAD_TOO_FREQUENTLY = new AdError(1002, "Ad was re-loaded too frequently");
        SERVER_ERROR = new AdError(2000, "Server Error");
        INTERNAL_ERROR = new AdError(2001, "Internal Error");
        MEDIATION_ERROR = new AdError(3001, "Mediation Error");
        MISSING_PROPERTIES = new AdError(2002, "Native ad failed to load due to missing properties");
    }
}
