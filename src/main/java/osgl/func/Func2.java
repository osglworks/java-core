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

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A function that takes one parameter and returns a value.
 *
 * The `Func` extends java SDK's {@link Function} interface with
 * a few utility functions
 *
 * @param <P1>
 *     The type of the first (and only) parameter
 * @param <R>
 *     The type of the function return value
 */
@FunctionalInterface
public interface Func2<P1, P2, R> extends FuncBase, BiFunction<P1, P2, R> {

    /**
     * A nil function that always returns null.
     */
    Func2<Object, Object, ?> NIL = (p1, p2) -> null;

    /**
     * Apply parameters to this function with fallbackValue specified.
     *
     * If there are exception encountered applying this function, then return the
     * `fallbackValue`.
     *
     * @param p1
     *      The first parameter
     * @param p2
     *      The second parameter
     * @param fallbackValue
     *      The fallback value
     * @return
     *      return value of this function or the `fallbackValue` if exception encountered.
     */
    default R applyOrElse(P1 p1, P2 p2, R fallbackValue) {
        return applyOrElse(p1, p2, constant(fallbackValue));
    }

    /**
     * Apply this function with fallback function specified.
     *
     * It shall return result of this function and only call fallback function
     * if there are exception encountered applying this function.
     *
     * @param p1
     *      the first parameter
     * @param p2
     *      the second parameter
     * @param fallback
     *      the fallback function to be applied in case of exception
     * @return
     *      the return value of this function or of the fallback function
     *      if exception encountered applying this function
     */
    default R applyOrElse(P1 p1, P2 p2, BiFunction<? super P1, ? super P2, ? extends R> fallback) {
        Objects.requireNonNull(fallback);
        try {
            return apply(p1, p2);
        } catch (Exception e) {
            return fallback.apply(p1, p2);
        }
    }

    /**
     * Returns a `Func2` instance that when applied, first
     * apply this function instance and use the return value
     * to apply to the `after` {@link Function}.
     *
     * @param after
     *      the function to be applied after applied to this function
     * @param <T>
     *      the type of return value of the `after` function
     * @return
     *      a `Func2` instance as described above
     */
    default <T> Func2<P1, P2, T> andThen(Function<? super R, ? extends T> after) {
        Objects.requireNonNull(after);
        return (p1, p2) -> after.apply(this.apply(p1, p2));
    }

    /**
     * Returns a {@link Func1} instance that when applied, will return
     * the result that apply the first parameter plus the specified `p2`
     * as second parameter to this function.
     *
     * @param p2
     *      the parameter to be applied as second parameter when the return function is applied
     * @return
     *      a {@link Func1} instance as described above
     */
    default Func1<P1, R> curry(final P2 p2) {
        return (p1) -> apply(p1, p2);
    }

    /**
     * Returns a `Func2` instance that when applied, first
     * try this function, in case of exception then return
     * the fallback value.
     *
     * @param fallbackValue
     *      the fallback value when exception encountered applying
     *      this function
     * @return
     *      a `Func2` instance as described above
     */
    default Func2<P1, P2, R> orElse(R fallbackValue) {
        return (p1, p2) -> applyOrElse(p1, p2, fallbackValue);
    }

    /**
     * Returns a `Func2` instance that when applied, first
     * apply this function instance, in case of exception
     * then apply the fallback function.
     *
     * @param fallback
     *      the fallback function
     * @return
     *      a `Func2` instance as described above
     */
    default Func2<P1, P2, R> orElse(BiFunction<? super P1, ? super P2, ? extends R> fallback) {
        Objects.requireNonNull(fallback);
        return (p1, p2) -> applyOrElse(p1, p2, fallback);
    }

    /**
     * Returns a {@link Proc2} instance that when invoked to {@link Proc2#run(Object, Object)}
     * will call {@link #apply(Object, Object)} method of this function and ignore the
     * return value.
     *
     * @return
     *      A {@link Proc2} instance as described above.
     */
    @SuppressWarnings("ReturnValueIgnored")
    default Proc2<P1, P2> toProcedure() {
        return this::apply;
    }

    /**
     * Returns a `Func2` instance that when applied always
     * returns `null`.
     *
     * @param <P1>
     *     the type of first parameter
     * @param <P2>
     *     the type of second parameter
     * @param <R>
     *     the type of return value
     * @return
     *      a `Func1` instance as described above
     */
    @SuppressWarnings("unchecked")
    static <P1, P2, R> Func2<P1, P2, R> nil() {
        return (Func2<P1, P2, R>) NIL;
    }

    /**
     * Returns a `Func2` instance that always return
     * a constant value without regarding to the parameters
     * passed when applying it.
     *
     * @param value
     *      The constant value
     * @param <P1>
     *      The type of the first parameter
     * @param <P2>
     *      The type of the second parameter
     * @param <R>
     *      The type of the result and the constant value
     * @return
     *      always the constant value specified
     */
    static <P1, P2, R> Func2<P1, P2, R> constant(final R value) {
        return (x, y) -> value;
    }

    /**
     * Factory method that convert a JDK {@link Function} to an OSGL
     * {@link Func2}.
     *
     * @param function
     *      the JDK {@link Function}
     * @param <P1>
     *      The type of the first parameter
     * @param <P2>
     *      The type of the second parameter
     * @param <R>
     *      The type of the result and the constant value
     * @return
     *      an OSGL {@link Func2} instance
     */
    static <P1, P2, R> Func2<P1, P2, R> of(final BiFunction<P1, P2, R> function) {
        if (function instanceof Func2) {
            return (Func2<P1, P2, R>) function;
        }
        return function::apply;
    }

    // TODO add lift to Option

}
