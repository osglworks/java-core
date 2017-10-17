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

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Procedure that take two parameters.
 *
 * `Proc2` extends JDK {@link BiConsumer} by delegating {@link Consumer#accept(Object)}
 * call to {@link #run(Object, Object)} call.
 *
 * @param <P1>
 *     the type of the first parameter
 * @param <P2>
 *     the type of the second parameter
 */
public interface Proc2<P1, P2> extends BiConsumer<P1, P2> {

    /**
     * A `Proc2` that does nothing.
     */
    Proc2<?, ?> NIL = (p1, p2) -> {
    };

    /**
     * Implement the logic of the procedure.
     *
     * @param p1
     *      the first parameter
     * @param p2
     *      the second parameter
     */
    void run(P1 p1, P2 p2);

    /**
     * Implement {@link BiConsumer#accept(Object, Object)} by calling
     * {@link #run(Object, Object)} method.
     *
     * @param p1
     *      the first parameter
     * @param p2
     *      the second parameter
     */
    @Override
    default void accept(P1 p1, P2 p2) {
        run(p1, p2);
    }

    /**
     * Returns a `Proc2` instance that when called will
     * run this `Proc2` and followed by the `after` {@link Consumer}.
     *
     * @param after
     *      A {@link Consumer} to be run after this `Proc2`.
     * @return
     *      A composite `Proc2` instance as described above.
     */
    default Proc2<P1, P2> andThen(BiConsumer<? super P1, ? super P2> after) {
        $.ensureNotNull(after);
        return (p1, p2) -> {
            run(p1, p2);
            after.accept(p1, p2);
        };
    }

    /**
     * Returns a {@link Proc1} instance with specified `p2`. When
     * running the returned procedure, it runs this procedure with
     * the `p2` specified as the second parameter
     *
     * @param p2
     *      a parameter that will be used as the second parameter.
     * @return
     *      a {@link Proc1} as described above
     */
    default Proc1<P1> curry(P2 p2) {
        return (p1) -> accept(p1, p2);
    }

    /**
     * Returns a `Proc2` instance that when called will run the
     * `before` {@link BiConsumer} first and then this procedure.
     *
     * @param before
     *      a {@link BiConsumer} to be invoked before this procedure
     * @return
     *      a composite `Proc2` instance as described above
     */
    default Proc2<P1, P2> nowThat(BiConsumer<? super P1, ? super P2> before) {
        $.ensureNotNull(before);
        return (p1, p2) -> {
            before.accept(p1, p2);
            run(p1, p2);
        };
    }

    /**
     * Returns a `Proc2` instance that when called will run this
     * procedure first, if there are any exception encountered then
     * run the `fallback`.
     *
     * @param fallback
     *      a {@link Consumer} provided as fallback procedure
     * @return
     *      an new `Proc2` instance as described above
     */
    default Proc2<P1, P2> orElse(BiConsumer<? super P1, ? super P2> fallback) {
        $.ensureNotNull(fallback);
        return (p1, p2) -> runOrElse(p1, p2, fallback);
    }

    /**
     * Run this procedure with fallback {@link Consumer} specified.
     *
     * If there are exception encountered running this procedure, then it will
     * call fallback.
     *
     * @param p1
     *      the first param to run this procedure
     * @param p2
     *      the second param to run this procedure
     * @param fallback
     *      the fallback {@link Consumer} to run if exception encountered running
     *      this procedure
     */
    default void runOrElse(P1 p1, P2 p2, BiConsumer<? super P1, ? super P2> fallback) {
        $.ensureNotNull(fallback);
        try {
            run(p1, p2);
        } catch (Exception e) {
            fallback.accept(p1, p2);
        }
    }

    /**
     * Returns a {@link Func1} instance that when applied, will call this
     * procedure with the parameter and return `null`.
     *
     * @return
     *      A function as described above.
     */
    default Func2<P1, P2, ?> toFunction() {
        return (p1, p2) -> {
            run(p1, p2);
            return null;
        };
    }

    /**
     * Returns a {@link Proc2} instance from a {@link Consumer} so that
     * when the instance is called, it will delegate to
     * {@link Consumer#accept(Object)} method on the consumer instance.
     *
     * @param consumer
     *      A {@link Consumer} consumer instance
     * @param <P1>
     *      The type of the first parameter that consumer takes
     * @param <P2>
     *      The type of the second parameter that consumer takes
     * @return
     *      A {@link Proc2} instance as described above
     */
    static <P1, P2> Proc2<P1, P2> of(BiConsumer<? super P1, ? super P2> consumer) {
        if (consumer instanceof Proc2) {
            return (Proc2<P1, P2>) consumer;
        }
        return consumer::accept;
    }

    /**
     * Returns a {@link Proc2} instance that when invoking {@link #run(Object, Object)}
     * will {@link Func1#apply(Object) apply} the param to the `function`,
     * and ignore the return value.
     *
     * @param function
     *      A {@link Func1} instance.
     * @param <P1>
     *      The type of the first parameter the `function` applied.
     * @param <P2>
     *      The type of the second parameter the `function` applied.
     * @return
     *      A {@link Proc2} instance as described above.
     */
    @SuppressWarnings("ReturnValueIgnored")
    static <P1, P2> Proc2<P1, P2> of(Func2<? super P1, ? super P2, ?> function) {
        return function::apply;
    }

}
