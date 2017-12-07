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

import static osgl.Lang.eq;
import static osgl.Lang.hc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A tuple with two elements.
 *
 * @param <A>
 *     Type of the first element
 * @param <B>
 *     Type of the second element
 */
public class T2<A, B> {

    /**
     * The first element `a` in this tuple
     */
    public final A a;

    /**
     * The second element `b` in this tuple
     */
    public final B b;

    /**
     * Construct a `T2`
     *
     * @param a
     *      Param to be assigned to the first element
     * @param b
     *      Param to be assigned to the second element
     */
    public T2(A a, B b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Check if this `T2` is equal to the Object `o` provided.
     *
     * An `T2` instance is considered to be equal to `o` if
     *
     * * `o` is an instance of `T2` and
     * * `o.a` equals to `this.a` and
     * * `o.b` equals to `this.b`
     *
     * @param o
     *      An object
     * @return
     *      `true` if this `T2` is equal to `o` or `false` otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof T2) {
            T2 that = (T2) o;
            return eq(that.a, a) && eq(that.b, b);
        }
        return false;
    }

    /**
     * Returns {@link Object#hashCode()} of this `T2`.
     *
     * @return hash code of this `T2`
     */
    @Override
    public int hashCode() {
        return hc(a, b);
    }

    /**
     * Returns String representation of this `T2`.
     *
     * @return  String representation of this `T2`.
     */
    @Override
    public String toString() {
        return "(" + a + ", " + b + ")";
    }

    /**
     * Returns the left element {@link #a}
     *
     * @return {@link #a}
     */
    public final A left() {
        return a;
    }

    /**
     * Returns the right element {@link #b}
     *
     * @return {@link #b}
     */
    public final B right() {
        return b;
    }

    /**
     * Returns the first element {@link #a}
     *
     * @return {@link #a}
     */
    public A first() {return a;}

    /**
     * Returns the second element {@link #b}
     *
     * @return {@link #b}
     */
    public B second() {return b;}

    /**
     * Returns the last element {@link #b}
     *
     * @return {@link #b}
     */
    public B last() {
        return b;
    }

    /**
     * Returns an new {@link Pair} with specified `a` as the first
     * element and `this.b` as the second element.
     *
     * @param a
     *      the param to be assigned to the first element of the new {@link Pair}.
     * @return
     *      A {@link Pair} as described above
     */
    public Pair<A, B> set1(A a) {
        return new Pair<>(a, b);
    }

    /**
     * Returns an new {@link Pair} with specified `b` as the second
     * element and `this.a` as the first element.
     *
     * @param b
     *      the param to be assigned to the second element of the new {@link Pair}.
     * @return
     *      A {@link Pair} as described above.
     */
    public Pair<A, B> set2(B b) {
        return new Pair<>(a, b);
    }

    /**
     * Alias of {@link #set1(Object)}.
     *
     * @param a
     *      the param to be assigned to the first element of the new {@link Pair}.
     * @return
     *      A {@link Pair} as described above
     */
    public Pair<A, B> setA(A a) {
        return set1(a);
    }

    /**
     * Alias of {@link #set2(Object)}.
     *
     * @param b
     *      the param to be assigned to the second element of the new {@link Pair}.
     * @return
     *      A {@link Pair} as described above.
     */
    public Pair<A, B> setB(B b) {
        return set2(b);
    }

    /**
     * Convert this {@code Tuple} instance into a Map with one key,value pair. Where
     * {@code key} is {@code a} and {@code value} is {@code b};
     * @return the map as described
     */
    @SuppressWarnings("unused")
    public Map<A, B> asMap() {
        Map<A, B> m = new HashMap<>();
        m.put(a, b);
        return m;
    }

    /**
     * Convert a list of `Tuple` instances into a Map. Where
     * ` key` is `a` and `value` is `b`;
     *
     * *Note* that the size of the returned map might be lesser than
     * the size of the tuple list if there are multiple `a` has
     * the same value, and the last one is the winner and it's `b`
     * will be put into the map
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param list the list of tuple to be transformed into map
     * @return the map as described
     */
    @SuppressWarnings("unused")
    public static <K, V> Map<K, V> asMap(Collection<T2<K, V>> list) {
        Map<K, V> m = new HashMap<>();
        for (T2<K, V> t: list) {
            m.put(t.a, t.b);
        }
        return m;
    }

    /**
     * Create a {@link Pair} instance.
     *
     * A {@link Pair} instance is also an instance of
     * * {@link Pair} and
     * * {@link T2}
     *
     * @param a
     *     Param to be assigned to the first element
     * @param b
     *     Param to be assigned to the second element
     * @param <A>
     *     The type of the first element
     * @param <B>
     *     The type of the second element
     * @return
     *      A `Pair` instance.
     */
    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }
}
