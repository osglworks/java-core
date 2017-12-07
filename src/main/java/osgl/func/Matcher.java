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

import java.util.function.Predicate;

/**
 * A `Matcher` is a {@link Predicate} that can be used to derive
 * {@link Predicate} of collection of target elements
 *
 * @param <T>
 *      The type of the target to the predicate
 */
@FunctionalInterface
public interface Matcher<T> extends Predicate<T> {

    /**
     * Check if target element `t` matches.
     * @param t
     *      the target element to be evaluated
     * @return
     *      `true` if `t` matches or `false` otherwise
     */
    boolean matches(T t);

    /**
     * Provides default implementation of {@link Predicate#test(Object)} that
     * delegate the test logic to {@link #matches(Object)}.
     *
     * @param t
     *      the target element to be tested
     * @return
     *      the result of {@link #matches(Object)}
     */
    @Override
    default boolean test(T t) {
        return matches(t);
    }


    default <TC extends Iterable<? extends T>> Predicate<TC> anyMatches() {
        return (tc) -> {
            for (T e: tc) {
                if (Matcher.this.test(e)) {
                    return true;
                }
            }
            return false;
        };
    }

    default <TC extends Iterable<T>> Predicate<TC> noneMatches() {
        return this.<TC>anyMatches().negate();
    }

    default <TC extends Iterable<T>> Predicate<TC> allMatches() {
        return this.negate().noneMatches();
    }

    default Matcher<T> negate() {
        return (e) -> !matches(e);
    }

    static <T> Matcher<T> of(Predicate<? super T> predicate) {
        return predicate::test;
    }

}
