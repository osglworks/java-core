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

import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * `IntMatcher` is a {@link IntPredicate} that derives
 * {@link Predicate} of `int[]` with `any`, `none` and `all`
 * three types of semantic.
 */
@FunctionalInterface
public interface IntMatcher extends IntPredicate {

    /**
     * Evaluate this `IntMatcher` on a given value `v`
     * @param v
     *      the `int` value to be evaluated.
     * @return
     *      `true` if `v` matches or `false` otherwise.
     */
    boolean matches(int v);

    /**
     * {@inheritDoc}
     *
     * Provides default implementation by delegating to {@link #matches(int)}.
     */
    default boolean test(int v) {
        return matches(v);
    }

    /**
     * Returns a {@link Predicate} evaluating on a `int[]` by checking
     * each element in the array and return `true` if any one of the
     * element in the array matches this `IntMatcher`.
     *
     * @return
     *      A {@link Predicate} of `int[]` as described above.
     */
    default Predicate<int[]> anyMatches() {
        return (ia) -> {
            int len = ia.length;
            for (int i = 0; i < len; ++i) {
                if (IntMatcher.this.matches(ia[i])) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * Returns a {@link Predicate} evaluating on a `int[]` by checking
     * each element in the array and return `true` if none of the
     * element in the array matches this `IntMatcher`.
     *
     * @return
     *      A {@link Predicate} of `int[]` as described above.
     */
    default Predicate<int[]> noneMatches() {
        return this.anyMatches().negate();
    }

    /**
     * Returns a {@link Predicate} evaluating on a `int[]` by checking
     * each element in the array and return `true` if all of the
     * element in the array matches this `IntMatcher`.
     *
     * @return
     *      A {@link Predicate} of `int[]` as described above.
     */
    default Predicate<int[]> allMatches() {
        return this.negate().noneMatches();
    }

    /**
     * {@inheritDoc}
     *
     * Overwrite {@link IntPredicate#negate()} to make it
     * return a `IntMatcher` type instead of {@link IntPredicate}
     * type.
     */
    default IntMatcher negate() {
        return (i) -> !matches(i);
    }

}
