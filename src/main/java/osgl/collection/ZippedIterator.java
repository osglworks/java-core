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
import java.util.NoSuchElementException;

class ZippedIterator<A, B> implements Iterator<T2<A, B>> {
    private Iterator<A> a;
    private Iterator<B> b;
    private Option<A> defA = $.none();
    private Option<B> defB = $.none();

    ZippedIterator(Iterator<A> a, Iterator<B> b) {
        this.a = a;
        this.b = b;
    }

    ZippedIterator(Iterator<A> a, Iterator<B> b, A defA, B defB) {
        this(a, b);
        this.defA = $.some(defA);
        this.defB = $.some(defB);
    }

    @Override
    public boolean hasNext() {
        boolean hasA = a.hasNext(), hasB = b.hasNext();
        if (hasA && hasB) {
            return true;
        }
        if (defA.isDefined()) {
            return hasA || hasB;
        } else {
            return false;
        }
    }

    @Override
    public Pair<A, B> next() {
        boolean hasA = a.hasNext(), hasB = b.hasNext();
        if (hasA && hasB) {
            return $.T2(a.next(), b.next());
        }
        if (defA.isDefined()) {
            if (hasA) {
                return $.T2(a.next(), defB.get());
            } else if (hasB) {
                return $.T2(defA.get(), b.next());
            } else {
                throw new NoSuchElementException();
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
