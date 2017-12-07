package osgl;

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

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Defines an option of element `T`.
 *
 * This class can be used to implement the `else-if` semantic in functional
 * programming and eliminate the `null` value
 *
 * @param <T>
 *      the element type
 */
public abstract class Option<T> implements Iterable<T>, Serializable {

    /**
     * An {@link Option} that contains nothing inside.
     *
     * @param <T>
     *          The element type.
     */
    private static class None<T> extends Option<T> {

        private static final long serialVersionUID = 962498820763181262L;

        /**
         * The sole instance of {@link None}.
         */
        public static final None INSTANCE = new None();

        private None() {
        }

        /**
         * `clone()` is not supported by `None`. Calling on this method will result a
         * {@link CloneNotSupportedException}.
         *
         * @return
         *      nothing
         * @throws CloneNotSupportedException
         */
        @Override
        protected Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException();
        }

        /**
         * Calling this method on a `None` result in {@link NoSuchElementException}.
         *
         * @return
         *      nothing
         * @throws NoSuchElementException
         */
        @Override
        public T get() {
            throw new NoSuchElementException();
        }

        /**
         * Returns an empty iterator.
         *
         * @return
         *      An iterator that has no element.
         */
        public Iterator<T> iterator() {
            return Collections.<T>emptyList().iterator();
        }

        /**
         * Returns `NONE` in string literal.
         *
         * @return
         *      `NONE`
         */
        @Override
        public String toString() {
            return "NONE";
        }

