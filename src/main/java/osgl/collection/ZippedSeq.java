package osgl.collection;

/*-
 * #%L
 * OSGL Core
 * %%
 * Copyright (C) 2017 OSGL (Open Source General Library)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import osgl.$;
import osgl.Option;

import java.util.Iterator;

class ZippedSeq<A, B> implements Sequence<T2<A, B>> {

    private Iterable<A> a;
    private Iterable<B> b;

    private Option<A> defA = $.none();
    private Option<B> defB = $.none();


    ZippedSeq(Iterable<A> a, Iterable<B> b) {
        this.a = a;
        this.b = b;
    }

    ZippedSeq(Iterable<A> a, Iterable<B> b, A defA, B defB) {
        this(a, b);
        this.defA = $.some(defA);
        this.defB = $.some(defB);
    }

    @Override
    public Iterator<T2<A, B>> iterator() {
        final Iterator<A> ia = a.iterator();
        final Iterator<B> ib = b.iterator();
        if (defA.isDefined()) {
            return new ZippedIterator<>(ia, ib, defA.get(), defB.get());
        } else {
            return new ZippedIterator<>(ia, ib);
        }
    }
    
}
