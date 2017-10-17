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

import java.util.function.*;

/**
 * A function that takes no parameter and returns a value.
 *
 * A `Func0` implements {@link Proc0} in a way that it calls
 * {@link #apply()} method and discard the return value.
 *
 * A `Func0` implements {@link Supplier} by delegating
 * the {@link Supplier#get()} call to {@link #apply()}.
 *
 * @param <R>
 *         The type of the function return value
 */
@FunctionalInterface
public interface Func0<R> extends FuncBase, Supplier<R> {

    /**
     * A `Func0` that does nothing and always returns `null`.
     */
    Func0<?> NIL = () -> null;

    /**
     * Implement the function logic and return a value.
     *
     * @return A return value
     */
    R apply();

    /**
     * Returns a `Func0` instance that when applied, first
     * apply this function instance and use the return value
     * to apply to the `after` {@link Function}.
     *
     * @param after
     *         the after function
     * @param <T>
     *         the type of return value of the `after` function
     * @return a `Func0` instance as described above
     */
    default <T> Func0<T> andThen(Function<? super R, ? extends T> after) {
        $.ensureNotNull(after);
        return () -> after.apply(this.apply());
    }

    /**
     * Apply this function and return the `fallbackValue` if exception
     * encountered during applying this function.
     *
     * @param fallbackValue
     *         the fallback value if there are exception applying this function
     * @return the result of this function or `fallbackValue` if exception encountered
     */
    default R applyOrElse(final R fallbackValue) {
        return applyOrElse(constant(fallbackValue));
    }

    /**
     * Apply this function and call fallback function if exception
     * encountered during applying this function.
     *
     * @param fallback
     *         the fallback function to be applied in case of exception
     * @return
     *         the return value of this function or of the fallback function
     *         if exception encountered applying this function
     */
    default R applyOrElse(final Func0<? extends R> fallback) {
        $.ensureNotNull(fallback);
        try {
            return apply();
        } catch (Exception e) {
            return fallback.apply();
        }
    }

    /**
     * Delegate {@link Supplier#get()} call to {@link #apply()}.
     *
     * @return A return value
     */
    default R get() {
        return apply();
    }

    /**
     * Returns a `Func0` instance that when applied,
     * first apply this function, in case of exception
     * encountered then return the `fallbackValue`.
     *
     * @param fallbackValue
     *         the value to be returned if exception encountered
     *         applying this function
     * @return
     *      the result of this function or
     *      `fallbackValue` if exception encountered
     */
    default Func0<R> orElse(R fallbackValue) {
        if (null == fallbackValue) {
            return nil();
        }
        return () -> applyOrElse(fallbackValue);
    }

    /**
     * Returns a `Func0` instance that when applied, first
     * apply this function, in case of exception
     * then apply the fallback function.
     *
     * @param fallback
     *         the fallback function
     * @return a `Func0` instance as described above
     */
    default Func0<R> orElse(Func0<? extends R> fallback) {
        return () -> applyOrElse(fallback);
    }

    /**
     * Returns a {@link Proc0} type instance that when {@link Proc0#run()} method is
     * invoked, calling {@link #apply()} method on this instance and discard
     * the return value.
     *
     * @return a {@link Proc0} instance as described above.
     */
    default Proc0 toProcedure() {
        return this::apply;
    }

    /**
     * Returns a `Func0` that when applied always return a constant value.
     *
     * @param value
     *         the constant value
     * @param <T>
     *         the type of the constant value
     * @return a `Func0` instance as described above
     */
    static <T> Func0<T> constant(T value) {
        return () -> value;
    }

    /**
     * Returns a {@link BooleanFunc0} that when applied, always returns
     * a boolean constant.
     *
     * @param value
     *         the boolean constant value
     * @return the {@link BooleanFunc0} as described above
     */
    static BooleanFunc0 constant(boolean value) {
        return () -> value;
    }

    /**
     * Returns a {@link DoubleFunc0} that when applied, always returns
     * a double constant.
     *
     * @param value
     *         the double constant value
     * @return the {@link DoubleFunc0} as described above
     */
    static DoubleFunc0 constant(double value) {
        return () -> value;
    }

    /**
     * Returns an {@link IntFunc0} that when applied, always returns an
     * int constant.
     *
     * This method is an alias of {@link IntFunc0#constant(int)}
     *
     * @param value
     *         the int constant value
     * @return an {@link IntFunc0} as described above
     */
    static IntFunc0 constant(int value) {
        return () -> value;
    }

    /**
     * Returns a {@link LongFunc0} that when applied, always returns
     * a long constant.
     *
     * @param value
     *         the long constant value
     * @return the {@link LongFunc0} as described above
     */
    static LongFunc0 constant(long value) {
        return () -> value;
    }

    @SuppressWarnings("unchecked")
    static <T> Func0<T> nil() {
        return (Func0<T>) NIL;
    }

    /**
     * Factory method that convert a JDK {@link Supplier} to OSGL {@link Func0}.
     *
     * @param supplier
     *         The JDK {@link Supplier} instance
     * @param <T>
     *         The type of value that `supplier` returns
     * @return An OSGL {@link Func0} instance
     */
    static <T> Func0<T> of(Supplier<T> supplier) {
        if (supplier instanceof Func0) {
            return (Func0<T>) supplier;
        }
        return supplier::get;
    }

    /**
     * Convert an {@link BooleanSupplier} to a {@link BooleanFunc0}.
     *
     * @param booleanSupplier
     *         the {@link BooleanSupplier} instance
     * @return a {@link BooleanFunc0} instance
     */
    static BooleanFunc0 of(BooleanSupplier booleanSupplier) {
        if (booleanSupplier instanceof BooleanFunc0) {
            return (BooleanFunc0) booleanSupplier;
        }
        return booleanSupplier::getAsBoolean;
    }

    /**
     * Convert an {@link DoubleSupplier} to a {@link DoubleFunc0}.
     *
     * @param doubleSupplier
     *         the {@link DoubleSupplier} instance
     * @return a {@link DoubleFunc0} instance
     */
    static DoubleFunc0 of(DoubleSupplier doubleSupplier) {
        if (doubleSupplier instanceof DoubleFunc0) {
            return (DoubleFunc0) doubleSupplier;
        }
        return doubleSupplier::getAsDouble;
    }

    /**
     * Convert an {@link IntSupplier} to a {@link IntFunc0}.
     *
     * @param intSupplier
     *         the {@link IntSupplier} instance
     * @return a {@link IntFunc0} instance
     */
    static IntFunc0 of(IntSupplier intSupplier) {
        if (intSupplier instanceof IntFunc0) {
            return (IntFunc0) intSupplier;
        }
        return intSupplier::getAsInt;
    }

    /**
     * Convert an {@link LongSupplier} to a {@link LongFunc0}.
     *
     * @param longSupplier
     *         the {@link LongSupplier} instance
     * @return a {@link LongFunc0} instance
     */
    static LongFunc0 of(LongSupplier longSupplier) {
        if (longSupplier instanceof LongFunc0) {
            return (LongFunc0) longSupplier;
        }
        return longSupplier::getAsLong;
    }

    // TODO add lift to Option

}
