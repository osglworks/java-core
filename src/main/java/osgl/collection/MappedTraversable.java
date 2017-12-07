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
import osgl.func.Func1;

import java.util.Iterator;
import java.util.function.Function;

public class MappedTraversable<T, R> implements Traversable<R> {

    private final Iterable<? extends T> data;
    private final Func1<? super T, ? extends R> mapper;

    MappedTraversable(Iterable<? extends T> iterable, Function<? super T, ? extends R> mapper) {
        this.data = $.requireNotNull(iterable);
        this.mapper = Func1.of(mapper);
    }

    @Override
    public Iterator<R> iterator() {
        return Iterators.map(data.iterator(), mapper);
    }

    public static <T, R> Traversable<R> of(Iterable<? extends T> iterable, Function<? super T, ? extends R> mapper) {
        return new MappedTraversable<>(iterable, mapper);
    }

}
