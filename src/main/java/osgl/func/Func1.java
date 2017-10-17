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
public interface Func1<P1, R> extends FuncBase, Function<P1, R> {

    /**
     * A nil function that always returns null.
     */
    Func1<Object, ?> NIL = (p) -> null;

    /**
     * A function that always return the parameter specified.
     */
    Func1<Object, Object> IDENTITY = (p) -> p;

    /**
     * Apply this function with fallback value specified.
     *
     * When there are exception encountered applying this function then return
     * the `fallbackValue`.
     *
     * @param param
     *      The parameter applied to this function
     * @param fallbackValue
     *      The fallback value to be returned if there are exception applying this function.
     * @return
     *      the return value of this function or the `fallbackValue` if exception encountered.
     */
    default R applyOrElse(P1 param, R fallbackValue) {
        return applyOrElse(param, constant(fallbackValue));
    }

    /**
     * Apply this function and call fallback function if exception
     * encountered during applying this function.
     *
     * @param param
     *      a parameter to be applied to the function
     * @param fallback
     *      the fallback function to be applied in case of exception
     * @return
     *      the return value of this function or of the fallback function
     *      if exception encountered applying this function
     */
    default R applyOrElse(P1 param, Function<? super P1, ? extends R> fallback) {
        Objects.requireNonNull(fallback);
        try {
            return apply(param);
        } catch (Exception e) {
            return fallback.apply(param);
        }
    }

    /**
     * Returns a `Func1` instance that when applied, first
     * apply this function instance and use the return value
     * to apply to the `after` {@link Function}.
     *
     * @param after
     *      the function to be applied after applied to this function
     * @param <T>
     *      the type of return value of the `after` function
     * @return
     *      a `Func1` instance as described above
     */
    default <T> Func1<P1, T> andThen(Function<? super R, ? extends T> after) {
        Objects.requireNonNull(after);
        return (p1) -> after.apply(this.apply(p1));
    }

    /**
     * Override {@link Function#compose(Function)} to return `Func1` typed function
     * instead of a {@link Function} type.
     *
     * This method is an alias of {@link #nowThat(Function)}.
     *
     * @param before
     *      The before function
     * @param <T>
     *      The type of parameter that before function takes
     * @return
     *      a `Func1` instance that first applied the `before`
     *      function and then applied this function
     */
    default <T> Func1<T, R> compose(Function<? super T, ? extends P1> before) {
        return nowThat(before);
    }

    /**
     * Returns a {@link Func0} instance that when applied, will return
     * the result that apply the specified param to this function.
     *
     * @param param
     *      the parameter to be applied when the return function is applied
     * @return
     *      a {@link Func0} instance as described above
     */
    default Func0<R> curry(final P1 param) {
        return () -> apply(param);
    }

    /**
     * Returns a `Func1` instance that when applied, first
     * try this function, in case of exception then return
     * the fallback value.
     *
     * @param fallbackValue
     *      the fallback value when exception encountered applying
     *      this function
     * @return
     *      a `Func1` instance as described above
     */
    default Func1<P1, R> orElse(R fallbackValue) {
        return (p1) -> applyOrElse(p1, fallbackValue);
    }

    /**
     * Returns a `Func1` instance that when applied, first
     * apply this function instance, in case of exception
     * then apply the fallback function.
     *
     * @param fallback
     *      the fallback function
     * @return
     *      a `Func1` instance as described above
     */
    default Func1<P1, R> orElse(Function<? super P1, ? extends R> fallback) {
        Objects.requireNonNull(fallback);
        return (p1) -> applyOrElse(p1, fallback);
    }

    /**
     * Returns a `Func0` instance that when applied, apply the `before`
     * function first and then apply this function with the result
     * of before function.
     *
     * @param before
     *      the before function to produce result to be fed into this function
     * @return
     *      the `Func0` instance as described above
     */
    default Func0<R> nowThat(Func0<? extends P1> before) {
        Objects.requireNonNull(before);
        return () -> apply(before.apply());
    }

    /**
     * Returns a `Func1` instance that when applied, first apply
     * the `before` function and then apply this function with
     * the result of `before` function.
     *
     * The semantic of this method is exactly the same as that
     * of {@link Function#compose(Function)}, while the only
     * differences is the function returned is of type `Func1`
     *
     * @param before
     *      The before function
     * @param <T>
     *      The type of parameter that before function takes
     * @return
     *      a `Func1` instance as described above
     * @see #compose(Function)
     */
    default <T> Func1<T, R> nowThat(Function<? super T, ? extends P1> before) {
        Objects.requireNonNull(before);
        return (t) -> apply(before.apply(t));
    }

    /**
     * Returns a {@link Proc1} instance that when invoked to {@link Proc1#run(Object)}
     * will call {@link #apply(Object)} method of this function and ignore the
     * return value.
     *
     * @return
     *      A {@link Proc1} instance as described above.
     */
    @SuppressWarnings("ReturnValueIgnored")
    default Proc1<P1> toProcedure() {
        return this::apply;
    }

    /**
     * Returns a `Func1` instance that when applied always
     * returns `null`.
     *
     * @param <PT>
     *     the type of parameter
     * @param <RT>
     *     the type of return value
     * @return
     *      a `Func1` instance as described above
     */
    @SuppressWarnings("unchecked")
    static <PT, RT> Func1<PT, RT> nil() {
        return (Func1<PT, RT>) NIL;
    }

    /**
     * Returns a `Func1` instance that when applied always
     * return the parameter specified.
     *
     * @param <PT>
     *     The type of the parameter
     * @param <RT>
     *     The type of the return value
     * @return
     *     The function as described above
     */
    @SuppressWarnings("unchecked")
    static <PT, RT> Func1<PT, RT> identity() {
        return (Func1<PT, RT>) IDENTITY;
    }

    /**
     * Returns a `Func1` instance that always return
     * a constant value without regarding to the parameter
     * passed when applying it.
     *
     * @param value
     *      The constant value
     * @param <PT>
     *      The type of the parameter
     * @param <RT>
     *      The type of the result and the constant value
     * @return
     *      always the constant value specified
     */
    static <PT, RT> Func1<PT, RT> constant(final RT value) {
        return (x) -> value;
    }

    /**
     * Factory method that convert a JDK {@link Function} to an OSGL
     * {@link Func1}.
     *
     * @param function
     *      the JDK {@link Function}
     * @param <PT>
     *      the type of parameter that `function` takes
     * @param <RT>
     *      the type of value that `function` returns
     * @return
     *      an OSGL {@link Func1} instance
     */
    static <PT, RT> Func1<PT, RT> of(final Function<PT, RT> function) {
        if (function instanceof Func1) {
            return (Func1<PT, RT>) function;
        }
        return function::apply;
    }

    /**
     * Factory method that convert a {@link Proc1} procedure to a `Func1` instance
     * that when calling {@link Func1#apply(Object)} method on the returning
     * function, it will call the {@link Proc1#run(Object)} on the `procedure`.
     *
     * @param procedure
     *      the procedure
     * @param <P1>
     *      the parameter take by the procedure
     * @return
     *      the function as described above
     */
    static <P1> Func1<P1, ?> of(final Proc1<P1> procedure) {
        return (p1) -> {
            procedure.run(p1);
            return null;
        };
    }

    // TODO add lift to Option

}
