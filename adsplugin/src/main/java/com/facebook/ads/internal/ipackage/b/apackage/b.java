//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.ads.internal.ipackage.b.apackage;

import com.facebook.ads.internal.ipackage.b.l;
import com.facebook.ads.internal.ipackage.b.a;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class b implements a {
    private final com.facebook.ads.internal.ipackage.b.apackage.a b;
    public File a;
    private RandomAccessFile c;

    public b(File var1, com.facebook.ads.internal.ipackage.b.apackage.a var2) throws l {
        try {
            if(var2 == null) {
                throw new NullPointerException();
            } else {
                this.b = var2;
                File var3 = var1.getParentFile();
                d.a(var3);
                boolean var4 = var1.exists();
                this.a = var4?var1:new File(var1.getParentFile(), var1.getName() + ".download");
                this.c = new RandomAccessFile(this.a, var4?"r":"rw");
            }
        } catch (IOException var5) {
            throw new l("Error using file " + var1 + " as disc cache", var5);
        }
    }

    public synchronized int a() throws l {
        try {
            return (int)this.c.length();
        } catch (IOException var2) {
            throw new l("Error reading length of file " + this.a, var2);
        }
    }

    public synchronized int a(byte[] var1, long var2, int var4) throws l {
        try {
            this.c.seek(var2);
            return this.c.read(var1, 0, var4);
        } catch (IOException var6) {
            throw new l(String.format("Error reading %d bytes with offset %d from file[%d bytes] to buffer[%d bytes]", new Object[]{Integer.valueOf(var4), Long.valueOf(var2), Integer.valueOf(this.a()), Integer.valueOf(var1.length)}), var6);
        }
    }

    public synchronized void a(byte[] var1, int var2) throws l {
        try {
            if(this.d()) {
                throw new l("Error append cache: cache file " + this.a + " is completed!");
            } else {
                this.c.seek((long)this.a());
                this.c.write(var1, 0, var2);
            }
        } catch (IOException var5) {
            String var4 = "Error writing %d bytes to %s from buffer with size %d";
            throw new l(String.format(var4, new Object[]{Integer.valueOf(var2), this.c, Integer.valueOf(var1.length)}), var5);
        }
    }

    public synchronized void b() throws l {
        try {
            this.c.close();
            this.b.a(this.a);
        } catch (IOException var2) {
            throw new l("Error closing file " + this.a, var2);
        }
    }

    public synchronized void c() throws l {
        if(!this.d()) {
            this.b();
            String var1 = this.a.getName().substring(0, this.a.getName().length() - ".download".length());
            File var2 = new File(this.a.getParentFile(), var1);
            boolean var3 = this.a.renameTo(var2);
            if(!var3) {
                throw new l("Error renaming file " + this.a + " to " + var2 + " for completion!");
            } else {
                this.a = var2;

                try {
                    this.c = new RandomAccessFile(this.a, "r");
                } catch (IOException var5) {
                    throw new l("Error opening " + this.a + " as disc cache", var5);
                }
            }
        }
    }

    public synchronized boolean d() {
        return !this.a(this.a);
    }

    private boolean a(File var1) {
        return var1.getName().endsWith(".download");
    }
}
