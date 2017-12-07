package osgl.func;

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
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * Provides predefined {@link java.util.function.Predicate predicates} and
 * conversion utilities.
 */
@SuppressWarnings("unused")
public enum Predicates {
    ;

    public enum CollectionBehavior {
        ALL, ANY, NONE
    }

    /**
     * Returns a {@link Predicate} that tests if two arguments are equal according
     * to {@link java.util.Objects#equals(Object, Object)}.
     *
     * @param <T>
     *         the type of arguments to the predicate
     * @param target
     *         the object reference with which to compare for equality,
     *         which may be `null`
     * @return a predicate that tests if two arguments are equal according
     * to {@link java.util.Objects#equals(Object, Object)}
     */
    public static <T> Predicate<T> equalsTo(Object target) {
        return Predicate.isEqual(target);
    }

    /**
     * Returns a {@link Predicate} that test if a {@link Collection} is empty.
     *
     * @param <T>
     *         The type of the collection
     * @return a predicate that tests if a collection is empty.
     */
    public static <T extends Collection> Predicate<T> isEmpty() {
        return Collection::isEmpty;
    }

    /**
     * Returns a {@link Predicate} that assert passed in parameter is null.
     *
     * @param <T>
     *         the type of the parameter
     * @return `true` if the parameter is `null` or `false` otherwise
     */
    public static <T> Predicate<T> isNull() {
        Predicate<T> notNull = notNull();
        return notNull.negate();
    }

    /**
     * Returns a {@link Predicate} that assert passed in parameter is not null.
     *
     * The predicate returns `true` if the parameter is not `null` or `false` otherwise
     *
     * @param <T>
     *         the type of the parameter
     * @return a {@link Predicate} as described above
     */
    public static <T> Predicate<T> notNull() {
        return $::notNull;
    }

    /**
     * Returns a {@link IntPredicate} that check if the parameter `x` is greater than `n` specified.
     *
     * @param n
     *         the number used to check parameter `x`
     * @return An {@link IntPredicate} as described above.
     */
    public static IntPredicate greaterThan(int n) {
        return (x) -> x > n;
    }

    /**
     * Alias of {@link #greaterThan(int)}.
     *
     * @param n
     *         the number used to check param `x`
     * @return An {@link IntPredicate} that check if param `x` is greater than `n` specified.
     */
    public static IntPredicate gt(int n) {
        return greaterThan(n);
    }

    /**
     * Returns an {@link IntPredicate} that check if the param `x` is
     * greater than or equal to the number `n` specified.
     *
     * The {@link IntPredicate} returned is a {@link IntPredicate#negate()}
     * of {@link #lessThan(int)}
     *
     * @param n
     *         the number used to check the parameter `x`
     * @return An {@link IntPredicate} as described above
     * @see #lessThan(int)
     * @see #greaterThan(int)
     */
    public static IntPredicate greaterThanOrEqualTo(int n) {
        return lessThan(n).negate();
    }

    /**
     * Alias of {@link #greaterThanOrEqualTo(int)}.
     *
     * @param n
     *         The number used to check parameter `x`
     * @return An {@link IntPredicate} that check if param `x` is greater than or equal to `n`
     */
    public static IntPredicate gte(int n) {
        return greaterThanOrEqualTo(n);
    }

    /**
     * Returns a {@link IntPredicate} that check if the parameter `x` is less than `n` specified.
     *
     * @param n
     *         the number used to check parameter `x`
     * @return An {@link IntPredicate} as described above.
     */
    public static IntPredicate lessThan(int n) {
        return (x) -> x < n;
    }

    /**
     * Alias of {@link #lessThan(int)}
     *
     * @param n
     *         the number used to check parameter `x`
     * @return A {@link IntPredicate} to check if param `x` is less than `n` specified.
     */
    public static IntPredicate lt(int n) {
        return lessThan(n);
    }

    /**
     * Returns an {@link IntPredicate} that check if the param `x` is
     * less than or equal to the number `n` specified.
     *
     * The {@link IntPredicate} returned is a {@link IntPredicate#negate()}
     * of {@link #greaterThan(int)}
     *
     * @param n
     *         the number used to check the parameter `x`
     * @return An {@link IntPredicate} as described above
     */
    public static IntPredicate lessThanOrEqualTo(int n) {
        return greaterThan(n).negate();
    }

