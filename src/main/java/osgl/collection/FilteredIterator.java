package osgl.collection;

/*-
 * #%L
 * Java Tool
 * %%
 * Copyright (C) 2014 - 2017 OSGL (Open Source General Library)
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
import java.util.function.Predicate;

/**
 * A `FilteredIterator` is an iterator that applies a {@link Predicate filter} on another
 * {@link Iterator}.
 *
 * `FilteredIterator` make sure that when iterating through elements only the
 * element passed the filter function will be returned.
 */
class FilteredIterator<T> extends StatefulIterator<T> {

    /**
     * The type defines the semantic how filter works.
     */
    enum Type {

        /**
         * apply filter to all elements and return
         * all elements that matches filter.
         */
        ALL,

        /**
         * Take elements that matches filter and stop at the
         * first non-matching element.
         */
        WHILE,

        /**
         * Skip elements that matches filter and start to
         * return element from the first non-matching element.
         */
        UNTIL
    }

    /**
     * The real iterator.
     */
    private final Iterator<? extends T> data;
    /**
     * The filter function.
     */
    private final Predicate<? super T> filter;
    /**
     * The type of filtering.
     */
    private final Type type;
    /**
     * Flag used along with {@link Type#UNTIL} to
     * indicate that it can start to return elements.
     */
    private boolean start;

    /**
     * Construct a `FilteredIterator` with type default to {@link Type#ALL}.
     *
     * @param iterator
     *      The real iterator
     * @param filter
     *      The filter function
     */
    FilteredIterator(Iterator<? extends T> iterator, Predicate<? super T> filter) {
        this(iterator, filter, Type.ALL);
    }

    /**
     * Construct a `FilteredIterator`.
     *
     * @param iterator
     *      the real iterator
     * @param filter
     *      the filter function
     * @param type
     *      the type of filtering
     */
    FilteredIterator(Iterator<? extends T> iterator, Predicate<? super T> filter, Type type) {
        data = $.requireNotNull(iterator);
        this.filter = $.requireNotNull(filter);
        this.type = $.requireNotNull(type);
    }

    /**
     * Returns the current element based on the result of
     * filtering and also the type of filtering logic.
     *
     * @return
     *      the current element
     */
    @Override
    protected Option<T> getCurrent() {
        boolean ok;
        while (data.hasNext()) {
            T t = data.next();
            switch (type) {
            case ALL:
                ok = filter.test(t);
                if (ok) {
                    return $.any(t);
                } else {
                    continue;
                }
            case WHILE:
                ok = filter.test(t);
                if (ok) {
                    return $.any(t);
                } else {
                    return $.none();
                }
            case UNTIL:
                if (start) {
                    return $.any(t);
                }
                ok = filter.test(t);
                if (ok) {
                    start = true;
                    return $.any(t);
                } else {
                    continue;
                }
            }
        }
        return $.none();
    }

}
