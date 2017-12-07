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
import osgl.Option;
import osgl.exception.E;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * `Traversable` is a {@link Serializable} {@link Iterable} with functional programming extensions.
 *
 * The functional programming support of `Traversable`:
 *
 * * {@link #map(Function)}
 * * {@link #flatMap(Function)}
 * * {@link #reduce(BiFunction)}
 * * {@link #allMatch(Predicate)}
 * * {@link #anyMatch(Predicate)}
 * * {@link #noneMatch(Predicate)}
 * * {@link #filter(Predicate)}
 * * {@link #findOne(Predicate)}
 * * {@link #accept(Consumer)}
 * * {@link #each(Consumer)}
 *
 * Other methods:
 *
 * * {@link #sized()}
 * * {@link #size()}
 *
 * Static methods:
 *
 * * {@link #nil()}
 * * {@link #of(Iterable)}
 *
 * @param <T>
 *         The element type
 */
public interface Traversable<T> extends Iterable<T>, Serializable {

    /**
     * Report if elements in this `Traversable`can be counted.
     *
     * if this `Traversable` can not be counted, calling {@link #size()}
     * method will result in an {@link UnsupportedOperationException}
     *
     * @return
     *      `true` if the elements in this `Traversable` can be counted
     *      or `false` otherwise.
     */
    default boolean sized() {
        return false;
    }

    /**
     * Report the total number of elements in this `Traversable`.
     *
     * If this `Traversable` cannot be counted, calling this method
     * will result in {@link UnsupportedOperationException}.
     *
     * @return
     *      the total number of elements in this `Traversable`
     * @throws UnsupportedOperationException
     *      if this `Traversable` cannot be counted.
     */
    default int size() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns an new immutable traversable with a mapper function specified.
     * The element in the new traversal is the result of the
     * mapper function applied to this traversal element.
     *
     * ```java
     * Traversable traversable = C.List(23, _.NONE, null);
     * eq(C.List(true, false, false), traversal.map($::notNull));
     * eq(C.List("23", "", ""), traversal.map(Object::toString));
     * ```
     *
     * For Lazy Traversable, it must use lazy evaluation for this method.
     * Otherwise it is up to implementation to decide whether use lazy
     * evaluation or not
     *
     * @param mapper
     *         the function that applied to element in this traversal and returns element in the result traversal
     * @param <R>
     *         the element type of the new traversal
     * @return
     *         the new traversal contains results of the mapper
     *         function applied to this traversal
     */
    default <R> Traversable<R> map(Function<? super T, ? extends R> mapper) {
        return MappedTraversable.of(this, mapper);
    }

    /**
     * Returns an new immutable traversable consisting of the results of replacing each
     * element of this stream with the contents of the iterable produced by applying the
     * provided mapping function to each element. If the result of the mapping function
     * is `null`, this is treated as if the result is an empty traversable.
     *
     * @param mapper
     *         the function produce an iterable when applied to an element
     * @param <R>
     *         the element type of the the new traversable
     * @return
     *         an new traversable as described above
     */
    default <R> Traversable<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return FlatMappedTraversable.of(this, mapper);
    }

    /**
     * Returns an new traversable that contains all elements in the current traversable
     * except that does not pass the test of the filter function specified.
     *
     * ```java
     *     Traversable traversable = C.List(-1, 0, 1, -3, 7);
     *     Traversable filtered = traversable.filter(Predicates.gt(0));
     *     yes(filtered.contains(1));
     *     no(filtered.contains(-3));
     * ```
     *
     * @param predicate
     *         the function that test if the element in the traversable should be
     *         kept in the resulting traversable. When applying the filter function
     *         to the element, if the result is `true` then the element will
     *         be kept in the resulting traversable.
     * @return the new traversable consists of elements passed
     *         the filter function test
     */
    default Traversable<T> filter(Predicate<? super T> predicate) {
        return FilteredTraversable.of(this, predicate);
    }

    /**
     * Performs a reduction on the elements in this traversable, using the provided
     * identity and accumulating function.
     *
     * This is equivalent to:
     *
     * ```java
     *      R result = identity;
     *      for (T element: this traversable) {
     *          result = accumulator.apply(result, element);
     *      }
     *      return result;
     * ```
     *
     * The above shows a typical left side reduce. However depending on the
     * implementation, it might choose another way to do the reduction, including
     * reduction in a parallel way
     *
     * @param initial
     *         the initial value for the accumulating function
     * @param accumulator
     *         the function the combine two values
     * @param <R>
     *         the type of identity and the return value
     * @return the result of reduction
     */
    default <R> R reduce(R initial, BiFunction<R, T, R> accumulator) {
        R ret = initial;
        for (T t : this) {
            ret = accumulator.apply(ret, t);
        }
        return ret;
    }

    /**
     * Performs a reduction on the elements in this traversable, using provided accumulating
     * function.
     *
     * This might be equivalent to:
     *
     * ```java
     *      boolean found = false;
     *      T result = null;
     *      for (T element: this traversable) {
     *          if (found) {
     *              result = accumulator.apply(result, element);
     *          } else {
     *              found = true;
     *              result = element;
     *          }
     *      }
     *      return found ? _.some(result) : _.none();
     * ```
     *
     * The above shows a typical left side reduction. However depending on the
     * implementation, it might choose another way to do the reduction, including
     * reduction in a parallel way.
     *
     * @param accumulator
     *         the function takes previous accumulating
     *         result and the current element being
     *         iterated.
     * @return
     *      an option describing the accumulating result or
     *      {@link Option#none()} if the structure is empty.
     */
     default Option<T> reduce(BiFunction<T, T, T> accumulator) {
         Iterator<T> itr = iterator();
         if (!itr.hasNext()) {
             return $.none();
         }
         T ret = itr.next();
         while (itr.hasNext()) {
             ret = accumulator.apply(ret, itr.next());
         }
         return $.any(ret);
     }

    /**
     * Check if all elements match the predicate specified
     *
     * @param predicate
     *      the function to test the element
     * @return
     *      `true` if all elements match the predicate
     */
    default boolean allMatch(Predicate<? super T> predicate) {
        return noneMatch(predicate.negate());
    }

    /**
     * Check if any elements matches the predicate specified
     *
     * @param predicate
     *         the function to test the element
     * @return
     *      `true` if any element matches the predicate
     */
    default boolean anyMatch(Predicate<? super T> predicate) {
        return findOne(predicate).isDefined();
    }

    /**
     * Check if no elements matches the predicate specified. This should be
     * equivalent to:
     *
     * ```java
     *      this.allMatch(_.F.negate(predicate));
     * ```
     *
     * @param predicate
     *         the function to test the element
     * @return
     *      `true` if none element matches the predicate
     */
    default boolean noneMatch(Predicate<? super T> predicate) {
        return !anyMatch(predicate);
    }

    /**
     * Returns an element that matches the predicate specified. The interface
     * does not indicate if it should be the first element matches the predicate
     * be returned or in case of parallel computing, whatever element matches
     * found first is returned. It's all up to the implementation to refine the
     * semantic of this method
     *
     * @param predicate
     *         the function map element to Boolean
     * @return
     *      an element in this traversal that matches the predicate or
     *      {@link Option#NONE} if no element matches
     */
    default Option<T> findOne(Predicate<? super T> predicate) {
        for (T t : this) {
            if (predicate.test(t)) {
                return $.any(t);
            }
        }
        return $.none();
    }

    /**
     * Iterate this `Traversable` with a visitor function. This method
     * does not specify the approach to iterate through this structure. The
     * implementation might choose iterate from left to right, or vice versa.
     * It might even choose to split the structure into multiple parts, and
     * iterate through them in parallel.
     *
     * @param visitor
     *      the function to traverse all elements in this `Sequence`.
     * @return
     *      a reference to this `Traversable`
     */
    default Traversable<T> accept(Consumer<? super T> visitor) {
        forEach(visitor);
        return this;
    }

    /**
     * Alias of {@link #accept(Consumer)}.
     *
     * @param visitor
     *      the function to traverse all elements in this `Sequence`.
     * @return
     *      a reference to this `Traversable`
     */
    default Traversable<T> each(Consumer<? super T> visitor) {
        return accept(visitor);
    }

    /**
     * Returns an immutable empty `Traversable`.
     *
     * @param <T>
     *      The type of the element
     * @return
     *      An immutable and empty `Traversable`
     */
    static <T> Traversable<T> nil() {
        throw E.tbd();
    }

    /**
     * Convert an {@link Iterable} to an immutable `Traversable`.
     *
     * @param iterable
     *      An {@link Iterable}.
     * @param <T>
     *      The type of the elements in `iterable`.
     * @return
     *      A `Traversable` corresponding to the `iterable`.
     */
    static <T> Traversable<T> of(Iterable<? extends T> iterable) {
        return DelegateTraversable.of(iterable);
    }

}
