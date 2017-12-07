package osgl;

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

import osgl.func.CharMatcher;
import osgl.func.CodePointPredicate;

import java.util.function.Predicate;

public class Strings {

    /**
     * `Strings.Pair` or `S.Pair` is a shortcut to {@link osgl.collection.Pair}`<String, String>`.
     */
    public static class Pair extends osgl.collection.Pair<String, String> {

        public Pair(String s1, String s2) {
            super(s1, s2);
        }

    }

    /**
     * `Strings.Triple` or `S.Triple` is a shortcut to {@link osgl.collection.Triple}`<String, String, String>`
     */
    public static class Triple extends osgl.collection.Triple<String, String, String> {
        public Triple(String s, String s2, String s3) {
            super(s, s2, s3);
        }
   }

    /**
     * `S.Matcher` is an alias of {@link CharMatcher} with a few additonal String predicate
     * factory methods.
     */
    public static abstract class Matcher extends CharMatcher {

        private Matcher() {}

        public static Predicate<String> contains(CharSequence charSequence) {
            return (s) -> s.contains(charSequence);
        }

        public static Predicate<String> startsWith(CharSequence charSequence) {
            return (s) -> s.startsWith(charSequence.toString());
        }

        public static Predicate<String> endsWith(CharSequence charSequence) {
            return (s) -> s.endsWith(charSequence.toString());
        }

        public static Predicate<String> matches(String regex) {
            return (s) -> s.matches(regex);
        }

        public static Predicate<String> contentEquals(CharSequence charSequence) {
            return (s) -> s.contentEquals(charSequence);
        }

        public static Predicate<String> contentEquals(StringBuffer buffer) {
            return (s) -> s.contentEquals(buffer);
        }

        public static Predicate<String> allInUpperCase() {
            return isUpperCase().allMatches();
        }

        public static Predicate<String> allInLowerCase() {
            return isLowerCase().allMatches();
        }

        public static Predicate<String> noUpperCase() {
            return isUpperCase().negate().allMatches();
        }

        public static Predicate<String> noLowerCase() {
            return isLowerCase().negate().allMatches();
        }

        public static Predicate<String> hasUpperCase() {
            return isUpperCase().anyMatches();
        }

        public static Predicate<String> hasLowerCase() {
            return isLowerCase().anyMatches();
        }

        /**
         * `S.Matcher.Unicode` is an alias of {@link CodePointPredicate}
         */
        public static abstract class Unicode implements CodePointPredicate {
            private Unicode() {
            }
        }

    }

    /**
     * Check if a string is empty.
     *
     * A string is considered to be empty if
     * * the reference is `null`, or
     * * the string length is `0`
     *
     * @param s
     *      The string to be checked.
     * @return
     *      `true` if the string is empty.
     */
    public static boolean empty(String s) {
        return null == s || s.length() == 0;
    }

    /**
     * Alias of {@link #empty(String)}.
     *
     * @param s
     *      The string to be checked
     * @return
     *      `true` if `s` is empty or `false` otherwise.
     */
    public static boolean isEmpty(String s) {
        return empty(s);
    }

    /**
     * Negate of {@link #empty(String)}.
     *
     * @param s
     *      The string to be checked
     * @return
     *      `true
     */
    public static boolean notEmpty(String s) {
        return null != s && s.length() > 0;
    }

}
