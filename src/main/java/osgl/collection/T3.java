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

/**
 * A tuple with three elements.
 *
 * @param <A>
 *     Type of the first element
 * @param <B>
 *     Type of the second element
 * @param <C>
 *     Type of the second element
 */
public class T3<A, B, C> {

    /**
     * The first element `a` in this tuple
     */
    public final A a;

    /**
     * The second element `b` in this tuple
     */
    public final B b;

    /**
     * The third element `c` in this tuple
     */
    public final C c;

    /**
     * Construct a `T3`
     *
     * @param a
     *      Param to be assigned to the first element
     * @param b
     *      Param to be assigned to the second element
     * @param c
     *      Param to be assigned to the third element
     */
    public T3(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Check if this `T3` is equal to the Object `o` provided.
     *
     * An `T3` instance is considered to be equal to `o` if
     *
     * * `o` is an instance of `T3` and
     * * `o.a` equals to `this.a` and
     * * `o.b` equals to `this.b` and
     * * `o.c` equals to `this.c`
     *
     * @param o
     *      An object
     * @return
     *      `true` if this `T3` is equal to `o` or `false` otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof T3) {
            T3 that = (T3) o;
            return eq(that.a, a) && eq(that.b, b) && eq(that.c, c);
        }
        return false;
    }

    /**
     * Returns {@link Object#hashCode()} of this `T3`.
     *
     * @return hash code of this `T3`
     */
    @Override
    public int hashCode() {
        return hc(a, b, c);
    }

    /**
     * Returns String representation of this `T3`.
     *
     * @return String representation of this `T3`.
     */
    @Override
    public String toString() {
        return "(" + a + ", " + b + ", " + c + ")";
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
     * Returns the third element {@link #c}
     *
     * @return {@link #c}
     */
    public C third() {return c;}

    /**
     * Returns the last element {@link #c}
     *
     * @return {@link #c}
     */
    public C last() {
        return c;
    }

    /**
     * Returns an new {@link Triple} with specified `a` as the first
     * element and other fields come from `this` `T3`.
     *
     * @param a
     *      the param to be assigned to the first element of the new {@link Triple}.
     * @return
     *      A {@link Triple} as described above.
     */
    public Triple<A, B, C> set1(A a) {
        return new Triple<>(a, b, c);
    }

    /**
     * Returns an new {@link Triple} with specified `b` as the second
     * element and other fields come from `this` `T3`
     *
     * @param b
     *      the param to be assigned to the second element of the new {@link Triple}.
     * @return
     *      A {@link Triple} as described above.
     */
    public Triple<A, B, C> set2(B b) {
        return new Triple<>(a, b, c);
    }

    /**
     * Returns an new {@link Triple} with specified `c` as the third
     * element and other fields come from `this` `T3`
     *
     * @param c
     *      the param to be assigned to the third element of the new {@link Triple}.
     * @return
     *      A {@link Triple} as described above.
     */
    public Triple<A, B, C> set3(C c) {
        return new Triple<>(a, b, c);
    }

    /**
     * Alias of {@link #set1(Object)}.
     *
     * @param a
     *      the param to be assigned to the first element of the new {@link Triple}.
     * @return
     *      A {@link Triple} as described above
     */
    public Triple<A, B, C> setA(A a) {
        return set1(a);
    }

    /**
     * Alias of {@link #set2(Object)}.
     *
     * @param b
     *      the param to be assigned to the second element of the new {@link Triple}.
     * @return
     *      A {@link Triple} as described above.
     */
    public Triple<A, B, C> setB(B b) {
        return set2(b);
    }

    /**
     * Alias of {@link #set3(Object)}.
     *
     * @param c
     *      the param to be assigned to the third element of the new {@link Triple}.
     * @return
     *      A {@link Triple} as described above.
     */
    public Triple<A, B, C> setC(C c) {
        return set3(c);
    }


    /**
     * Create a {@link Triple} instance.
     *
     * A {@link Triple} instance is also an instance of
     * * {@link Pair} and
     * * {@link T3}
     *
     * @param a
     *     param to be assigned to the first element
     * @param b
     *     param to be assigned to the second element
     * @param b
     *     param to be assigned to the second element
     * @param <A>
     *     The type of the first element
     * @param <B>
     *     The type of the second element
     * @param <C>
     *     The type of the third element
     * @return
     *      A `Triple` instance.
     */
    public static <A, B, C> Triple<A, B, C> of(A a, B b, C c) {
        return new Triple<>(a, b, c);
    }
}
