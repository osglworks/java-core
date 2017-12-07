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
import java.util.function.Function;

/**
 * A `FlatMappedIterator` represents the result of
 * applying `Function<T, Iterable<R>>` function
 * to an iterator of `T`.
 */
class FlatMappedIterator<T, R> extends StatefulIterator<R> {
    private final Iterator<? extends T> data;
    private final Function<? super T, ? extends Iterable<? extends R>> mapper;
    private Iterator<? extends R> currentMapped = null;

    /**
     * Construct a `FlatMappedIterator`.
     *
     * @param itr
     *      An iterator of `T` typed elements
     * @param mapper
     *      A function that map element `T` to an iterable of `R`.
     */
    FlatMappedIterator(Iterator<? extends T> itr, Function<? super T, ? extends Iterable<? extends R>> mapper) {
        this.data = itr;
        this.mapper = mapper;
    }

    /**
     * Returns the next elements been mapped.
     *
     * @return
     *      An option that describes the next element.
     */
    @Override
    protected Option<R> getCurrent() {
        while (null == currentMapped || !currentMapped.hasNext()) {
            if (!data.hasNext()) {
                return $.none();
            }
            currentMapped = mapper.apply(data.next()).iterator();
        }
        return $.any(currentMapped.next());
    }
}
