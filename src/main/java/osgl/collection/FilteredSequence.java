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

import java.util.Iterator;
import java.util.function.Predicate;

class FilteredSequence<T> extends DelegateSequence<T> {

    private final Predicate<? super T> filter;
    private final FilteredIterator.Type type;

    FilteredSequence(Sequence<? extends T> seq, Predicate<? super T> filter) {
        this(seq, filter, FilteredIterator.Type.ALL);
    }

    FilteredSequence(
            Sequence<? extends T> seq,
            Predicate<? super T> filter,
            FilteredIterator.Type type
    ) {
        super(seq);
        this.filter = $.requireNotNull(filter);
        this.type = $.requireNotNull(type);
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.filter(super.iterator(), filter, type);
    }

    static <T> FilteredSequence<T> of(Sequence<? extends T> seq, Predicate<? super T> filter) {
        return new FilteredSequence<T>(seq, filter);
    }
}
