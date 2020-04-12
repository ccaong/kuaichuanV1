// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.ads.internal.util;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.ArrayList;
import android.graphics.Bitmap;

class u implements j
{
    private static final short[] a;
    private static final byte[] b;
    
    @Override
    public Bitmap a(final Bitmap bitmap, final float n) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        int[] array;
        try {
            array = new int[width * height];
        }
        catch (OutOfMemoryError outOfMemoryError) {
            return null;
        }
        bitmap.getPixels(array, 0, width, 0, 0, width, height);
        final int a = ac.a;
        final ArrayList list = new ArrayList<Callable<Object>>(a);
        final ArrayList list2 = new ArrayList<Callable<Object>>(a);
        for (int i = 0; i < a; ++i) {
            list.add(new a(array, width, height, (int)n, a, i, 1));
            list2.add(new a(array, width, height, (int)n, a, i, 2));
        }
        try {
            ac.b.invokeAll((Collection<? extends Callable<Object>>)list);
        }
        catch (InterruptedException ex) {
            return null;
        }
        try {
            ac.b.invokeAll((Collection<? extends Callable<Object>>)list2);
        }
        catch (InterruptedException ex2) {
            return null;
        }
        try {
            return Bitmap.createBitmap(array, width, height, Bitmap.Config.ARGB_8888);
        }
        catch (OutOfMemoryError outOfMemoryError2) {
            return null;
        }
    }
    
    private static void b(final int[] array, final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        final int n7 = n - 1;
        final int n8 = n2 - 1;
        final int n9 = n3 * 2 + 1;
        final short n10 = u.a[n3];
        final byte b = u.b[n3];
        final int[] array2 = new int[n9];
        if (n6 == 1) {
            final int n11 = n5 * n2 / n4;
            for (int n12 = (n5 + 1) * n2 / n4, i = n11; i < n12; ++i) {
                long n21;
                long n20;
                long n19;
                long n18;
                long n17;
                long n16;
                long n15;
                long n14;
                long n13 = n14 = (n15 = (n16 = (n17 = (n18 = (n19 = (n20 = (n21 = 0L)))))));
                int n22 = n * i;
                for (int j = 0; j <= n3; ++j) {
                    array2[j] = array[n22];
                    n14 += (array[n22] >>> 16 & 0xFF) * (j + 1);
                    n13 += (array[n22] >>> 8 & 0xFF) * (j + 1);
                    n15 += (array[n22] & 0xFF) * (j + 1);
                    n19 += (array[n22] >>> 16 & 0xFF);
                    n20 += (array[n22] >>> 8 & 0xFF);
                    n21 += (array[n22] & 0xFF);
                }
                for (int k = 1; k <= n3; ++k) {
                    if (k <= n7) {
                        ++n22;
                    }
                    array2[k + n3] = array[n22];
                    n14 += (array[n22] >>> 16 & 0xFF) * (n3 + 1 - k);
                    n13 += (array[n22] >>> 8 & 0xFF) * (n3 + 1 - k);
                    n15 += (array[n22] & 0xFF) * (n3 + 1 - k);
                    n16 += (array[n22] >>> 16 & 0xFF);
                    n17 += (array[n22] >>> 8 & 0xFF);
                    n18 += (array[n22] & 0xFF);
                }
                int n23 = n3;
                int n24 = n3;
                if (n24 > n7) {
                    n24 = n7;
                }
                int n25 = n24 + i * n;
                int n26 = i * n;
                for (int l = 0; l < n; ++l) {
                    array[n26] = (int)((array[n26] & 0xFF000000) | (n14 * n10 >>> b & 0xFFL) << 16 | (n13 * n10 >>> b & 0xFFL) << 8 | (n15 * n10 >>> b & 0xFFL));
                    ++n26;
                    final long n27 = n14 - n19;
                    final long n28 = n13 - n20;
                    final long n29 = n15 - n21;
                    int n30 = n23 + n9 - n3;
                    if (n30 >= n9) {
                        n30 -= n9;
                    }
                    final int n31 = n30;
                    final long n32 = n19 - (array2[n31] >>> 16 & 0xFF);
                    final long n33 = n20 - (array2[n31] >>> 8 & 0xFF);
                    final long n34 = n21 - (array2[n31] & 0xFF);
                    if (n24 < n7) {
                        ++n25;
                        ++n24;
                    }
                    array2[n31] = array[n25];
                    final long n35 = n16 + (array[n25] >>> 16 & 0xFF);
                    final long n36 = n17 + (array[n25] >>> 8 & 0xFF);
                    final long n37 = n18 + (array[n25] & 0xFF);
                    n14 = n27 + n35;
                    n13 = n28 + n36;
                    n15 = n29 + n37;
                    if (++n23 >= n9) {
                        n23 = 0;
                    }
                    final int n38 = n23;
                    n19 = n32 + (array2[n38] >>> 16 & 0xFF);
                    n20 = n33 + (array2[n38] >>> 8 & 0xFF);
                    n21 = n34 + (array2[n38] & 0xFF);
                    n16 = n35 - (array2[n38] >>> 16 & 0xFF);
                    n17 = n36 - (array2[n38] >>> 8 & 0xFF);
                    n18 = n37 - (array2[n38] & 0xFF);
                }
            }
        }
        else if (n6 == 2) {
            final int n39 = n5 * n / n4;
            for (int n40 = (n5 + 1) * n / n4, n41 = n39; n41 < n40; ++n41) {
                long n50;
                long n49;
                long n48;
                long n47;
                long n46;
                long n45;
                long n44;
                long n43;
                long n42 = n43 = (n44 = (n45 = (n46 = (n47 = (n48 = (n49 = (n50 = 0L)))))));
                int n51 = n41;
                for (int n52 = 0; n52 <= n3; ++n52) {
                    array2[n52] = array[n51];
                    n43 += (array[n51] >>> 16 & 0xFF) * (n52 + 1);
                    n42 += (array[n51] >>> 8 & 0xFF) * (n52 + 1);
                    n44 += (array[n51] & 0xFF) * (n52 + 1);
                    n48 += (array[n51] >>> 16 & 0xFF);
                    n49 += (array[n51] >>> 8 & 0xFF);
                    n50 += (array[n51] & 0xFF);
                }
                for (int n53 = 1; n53 <= n3; ++n53) {
                    if (n53 <= n8) {
                        n51 += n;
                    }
                    array2[n53 + n3] = array[n51];
                    n43 += (array[n51] >>> 16 & 0xFF) * (n3 + 1 - n53);
                    n42 += (array[n51] >>> 8 & 0xFF) * (n3 + 1 - n53);
                    n44 += (array[n51] & 0xFF) * (n3 + 1 - n53);
                    n45 += (array[n51] >>> 16 & 0xFF);
                    n46 += (array[n51] >>> 8 & 0xFF);
                    n47 += (array[n51] & 0xFF);
                }
                int n54 = n3;
                int n55 = n3;
                if (n55 > n8) {
                    n55 = n8;
                }
                int n56 = n41 + n55 * n;
                int n57 = n41;
                for (int n58 = 0; n58 < n2; ++n58) {
                    array[n57] = (int)((array[n57] & 0xFF000000) | (n43 * n10 >>> b & 0xFFL) << 16 | (n42 * n10 >>> b & 0xFFL) << 8 | (n44 * n10 >>> b & 0xFFL));
                    n57 += n;
                    final long n59 = n43 - n48;
                    final long n60 = n42 - n49;
                    final long n61 = n44 - n50;
                    int n62 = n54 + n9 - n3;
                    if (n62 >= n9) {
                        n62 -= n9;
                    }
                    final int n63 = n62;
                    final long n64 = n48 - (array2[n63] >>> 16 & 0xFF);
                    final long n65 = n49 - (array2[n63] >>> 8 & 0xFF);
                    final long n66 = n50 - (array2[n63] & 0xFF);
                    if (n55 < n8) {
                        n56 += n;
                        ++n55;
                    }
                    array2[n63] = array[n56];
                    final long n67 = n45 + (array[n56] >>> 16 & 0xFF);
                    final long n68 = n46 + (array[n56] >>> 8 & 0xFF);
                    final long n69 = n47 + (array[n56] & 0xFF);
                    n43 = n59 + n67;
                    n42 = n60 + n68;
                    n44 = n61 + n69;
                    if (++n54 >= n9) {
                        n54 = 0;
                    }
                    final int n70 = n54;
                    n48 = n64 + (array2[n70] >>> 16 & 0xFF);
                    n49 = n65 + (array2[n70] >>> 8 & 0xFF);
                    n50 = n66 + (array2[n70] & 0xFF);
                    n45 = n67 - (array2[n70] >>> 16 & 0xFF);
                    n46 = n68 - (array2[n70] >>> 8 & 0xFF);
                    n47 = n69 - (array2[n70] & 0xFF);
                }
            }
        }
    }
    
    static {
        a = new short[] { 512, 512, 456, 512, 328, 456, 335, 512, 405, 328, 271, 456, 388, 335, 292, 512, 454, 405, 364, 328, 298, 271, 496, 456, 420, 388, 360, 335, 312, 292, 273, 512, 482, 454, 428, 405, 383, 364, 345, 328, 312, 298, 284, 271, 259, 496, 475, 456, 437, 420, 404, 388, 374, 360, 347, 335, 323, 312, 302, 292, 282, 273, 265, 512, 497, 482, 468, 454, 441, 428, 417, 405, 394, 383, 373, 364, 354, 345, 337, 328, 320, 312, 305, 298, 291, 284, 278, 271, 265, 259, 507, 496, 485, 475, 465, 456, 446, 437, 428, 420, 412, 404, 396, 388, 381, 374, 367, 360, 354, 347, 341, 335, 329, 323, 318, 312, 307, 302, 297, 292, 287, 282, 278, 273, 269, 265, 261, 512, 505, 497, 489, 482, 475, 468, 461, 454, 447, 441, 435, 428, 422, 417, 411, 405, 399, 394, 389, 383, 378, 373, 368, 364, 359, 354, 350, 345, 341, 337, 332, 328, 324, 320, 316, 312, 309, 305, 301, 298, 294, 291, 287, 284, 281, 278, 274, 271, 268, 265, 262, 259, 257, 507, 501, 496, 491, 485, 480, 475, 470, 465, 460, 456, 451, 446, 442, 437, 433, 428, 424, 420, 416, 412, 408, 404, 400, 396, 392, 388, 385, 381, 377, 374, 370, 367, 363, 360, 357, 354, 350, 347, 344, 341, 338, 335, 332, 329, 326, 323, 320, 318, 315, 312, 310, 307, 304, 302, 299, 297, 294, 292, 289, 287, 285, 282, 280, 278, 275, 273, 271, 269, 267, 265, 263, 261, 259 };
        b = new byte[] { 9, 11, 12, 13, 13, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16, 17, 17, 17, 17, 17, 17, 17, 18, 18, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24 };
    }
    
    private static class a implements Callable<Void>
    {
        private final int[] a;
        private final int b;
        private final int c;
        private final int d;
        private final int e;
        private final int f;
        private final int g;
        
        public a(final int[] a, final int b, final int c, final int d, final int e, final int f, final int g) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
        }
        
        public Void call() {
            b(this.a, this.b, this.c, this.d, this.e, this.f, this.g);
            return null;
        }
    }
}
