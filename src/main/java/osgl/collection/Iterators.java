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

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * Utility class to create certain types of {@link Iterator}
 */
public enum Iterators {
    ;

    public static <T> Iterator<T> composite(
            Iterator<? extends T> head,
            Iterator<? extends T> tail) {
        return new CompositeIterator<>(head, tail);
    }

    public static <T> Iterator<T> filter(Iterator<? extends T> itr, Predicate<? super T> filter) {
        return new FilteredIterator<>(itr, filter);
    }

    public static <T> Iterator<T> filter(
            Iterator<? extends T> itr,
            Predicate<? super T> filter,
            FilteredIterator.Type type) {
        return new FilteredIterator<>(itr, filter, type);
    }

    public static <T> Iterator<T> filterIndex(Iterator<? extends T> itr, IntPredicate predicate) {
        return new IndexFilteredIterator<>(itr, predicate);
    }

    public static <T, R> Iterator<R> flatMap(Iterator<? extends T> itr, Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return new FlatMappedIterator<T, R>(itr, mapper);
    }

    public static <T, R> Iterator<R> map(Iterator<? extends T> itr, Function<? super T, ? extends R> mapper) {
        return new MappedIterator<T, R>(itr, mapper);
    }

}
