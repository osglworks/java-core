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

import java.util.Iterator;
import java.util.function.Function;

/**
 */
class FlatMappedTraversable<T, R> implements Traversable<R> {
    private final Iterable<? extends T> data;
    private final Function<? super T, ? extends Iterable<? extends R>> mapper;

    FlatMappedTraversable(Iterable<? extends T> itr, Function<? super T, ? extends Iterable<? extends R>> mapper) {
        this.data = $.requireNotNull(itr);
        this.mapper = $.requireNotNull(mapper);
    }

    @Override
    public Iterator<R> iterator() {
        return Iterators.flatMap(data.iterator(), mapper);
    }

    public static <T, R> Traversable<R> of(Iterable<? extends T> itr, Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return new FlatMappedTraversable<>(itr, mapper);
    }

}
