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

import static osgl.func.Predicates.lt;

import osgl.exception.E;
import osgl.func.Predicates;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * `Sequence` is a {@link Traversable} that element always iterate in the same order.
 *
 * `Sequence` specific methods:
 *
 * * {@link #head()}
 * * {@link #head(int)}
 * * {@link #tail(int)}
 * * {@link #take(int)}
 * * {@link #drop(int)}
 * * {@link #dropTail(int)}
 * * {@link #append(Object)}
 * * {@link #append(Sequence)}
 * * {@link #prepend(Object)}
 * * {@link #prepend(Sequence)}
 *
 * The following `Traversable` methods are overwritten so they
 * return type of `Sequence` instead of `Traversable`:
 *
 * * {@link #map(Function)}
 * * {@link #flatMap(Function)}
 * * {@link #filter(Predicate)}
 * * {@link #accept(Consumer)}
 * * {@link #each(Consumer)}
 *
 * Static methods:
 *
 * * {@link #nil()}
 *
 * @param <T>
 *      The type of the sequence element
 */
public interface Sequence<T> extends Traversable<T> {

    /**
     * Iterate this `Sequence` with a visitor function. This method
     * does not specify the approach to iterate through this structure. The
     * implementation might choose iterate from left to right, or vice versa.
     * It might even choose to split the structure into multiple parts, and
     * iterate through them in parallel.
     *
     * @param visitor
     *      the function to traverse all elements in this `Sequence`.
     * @return
     *      a reference to this `Sequence`.
     */
    @Override
    default Sequence<T> accept(Consumer<? super T> visitor) {
        forEach(visitor);
        return this;
    }

    /**
     * Alias of {@link #accept(Consumer)}.
     *
     * @param visitor
     *      the function to traverse all elements in this `Sequence`.
     * @return
     *      a reference to this `Sequence`.
     */
    @Override
    default Sequence<T> each(Consumer<? super T> visitor) {
        forEach(visitor);
        return this;
    }

    /**
     * Returns a `Sequence` without the first `n` elements of this `Sequence`.
     *
     * if `n` is negative then return {@link #dropTail(int)} with negative of `n`.
     *
     * if `n` is `0` then returns a `Sequence` that contains all elements of this
     * `Sequence`. Default implementation is to return reference to this `Sequence`
     * directly.
     *
     * if this `Sequence` can be counted and `n` is greater than or equal to
     * {@link #size()} of this `Sequence` then return an empty `Sequence`.
     *
     * @param n
     *      Specify the number of head elements of this `Sequence` that
     *      should be excluded from the result `Sequence`.
     * @return
     *      A `Sequence` as described above.
     */
    default Sequence<T> drop(int n) {
        if (0 == n) {
            return this;
        }
        if (n < 0) {
            return dropTail(-n);
        }
        if (sized() && (size() <= n)) {
            return nil();
        }
        return new IndexFilteredSequence<>(this, Predicates.gt(n));
    }

    /**
     * Returns a `Sequence` with the head elements excluded from this `Sequence` as per
     * filter specified.
     *
     * The process stops at the first element that does not pass the {@link Predicate#test(Object)}
     * of the `filter`.
     *
     * @param filter
     *      The predicate used to test the elements
     * @return
     *      A `Sequence` as described above
     */
    default Sequence<T> dropWhile(Predicate<? super T> filter) {
        return new FilteredSequence<>(this, filter, FilteredIterator.Type.UNTIL);
    }

    /**
     * Returns a `Sequence` without the last `n` elements of this `Sequence`.
     *
     * if `n` is `0` then returns a `Sequence` contains all elements in this `Sequence`.
     *
     * if `n` is negative then returns {@link #drop(int)} with negative of `n`.
     *
     * if this `Sequence` can be counted and `n` is greater than or equal to
     * {@link #size()} of this `Sequence` then return an empty `Sequence`.
     *
     * If this `Sequence` class does not support drop elements from tail it will throw out
     * {@link UnsupportedOperationException}.
     *
     * Default implementation is to throw {@link UnsupportedOperationException}.
     *
     * @param n
     *      The number of last elements in this `Sequence` to be excluded from
     *      the result `Sequence`.
     * @return
     *      A `Sequence` as described above.
     * @throws UnsupportedOperationException
     *      if this `Sequence` does not support this method.
     *
     */
    default Sequence<T> dropTail(int n) throws UnsupportedOperationException {
        if (0 == n) {
            return this;
        }
        if (n < 0) {
            return drop(-n);
        }
        if (sized() && size() <= n) {
            return nil();
        }
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the first element in this `Sequence`.
     *
     * If this `Sequence` is empty then it throws {@link java.util.NoSuchElementException}
     *
     * @return
     *      the first element in this `Sequence`
     * @throws java.util.NoSuchElementException
     *      if the `Sequence` does not contains element
     */
    default T head() {
        Iterator<T> itr = iterator();
        if (!itr.hasNext()) {
            throw new NoSuchElementException();
        }
        return itr.next();
    }

    /**
     * Returns a `Sequence` that contains the first `n` elements of this `Sequence`
     * if `n` is positive and this `Sequence` contains more than `n` elements.
     *
     * If this `Sequence` contains less than `n` elements, then return a `Sequence`
     * that contains all elements contained in this `Sequence`. Note it might return
     * this `Sequence` directly in this case.
     *
     * if `n` is negative then throws {@link IllegalArgumentException}. Note sub class/interface
     * might choose different way of handling negative `n`.
     *
     * if `n` is `0` then an empty `Sequence` is returned.
     *
     * @param n
     *      specify the number of elements to be returned.
     * @return
     *      A `Sequence` as described above.
     * @throws UnsupportedOperationException
     *      if `n` is negative and returning tail elements is not supported
     * @see #take(int)
     */
    default Sequence<T> head(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (0 == n) {
            return nil();
        }
        if (sized()) {
            if (n >= size()) {
                return this;
            }
        }
        return new IndexFilteredSequence<>(this, lt(n));
    }

    /**
     * Returns a `Sequence` that contains the last `n` elements of this `Sequence`
     * if `n` is positive and this `Sequence` contains more than `n` elements.
     *
     * If this `Sequence` contains less than `n` elements, then return a `Sequence`
     * that contains all elements contained in this `Sequence`. Note it might return
     * this `Sequence` directly in this case.
     *
     * if `n` is negative
     *
     * if `n` is `0` then an empty `Sequence` is returned.
     *
     * @param n
     *      specify the number of elements to be returned.
     * @return
     *      A `Sequence` as described above.
     * @throws UnsupportedOperationException
     *      if `n` is negative and returning tail elements is not supported
     * @see #take(int)
     */
    default Sequence<T> tail(int n) {
        if (n < 0) {
            return head(-n);
        }
        if (0 == n) {
            return nil();
        }
        if (sized()) {
            int size = size();
            if (n >= size) {
                return this;
            } else {
                return drop(size - n);
            }
        }
        return new IndexFilteredSequence<>(this, lt(n));
    }

    /**
     * Alias of {@link #head(int)}
     *
     * @param n
     *      the number of elements to be returned from head of this `Sequence`
     * @return
     *      A sequence as described in {@link #head(int)}
     */
    default Sequence<T> take(int n) {
        return head(n);
    }

    /**
     * Return a `Sequence` contains elements from this `Sequence` while `predicate` test the
     * element returns `true`.
     *
     * This process stops at the first element cannot pass the test of the
     * `predicate`.
     *
     * @param filter
     *      The predicate used to test the element.
     * @return
     *      A Sequence as described above.
     */
    default Sequence<T> takeWhile(Predicate<? super T> filter) {
        return new FilteredSequence<>(this, filter, FilteredIterator.Type.WHILE);
    }

    /**
     * Returns a `Sequence` of this `Sequence` appended with
     * an element specified.
     *
     * The default implementation is to return an new `Sequence`. However
     * sub class might choose to implement appending the element to
     * this `Sequence` if this `Sequence` is a {@link #isMutable() mutable}
     * data structure.
     *
     * @param element
     *      The element to be appended to this `Sequence`.
     * @return
     *      The `Sequence` as described above.
     */
    default Sequence<T> append(T element) {
        throw E.tbd();
    }

    /**
     * Returns a concatenated `Sequence` of this `Sequence` appended with
     * the `tail` `Sequence` specified.
     *
     * The default implementation is to return an new `Sequence`. However
     * sub class might choose to implement appending `tail` to
     * this `Sequence` if this `Sequence` is a {@link #isMutable() mutable}
     * data structure.
     *
     * @param tail
     *      The `Sequence` to be append to this `Sequence`.
     * @return
     *      A concatenated `Sequence` as described above.
     */
    default Sequence<T> append(Sequence<? extends T> tail) {
        return CompositeSequence.of(this, tail);
    }

    /**
     * Returns a `Sequence` of this `Sequence` prepended with
     * an element specified.
     *
     * The default implementation is to return an new `Sequence`. However
     * sub class might choose to implement prepending the element to
     * this `Sequence` if this `Sequence` is a {@link #isMutable() mutable}
     * data structure.
     *
     * @param element
     *      The element to be prepended to this `Sequence`.
     * @return
     *      The `Sequence` as described above.
     */
    default Sequence<T> prepend(T element) {
        throw E.tbd();
    }

    /**
     * Returns a concatenated `Sequence` of this `Sequence` prepended with
     * `head` `Sequence`.
     *
     * The default implementation is to return an new `Sequence`. However
     * sub class might choose to implement prepending `head` to
     * this `Sequence` if this `Sequence` is a {@link #isMutable() mutable}
     * data structure.
     *
     * @param head
     *      The `Sequence` to be prepended to this `Sequence`.
     * @return
     *      A concatenated `Sequence` as described above.
     */
    default Sequence<T> prepend(Sequence<? extends T> head) {
        return CompositeSequence.of(head, this);
    }

    /**
     * Returns a `Sequence` that each element is map of the element in
     * this `Sequence` by applying the `mapper` function.
     *
     * **Note** the returned `Sequence` is an {@link #isImmutable() immutable}
     * data structure.
     *
     * @param mapper
     *      the function that applied to element in this sequence
     *      and returns element in the result sequence.
     * @param <R>
     *      The type of the `mapper` return value.
     * @return
     *      A `Sequence` contains mapped value from the elements in
     *      this `Sequence`.
     */
    @Override
    default <R> Sequence<R> map(Function<? super T, ? extends R> mapper) {
        return MappedSequence.of(this, mapper);
    }

    /**
     * Returns an new `Sequence` that contains all elements in the current
     * `Sequence` except that does not pass the test of the filter function
     * specified.
     *
     * ```java
     *     Sequence<Integer> seq = C.List(-1, 0, 1, -3, 7);
     *     Sequence<Integer> filtered = seq.filter(Predicates.gt(0));
     *     yes(filtered.contains(1));
     *     no(filtered.contains(-3));
     * ```
     *
     * **Note** the returned `Sequence` is an {@link #isImmutable() immutable}
     * data structure.
     *
     * @param predicate
     *         the function that test if the element in the `Sequence` should be
     *         kept in the resulting `Sequence`. When applying the filter function
     *         to the element, if the result is `true` then the element will
     *         be kept in the resulting `Sequence`.
     * @return the new `Sequence` consists of elements passed
     *         the filter function test
     */
    @Override
    default Sequence<T> filter(Predicate<? super T> predicate) {
        return FilteredSequence.of(this, predicate);
    }

    /**
     * Returns an immutable empty `Sequence`.
     *
     * @param <T>
     *      The type of the element
     * @return
     *      An immutable and empty `Sequence`
     */
    static <T> Sequence<T> nil() {
        throw E.tbd();
    }
}