    /**
     * Alias of {@link #lessThanOrEqualTo(int)}.
     *
     * @param n
     *         The number used to check parameter `x`
     * @return An {@link IntPredicate} that check if param `x` is less than or equal to number `n`
     */
    public static IntPredicate lte(int n) {
        return lessThanOrEqualTo(n);
    }

    /**
     * Returns a {@link LongPredicate} that check if the parameter `x` is greater than `n` specified.
     *
     * @param n
     *         the number used to check parameter `x`
     * @return An {@link LongPredicate} as described above.
     */
    public static LongPredicate greaterThan(long n) {
        return (x) -> x > n;
    }

    /**
     * Alias of {@link #greaterThan(long)}.
     *
     * @param n
     *         the number used to check param `x`
     * @return An {@link LongPredicate} that check if param `x` is greater than `n` specified.
     */
    public static LongPredicate gt(long n) {
        return greaterThan(n);
    }

    /**
     * Returns an {@link LongPredicate} that check if the param `x` is
     * greater than or equal to the number `n` specified.
     *
     * The {@link LongPredicate} returned is a {@link LongPredicate#negate()}
     * of {@link #lessThan(long)}
     *
     * @param n
     *         the number used to check the parameter `x`
     * @return An {@link LongPredicate} as described above
     * @see #lessThan(long)
     * @see #greaterThan(long)
     */
    public static LongPredicate greaterThanOrEqualTo(long n) {
        return lessThan(n).negate();
    }

    /**
     * Alias of {@link #greaterThanOrEqualTo(long)}.
     *
     * @param n
     *         The number used to check parameter `x`
     * @return An {@link LongPredicate} that check if param `x` is greater than or equal to `n`
     */
    public static LongPredicate gte(long n) {
        return greaterThanOrEqualTo(n);
    }

    /**
     * Returns a {@link LongPredicate} that check if the parameter `x` is less than `n` specified.
     *
     * @param n
     *         the number used to check parameter `x`
     * @return An {@link LongPredicate} as described above.
     */
    public static LongPredicate lessThan(long n) {
        return (x) -> x < n;
    }

    /**
     * Alias of {@link #lessThan(long)}
     *
     * @param n
     *         the number used to check parameter `x`
     * @return A {@link LongPredicate} to check if param `x` is less than `n` specified.
     */
    public static LongPredicate lt(long n) {
        return lessThan(n);
    }

    /**
     * Returns an {@link LongPredicate} that check if the param `x` is
     * less than or equal to the number `n` specified.
     *
     * The {@link LongPredicate} returned is a {@link LongPredicate#negate()}
     * of {@link #greaterThan(long)}
     *
     * @param n
     *         the number used to check the parameter `x`
     * @return An {@link LongPredicate} as described above
     */
    public static LongPredicate lessThanOrEqualTo(long n) {
        return greaterThan(n).negate();
    }

    /**
     * Alias of {@link #lessThanOrEqualTo(long)}.
     *
     * @param n
     *         The number used to check parameter `x`
     * @return An {@link LongPredicate} that check if param `x` is less than or equal to number `n`
     */
    public static LongPredicate lte(long n) {
        return lessThanOrEqualTo(n);
    }

    /**
     * Returns a {@link Predicate} that assert a string contains string `part` as specified.
     *
     * The predicate returns `true` if the passed in string contains `part` or
     * `false` otherwise
     *
     * @param part
     *         the part string to be tested against the string parameter
     * @return A {@link Predicate} as described above
     */
    public static Predicate<String> contains(String part) {
        return (s) -> s.contains(part);
    }

    /**
     * Convert a {@link Function}`<T, Boolean>` into a {@link Predicate}.
     *
     * @param function
     *         The function that takes a parameter and return `Boolean` result.
     * @param <T>
     *         The type of the parameter the function takes
     * @return a {@link Predicate} that returns the `function`'s result
     */
    public static <T> Predicate<T> of(Function<? super T, Boolean> function) {
        return function::apply;
    }

    /**
     * Convert a {@link Predicate} to a {@link Func1} that when applied call the
     * {@link Predicate#test(Object)} method on the `predicate` and return the
     * result.
     *
     * @param predicate
     *         The predicate
     * @param <T>
     *         The type of the parameter
     * @return A {@link Func1} that return the result of the predicate.
     */
    public static <T> Func1<T, Boolean> toFunction(Predicate<? super T> predicate) {
        return predicate::test;
    }

}
