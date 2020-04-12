//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.ads.internal.ipackage.b.apackage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

class d {
    static void a(File var0) throws IOException {
        if(var0.exists()) {
            if(!var0.isDirectory()) {
                throw new IOException("File " + var0 + " is not directory!");
            }
        } else {
            boolean var1 = var0.mkdirs();
            if(!var1) {
                throw new IOException(String.format("Directory %s can\'t be created", new Object[]{var0.getAbsolutePath()}));
            }
        }

    }

    static List<File> b(File var0) {
        Object var1 = new LinkedList();
        File[] var2 = var0.listFiles();
        if(var2 != null) {
            var1 = Arrays.asList(var2);
            Collections.sort((List)var1, new d.a());
        }

        return (List)var1;
    }

    static void c(File var0) throws IOException {
        if(var0.exists()) {
            long var1 = System.currentTimeMillis();
            boolean var3 = var0.setLastModified(var1);
            if(!var3) {
                d(var0);
                if(var0.lastModified() < var1) {
                    throw new IOException("Error set last modified date to " + var0);
                }
            }
        }

    }

    static void d(File var0) throws IOException {
        long var1 = var0.length();
        if(var1 == 0L) {
            e(var0);
        } else {
            RandomAccessFile var3 = new RandomAccessFile(var0, "rwd");
            var3.seek(var1 - 1L);
            byte var4 = var3.readByte();
            var3.seek(var1 - 1L);
            var3.write(var4);
            var3.close();
        }
    }

    private static void e(File var0) throws IOException {
        if(!var0.delete() || !var0.createNewFile()) {
            throw new IOException("Error recreate zero-size file " + var0);
        }
    }

    private static final class a implements Comparator<File> {
        private a() {
        }

        public int compare(File var1, File var2) {
            return this.a(var1.lastModified(), var2.lastModified());
        }

        private int a(long var1, long var3) {
            return var1 < var3?-1:(var1 == var3?0:1);
        }
    }
}