        private Object readResolve() throws ObjectStreamException {
            return NONE;
        }
    }

    /**
     * An {@link Option} that contains a non-null element.
     *
     * @param <T>
     *          The element type
     */
    private static class Some<T> extends Option<T> {

        private static final long serialVersionUID = 962498820763181265L;

        final T value;

        /**
         * Construct a `Some` instance with a non-null value.
         *
         * @param value
         *      The value that must not be null
         * @throws NullPointerException if the value is `null`.
         */
        public Some(T value) {
            this.value = $.requireNotNull(value);
        }

        /**
         * Returns the value contained in this `Some`.
         *
         * @return
         *      The value.
         */
        @Override
        public T get() {
            return value;
        }

        /**
         * Returns an iterator that contains `value` of this `Some`.
         *
         * @return
         *      an iterator that contains a single element: `value`
         */
        @Override
        public Iterator<T> iterator() {
            return Collections.singletonList(value).iterator();
        }

        @Override
        public String toString() {
            return "Some(" + value + ")";
        }
    }

    /**
     * The reference to {@link None#NONE} instance.
     */
    public static final None NONE = None.INSTANCE;

    /**
     * Check if this `Option` is equals to an object.
     *
     * An `Option` is supposed to be equal to an object in the following
     * cases:
     *
     * 1. the object and `this` option is the same reference
     * 2. the object is an `Option` and the value contained in the object
     *    equals to the value contained in this `Option`.
     *
     * @param obj
     *      The object to be tested.
     * @return
     *      `true` if the `obj` equals to this `Option` or `false` otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Option) {
            Option that = (Option) obj;
            return $.eq(get(), that.get());
        }
        return false;
    }

    /**
     * Returns hash code of this `Option`.
     *
     * If the `Option` is {@link #NONE} then return `0`, otherwise
     * it returns the hash code of the value.
     *
     * @return
     *      the hash code calculated in above logic.
     */
    @Override
    public int hashCode() {
        return isDefined() ? get().hashCode() : 0;
    }

    /**
     * Convert this `Option` to JDK {@link Optional}.
     *
     * @return
     *      A corresponding JDK {@link Optional} of this `Option`.
     */
    public Optional<T> toOptional() {
        return isDefined() ? Optional.of(get()) : Optional.empty();
    }


    /**
     * Check if there are value defined in this `Option`.
     *
     * Generally a {@link None} always return `false` on this method and
     * a {@link Some} always return `true`.
     *
     * @return `true` if there are value defined or `false` otherwise.
     */
    public final boolean isDefined() {
        return this != NONE;
    }

    /**
     * Alias of {@link #isDefined()}.
     *
     * This method adapt `Option` to JDK's {@link java.util.Optional}.
     *
     * @return
     *      `true` if there are value defined or `false` otherwise.
     */
    public final boolean isPresent() {
        return isDefined();
    }

    /**
     * Negate of {@link #isDefined()}.
     *
     * @return `true` if this option is not defined
     */
    public boolean notDefined() {
        return !isDefined();
    }

    /**
     * If a value is present in this {@code Option}, returns the value,
     * otherwise throws NoSuchElementException.
     *
     * @return the non-null value held by this `Option`
     * @throws NoSuchElementException if this `Option` is {@link #NONE}
     */
    public abstract T get() throws NoSuchElementException;

    /**
     * If a value is present, and the value matches the given predicate,
     * return an `Option` describing the value, otherwise return
     * {@link #NONE}.
     *
     * @param predicate
     *      the function to test the value held by this `Option`
     * @return
     *      an `Option` describing the value of this `Option` if
     *      a value is present and the value matches the given
     *      predicate, otherwise {@link #NONE}
     */
    public final Option<T> filter(Predicate<? super T> predicate) {
        $.requireNotNull(predicate);
        if (notDefined()) {
            return none();
        }
        T v = get();
        if (predicate.test(v)) {
            return this;
        } else {
            return none();
        }
    }

    /**
     * If a value is present, apply the provided mapping function to it,
     * and if the result is non-null, return an `Option` describing
     * the result. Otherwise return {@link #NONE}.
     *
     * @param mapper
     *      a mapping function to apply to the value, if present
     * @param <B>
     *      The type of the result of the mapping function
     * @return
     *      an Optional describing the result of applying a mapping
     *      function to the value of this `Option`, if a value
     *      is present, otherwise {@link #NONE}
     * @throws NullPointerException
     *      if the mapper function is `null`
     */
    public final <B> Option<B> map(final Function<? super T, ? extends B> mapper) {
        return isDefined() ? of(mapper.apply(get())) : NONE;
    }

    /**
     * If a value is present, apply the provided `Option` - bearing
     * mapping function to it, return that result, otherwise return
     * {@link #NONE}.
     *
     * This method is similar to {@link #map(Function)}, but the
     * provided mapper is one whose result is already an `Option`,
     * and if invoked, `flatMap` does not wrap it with an
     * additional `Option`.
     *
     * @param mapper
     *      A mapping function to apply to the value of this `Option`
     * @param <B>
     *     The type of the return option value by the mapping function
     * @return
     *     An `Option` returned by `mapper`.
     */
    @SuppressWarnings("unchecked")
    public final <B> Option<B> flatMap(final Function<? super T, Option<B>> mapper) {
        $.requireNotNull(mapper);
        Option<B> result = isDefined() ? mapper.apply(get()) : NONE;
        return $.requireNotNull(result);
    }

    /**
     * Return the value if present, otherwise return `fallback` value
     *
     * @param fallback
     *      the value to be returned if this `Option` is not {@link #isDefined() defined}.
     * @return
     *      the value of this `Option` if is defined or the `fallback` value
     */
    public final T orElse(T fallback) {
        return isDefined() ? get() : fallback;
    }

    /**
     * Return the value if present, otherwise invoke `fallback` and return
     * the result of that invocation.
     *
     * @param fallback
     *      the supplier that is invoked when this option is not {@link #isDefined() defined}
     * @return
     *      the value if present otherwise the result of {@link Supplier#get()} on `fallback`
     * @throws NullPointerException
     *      if value is not present and `fallback` is `null`
     */
    public final T orElse(Supplier<? extends T> fallback) {
        return isDefined() ? get() : fallback.get();
    }

    /**
     * Run this `Option` with the {@link Consumer} specified.
     *
     * The consumer's {@link Consumer#accept(Object)} method will only be
     * invoked if this `Option` is {@link #isDefined() defined}.
     *
     * @param consumer
     *      The consumer to be invoked if this `Option` is defined.
     */
    public final void runWith(Consumer<? super T> consumer) {
        if (isDefined()) {
            consumer.accept(get());
        }
    }

    /**
     * Returns {@link #NONE} instance.
     *
     * @param <T>
     *      The element type
     * @return
     *      The {@link #NONE} instance.
     */
    @SuppressWarnings("unchecked")
    public static <T> None<T> none() {
        return (None<T>) NONE;
    }

    /**
     * Alias of {@link #none()}.
     *
     * This method adapt `Option` to JDK's {@link java.util.Optional}.
     *
     * @param <T>
     *      The element type
     * @return
     *      {@link #NONE} instance.
     */
    public static <T> None<T> empty() {
        return none();
    }

    /**
     * Returns an `Option` with specified value.
     *
     * If the `value` is `null` then return {@link #NONE}, otherwise
     * return an `Option` of the `value`.
     *
     * @param value
     *      A nullable value.
     * @param <T>
     *      The type of the value.
     * @return
     *      An `Option` as described above.
     */
    public static <T> Option<T> any(T value) {
        return null == value ? NONE : some(value);
    }

    /**
     * Returns an `Option` with the specified present non-null value.
     *
     * @param value
     *      the value that cannot be `null`
     * @param <T>
     *      the type of the value
     * @return
     *      a `Option` of the value
     * @throws NullPointerException
     *      if the value specified is `null`
     */
    public static <T> Some<T> some(T value) {
        return new Some<>(value);
    }

    /**
     * Alias of {@link #some(Object)}.
     *
     * @param value
     *      a non-null value.
     * @param <T>
     *      the type of the value.
     * @return
     *      an `Option` of the value.
     * @throws NullPointerException
     *      if the value specified is `null`
     */
    public static <T> Option<T> of(T value) {
        return some(value);
    }

    /**
     * Alias of {@link #any(Object)}.
     *
     * @param value
     *      A nullable value.
     * @param <T>
     *      The type of the value.
     * @return
     *      An `Option` of the value or {@link #NONE} if `value` is `null`.
     */
    public static <T> Option<T> ofNullable(T value) {
        return any(value);
    }

    /**
     * Convert a JDK {@link Optional} to an {@link Option}.
     *
     * @param optional
     *      A JDK {@link Optional} instance.
     * @param <T>
     *     The type of the optional element
     * @return
     *     An {@link Option} that contains the {@link Optional} element
     *     or {@link Option#NONE} if {@link Optional} is not {@link Optional#isPresent()}.
     */
    public static <T> Option<T> of(Optional<T> optional) {
        return optional.isPresent() ? of(optional.get()) : none();
    }

}
