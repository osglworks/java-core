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

import java.util.function.Consumer;

/**
 * Procedure that take one parameter.
 *
 * `Proc1` extends JDK {@link Consumer} by delegating {@link Consumer#accept(Object)}
 * call to {@link #run(Object)} call.
 *
 * @param <P1>
 *     the type of the parameter
 */
@FunctionalInterface
public interface Proc1<P1> extends Consumer<P1> {

    /**
     * A `Proc1` that does nothing.
     */
    Proc1<?> NIL = (param) -> {
    };

    /**
     * Implement the logic of the procedure.
     *
     * @param param1
     *      a parameter
     */
    void run(P1 param1);

    /**
     * Implement {@link Consumer#accept(Object)} by calling
     * {@link #run(Object)} method.
     *
     * @param param1
     *      a parameter
     */
    @Override
    default void accept(P1 param1) {
        run(param1);
    }

    /**
     * Returns a `Proc1` instance that when called will
     * run this `Proc1` and followed by the `after` {@link Consumer}.
     *
     * @param after
     *      A {@link Consumer} to be run after this `Proc1`.
     * @return
     *      A composite `Proc1` instance as described above.
     */
    default Proc1<P1> andThen(Consumer<? super P1> after) {
        $.requireNotNull(after);
        return (P1 param) -> {
            run(param);
            after.accept(param);
        };
    }

    /**
     * Returns a {@link Proc0} instance with specified `param`. When
     * running the returned procedure, it runs this procedure with
     * the `param` specified.
     *
     * @param param
     *      a parameter
     * @return
     *      a {@link Proc0} as described above
     */
    default Proc0 curry(P1 param) {
        return () -> accept(param);
    }

    /**
     * Returns a `Proc1` instance that when called will run the
     * `before` {@link Consumer} first and then this procedure.
     *
     * @param before
     *      a {@link Consumer} to be invoked before this procedure
     * @return
     *      a composite `Proc1` instance as described above
     */
    default Proc1<P1> nowThat(Consumer<? super P1> before) {
        $.requireNotNull(before);
        return (param) -> {
            before.accept(param);
            run(param);
        };
    }

    /**
     * Returns a `Proc1` instance that when called will run this
     * procedure first, if there are any exception encountered then
     * run the `fallback`.
     *
     * @param fallback
     *      a {@link Consumer} provided as fallback procedure
     * @return
     *      an new `Proc1` instance as described above
     */
    default Proc1<P1> orElse(Consumer<? super P1> fallback) {
        $.requireNotNull(fallback);
        return (param) -> runOrElse(param, fallback);
    }

    /**
     * Run this procedure with fallback {@link Consumer} specified.
     *
     * If there are exception encountered running this procedure, then it will
     * call fallback.
     *
     * @param param
     *      the param to run this procedure
     * @param fallback
     *      the fallback {@link Consumer} to run if exception encountered running
     *      this procedure
     */
    default void runOrElse(P1 param, Consumer<? super P1> fallback) {
        $.requireNotNull(fallback);
        try {
            run(param);
        } catch (Exception e) {
            fallback.accept(param);
        }
    }

    /**
     * Returns a {@link Func1} instance that when applied, will call this
     * procedure with the parameter and return `null`.
     *
     * @return
     *      A function as described above.
     */
    default Func1<P1, ?> toFunction() {
        return (p) -> {
            run(p);
            return null;
        };
    }

    /**
     * Returns a {@link Proc1} instance from a {@link Consumer} so that
     * when the instance is called, it will delegate to
     * {@link Consumer#accept(Object)} method on the consumer instance.
     *
     * @param consumer
     *      A {@link Consumer} consumer instance
     * @param <P>
     *      The type of the parameter that consumer takes
     * @return
     *      A {@link Proc1} instance as described above
     */
    static <P> Proc1<P> of(Consumer<? super P> consumer) {
        if (consumer instanceof Proc1) {
            return (Proc1<P>) consumer;
        }
        return consumer::accept;
    }

    /**
     * Returns a {@link Proc1} instance that when invoking {@link #run(Object)}
     * will {@link Func1#apply(Object) apply} the param to the `function`,
     * and ignore the return value.
     *
     * @param function
     *      A {@link Func1} instance.
     * @param <P>
     *      The type of the parameter the `function` applied.
     * @return
     *      A {@link Proc1} instance as described above.
     */
    @SuppressWarnings("ReturnValueIgnored")
    static <P> Proc1<P> of(Func1<? super P, ?> function) {
        return function::apply;
    }

}
