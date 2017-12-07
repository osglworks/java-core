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
class FlatMappedSequence<T, R> implements Sequence<R> {
    private final Sequence<? extends T> seq;
    private final Function<? super T, ? extends Sequence<? extends R>> mapper;

    FlatMappedSequence(
            Sequence<? extends T> seq,
            Function<? super T, ? extends Sequence<? extends R>> mapper
    ) {
        this.seq = $.requireNotNull(seq);
        this.mapper = $.requireNotNull(mapper);
    }

    @Override
    public Iterator<R> iterator() {
        return Iterators.flatMap(seq.iterator(), mapper);
    }

    public static <T, R> Sequence<R> of(
            Sequence<? extends T> seq,
            Function<? super T, ? extends Sequence<? extends R>> mapper
    ) {
        return new FlatMappedSequence<>(seq, mapper);
    }

}
