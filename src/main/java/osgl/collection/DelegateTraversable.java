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

import java.util.Collection;
import java.util.Iterator;

/**
 * A {@link Traversable} delegate element source to an {@link Iterable}.
 *
 * @param <T>
 *         The type of the element in the `Traversable`.
 */
class DelegateTraversable<T> implements Traversable<T> {

    private final Iterable<? extends T> data;
    private final boolean sized;

    /**
     * Construct `IterableTraversable` with an {@link Iterable}.
     *
     * @param iterable
     *         A non null {@link Iterable}.
     */
    DelegateTraversable(Iterable<? extends T> iterable) {
        this.data = $.requireNotNull(iterable);
        this.sized = iterable instanceof Collection;
    }

    @Override
    public boolean sized() {
        return sized;
    }

    @Override
    public int size() throws UnsupportedOperationException {
        return ((Collection) data).size();
    }

    @Override
    public Iterator<T> iterator() {
        return (Iterator<T>) data.iterator();
    }

    /**
     * Create a {@link Traversable} from an iterable.
     *
     * @param iterable
     *         A non-null {@link Iterable}.
     * @param <T>
     *         The type of the iterable element.
     * @return A {@link Traversable} of the `iterable`.
     */
    @SuppressWarnings("unchecked")
    static <T> Traversable<T> of(Iterable<? extends T> iterable) {
        return new DelegateTraversable<>(iterable);
    }

}
