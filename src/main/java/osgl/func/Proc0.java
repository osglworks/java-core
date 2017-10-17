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

/**
 * A procedure that takes no parameters.
 */
public interface Proc0 extends FuncBase, Runnable {

    /**
     * A `Proc0` that does nothing.
     */
    Proc0 NIL = () -> {
    };

    /**
     * Implement the logic of the procedure.
     */
    @Override
    void run();

    /**
     * Returns a `Proc0` that when called, will call this
     * procedure first and then call the `runnable`.
     *
     * @param after
     *      a {@link Runnable} to be called after this procedure
     * @return
     *      a `Proc0` instance as described above
     * @throws NullPointerException
     *      if `after` is `null`
     */
    default Proc0 andThen(Runnable after) {
        $.ensureNotNull(after);
        return () -> {
            run();
            after.run();
        };
    }

    /**
     * Returns a `Proc0` that when called, will call the
     * `runnable` specified first and then call this procedure.
     *
     * @param before
     *      a {@link Runnable} to be called before this procedure
     * @return
     *      a `Proc0` procedure as described above
     * @throws NullPointerException
     *      if `before` is `null`
     */
    default Proc0 nowThat(Runnable before) {
        $.ensureNotNull(before);
        return () -> {
            before.run();
            run();
        };
    }

    /**
     * Returns a `Proc0` that when invoked will run
     * this procedure first, in case of Exception then
     * run the `fallback`.
     *
     * @param fallback
     *      A {@link Runnable} fallback
     * @return
     *      An new `Proc0` instance as described above
     * @throws NullPointerException
     *      if `fallback` is `null`
     * @see #runOrElse(Runnable)
     */
    default Proc0 orElse(Runnable fallback) {
        $.ensureNotNull(fallback);
        return () -> runOrElse(fallback);
    }

    /**
     * Run this procedure, in case of exception then run the fallback.
     *
     * @param fallback
     *      A {@link Runnable} as fallback of this procedure.
     * @throws NullPointerException
     *      if `fallback` is `null`
     * @see #orElse(Runnable)
     */
    default void runOrElse(Runnable fallback) {
        $.ensureNotNull(fallback);
        try {
            run();
        } catch (Exception e) {
            fallback.run();
        }
    }

    /**
     * Returns a {@link Func0} instance that when {@link Func0#apply()} method
     * is invoked, call the {@link #run()} method on this instance and return
     * `null`.
     *
     * @param <T>
     *      The type of the parameter the function takes.
     * @return
     *      a {@link Func0} instance as described.
     */
    default <T> Func0<T> toFunction() {
        return () -> {
            run();
            return null;
        };
    }

    /**
     * Convert a {@link Func0} function to `Proc0`.
     *
     * This method calls {@link Func0#toProcedure()} to
     * do the job.
     *
     * @param function
     *      A {@link Func0} instance
     * @return
     *      A `Proc0` that calls {@link Func0#apply()} on the `function` and
     *      ignore the return value.
     * @throws NullPointerException
     *      if `function` is `null`
     */
    static Proc0 of(Func0<?> function) {
        return function.toProcedure();
    }

    /**
     * Convert a general {@link Runnable} instance to
     * `Proc0`.
     *
     * @param runnable
     *      A {@link Runnable} instance
     * @return
     *      A {@link Proc0} instance than when called will run
     *      the `runnable.run()` method
     * @throws NullPointerException
     *      if `runnable` is `null`
     */
    static Proc0 of(Runnable runnable) {
        $.ensureNotNull(runnable);
        if (runnable instanceof Proc0) {
            return (Proc0) runnable;
        }
        return runnable::run;
    }

}
