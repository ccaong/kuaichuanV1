// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads;

public class RewardData
{
    private String a;
    private String b;
    
    public RewardData(final String a, final String b) {
        this.a = a;
        this.b = b;
    }
    
    public String getUserID() {
        return this.a;
    }
    
    public String getCurrency() {
        return this.b;
    }
}
