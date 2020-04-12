//
// Decompiled by Procyon v0.5.30
//

package com.facebook.ads.internal.bpackage;

import java.io.Serializable;
import java.util.Iterator;
import java.util.ArrayList;
import android.content.Context;
import java.util.List;
import android.view.View;
import android.os.Bundle;
import com.facebook.ads.internal.util.ad;

public final class a implements ad<Bundle>
{
    private final View a;
    private final List<d> b;
    private final Context c;
    private c d;

    public a(final Context c, final View a, final List<b> list) {
        this.c = c;
        this.a = a;
        this.b = new ArrayList<d>(list.size());
        final Iterator<b> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.b.add(new d(iterator.next()));
        }
        this.d = new c();
    }

    public a(final Context c, final View a, final List<b> list, final Bundle bundle) {
        this.c = c;
        this.a = a;
        this.b = new ArrayList<d>(list.size());
        final ArrayList parcelableArrayList = bundle.getParcelableArrayList("TESTS");
        for (int i = 0; i < list.size(); ++i) {
            this.b.add(new d(list.get(i), (Bundle)parcelableArrayList.get(i)));
        }
        this.d = (c)bundle.getSerializable("STATISTICS");
    }

    public void a() {
        this.d.a();
    }

    public void a(final double n, final double n2) {
        if (n2 >= 0.0) {
            this.d.b(n, n2);
        }
        final double a = e.a(this.a, this.c);
        this.d.a(n, a);
        final Iterator<d> iterator = this.b.iterator();
        while (iterator.hasNext()) {
            iterator.next().a(n, a);
        }
    }

    public c b() {
        return this.d;
    }

    @Override
    public Bundle getSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putSerializable("STATISTICS", (Serializable)this.d);
        final ArrayList<Bundle> list = new ArrayList<Bundle>(this.b.size());
        final Iterator<d> iterator = this.b.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getSaveInstanceState());
        }
        bundle.putParcelableArrayList("TESTS", (ArrayList)list);
        return bundle;
    }
}
