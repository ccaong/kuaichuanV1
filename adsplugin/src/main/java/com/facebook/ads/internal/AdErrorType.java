// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal;

import com.facebook.ads.AdError;

public enum AdErrorType
{
    UNKNOWN_ERROR(-1, "unknown error", false), 
    NETWORK_ERROR(1000, "Network Error", true), 
    NO_FILL(1001, "No Fill", true), 
    LOAD_TOO_FREQUENTLY(1002, "Ad was re-loaded too frequently", true), 
    DISABLED_APP(1005, "App is disabled from making ad requests", true), 
    SERVER_ERROR(2000, "Server Error", true), 
    INTERNAL_ERROR(2001, "Internal Error", true), 
    START_BEFORE_INIT(2004, "initAd must be called before startAd", true), 
    AD_REQUEST_FAILED(1111, "Facebook Ads SDK request for ads failed", false), 
    AD_REQUEST_TIMEOUT(1112, "Facebook Ads SDK request for ads timed out", false), 
    PARSER_FAILURE(1201, "Failed to parse Facebook Ads SDK delivery response", false), 
    UNKNOWN_RESPONSE(1202, "Unknown Facebook Ads SDK delivery response type", false), 
    ERROR_MESSAGE(1203, "Facebook Ads SDK delivery response Error message", true), 
    NO_AD_PLACEMENT(1302, "Facebook Ads SDK returned no ad placements", false);
    
    private final int a;
    private final String b;
    private final boolean c;
    
    private AdErrorType(final int a, final String b, final boolean c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public int getErrorCode() {
        return this.a;
    }
    
    public String getDefaultErrorMessage() {
        return this.b;
    }
    
    boolean a() {
        return this.c;
    }
    
    public b getAdErrorWrapper(final String s) {
        return new b(this, s);
    }
    
    public AdError getAdError(final String s) {
        return new b(this, s).b();
    }
    
    public static AdErrorType adErrorTypeFromCode(final int n) {
        return adErrorTypeFromCode(n, AdErrorType.UNKNOWN_ERROR);
    }
    
    public static AdErrorType adErrorTypeFromCode(final int n, final AdErrorType adErrorType) {
        for (final AdErrorType adErrorType2 : values()) {
            if (adErrorType2.getErrorCode() == n) {
                return adErrorType2;
            }
        }
        return adErrorType;
    }
}
