// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.util.Map;
import android.media.AudioManager;
import android.content.Context;

public class aj
{
    public static float a(final Context context) {
        final AudioManager audioManager = (AudioManager)context.getSystemService("audio");
        if (audioManager != null) {
            final int streamVolume = audioManager.getStreamVolume(3);
            final int streamMaxVolume = audioManager.getStreamMaxVolume(3);
            if (streamMaxVolume > 0) {
                return streamVolume * 1.0f / streamMaxVolume;
            }
        }
        return 0.0f;
    }
    
    public static void a(final Map<String, String> map, final boolean b, final boolean b2) {
        map.put("autoplay", b ? "1" : "0");
        map.put("inline", b2 ? "1" : "0");
    }
}
