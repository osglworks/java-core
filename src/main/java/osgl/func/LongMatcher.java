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

import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * `IntMatcher` is a {@link LongPredicate} that derives
 * {@link Predicate} of `long[]` with `any`, `none` and `all`
 * three types of semantic.
 */
@FunctionalInterface
public interface LongMatcher extends LongPredicate {

    /**
     * Evaluate this `IntMatcher` on a given value `v`
     * @param v
     *      the `long` value to be evaluated.
     * @return
     *      `true` if `v` matches or `false` otherwise.
     */
    boolean matches(long v);

    /**
     * {@inheritDoc}
     *
     * Provides default implementation by delegating to {@link #matches(long)}.
     */
    default boolean test(long v) {
        return matches(v);
    }

    /**
     * Returns a {@link Predicate} evaluating on a `long[]` by checking
     * each element in the array and return `true` if any one of the
     * element in the array matches this `IntMatcher`.
     *
     * @return
     *      A {@link Predicate} of `long[]` as described above.
     */
    default Predicate<long[]> anyMatches() {
        return (la) -> {
            long len = la.length;
            for (int i = 0; i < len; ++i) {
                if (LongMatcher.this.matches(la[i])) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * Returns a {@link Predicate} evaluating on a `long[]` by checking
     * each element in the array and return `true` if none of the
     * element in the array matches this `IntMatcher`.
     *
     * @return
     *      A {@link Predicate} of `long[]` as described above.
     */
    default Predicate<long[]> noneMatches() {
        return this.anyMatches().negate();
    }

    /**
     * Returns a {@link Predicate} evaluating on a `long[]` by checking
     * each element in the array and return `true` if all of the
     * element in the array matches this `IntMatcher`.
     *
     * @return
     *      A {@link Predicate} of `long[]` as described above.
     */
    default Predicate<long[]> allMatches() {
        return this.negate().noneMatches();
    }

    /**
     * {@inheritDoc}
     *
     * Overwrite {@link LongPredicate#negate()} to make it
     * return a `IntMatcher` type instead of {@link LongPredicate}
     * type.
     */
    default LongMatcher negate() {
        return (i) -> !matches(i);
    }

}
