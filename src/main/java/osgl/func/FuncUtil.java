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

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provides utility methods support functional programming.
 */
public class FuncUtil {
    FuncUtil() {}

    /**
     * Convert a JDK {@link Supplier} to OSGL {@link Func0}.
     *
     * This method is an alias of {@link Func0#of(Supplier)}.
     *
     * @param supplier
     *      The JDK {@link Supplier} instance
     * @param <R>
     *      The type of supplier return value
     * @return
     *      A OSGL {@link Func0} instance
     */
    public static <R> Func0<R> asFunc0(Supplier<R> supplier) {
        return Func0.of(supplier);
    }

    /**
     * Convert a JDK {@link Function} instance to a OSGL {@link Func1} instance.
     *
     * This method is an alias of {@link Func1#of(Function)}.
     *
     * @param function
     *      the JDK {@link Function} instance
     * @param <P1>
     *      the type of parameter the `function` takes
     * @param <R>
     *      the type of value the `function` returns
     * @return
     *      a OSGL {@link Func1} instance
     */
    public static <P1, R> Func1<P1, R> asFunc1(Function<P1, R> function) {
        return Func1.of(function);
    }

    /**
     * throw out a {@link Break} instance.
     */
    public static void breakOut() {
        throw new Break();
    }

    /**
     * Throw out a {@link Break} instance with payload
     * object specified.
     *
     * @param payload
     *      the payload
     */
    public static void breakOut(Object payload) {
        throw new Break(payload);
    }

    /**
     * Alias of {@link #asFunc0(Supplier)}.
     *
     * @param supplier
     *      The JDK {@link Supplier} instance
     * @param <R>
     *      The type of supplier return value
     * @return
     *      A OSGL {@link Func0} instance
     */
    public static <R> Func0<R> f0(Supplier<R> supplier) {
        return asFunc0(supplier);
    }

    /**
     * Alias of {@link #asFunc1(Function)}.
     *
     * @param function
     *      the JDK {@link Function} instance
     * @param <P1>
     *      the type of parameter the `function` takes
     * @param <R>
     *      the type of value the `function` returns
     * @return
     *      a OSGL {@link Func1} instance
     */
    public static <P1, R> Func1<P1, R> f1(Function<P1, R> function) {
        return asFunc1(function);
    }
}
