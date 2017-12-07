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
import java.util.NoSuchElementException;

/**
 * A `StatefulIterator` defines a `getCurrent()` method for
 * sub class to implement the `current` state of the
 * iterator.
 *
 * @param <T>
 *      The type of the elements in the iterator.
 */
abstract class StatefulIterator<T> implements Iterator<T> {

    private Option<T> current = $.none();

    /**
     * If there are still elements, then return the an option describes the next element,
     * otherwise return {@link Option#NONE}
     *
     * @return
     *      either next element or {@link Option#NONE} if no element in the iterator
     */
    protected abstract Option<T> getCurrent();

    /**
     * Check if there are more elements in the iterator.
     *
     * @return
     *      `true` if there are more elements in the iterator,
     *      or `false` otherwise.
     */
    public boolean hasNext() {
        if (current.isDefined()) {
            return true;
        }
        current = getCurrent();
        return current.isDefined();
    }

    /**
     * Returns next element in the iterator.
     *
     * If there are no more element in the iterator, then it
     * will throw out {@link NoSuchElementException}.
     *
     * @return
     *      The next element in the iterator
     * @throws NoSuchElementException
     *      if there are no more element in the iterator.
     */
    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        T ret = current.get();
        current = $.none();
        return ret;
    }

}
