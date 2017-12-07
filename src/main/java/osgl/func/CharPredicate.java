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

import osgl.exception.E;

import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of one `int`-valued
 * argument. This is the `int`-consuming primitive type specialization of
 * {@link Predicate}.
 *
 * This is a [functional interface](https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html)
 * whose functional method is {@link #test(char)}.
 *
 * @see Predicate
 */
@FunctionalInterface
public interface CharPredicate {

    /**
     * @see Character#isUpperCase(char)
     */
    CharPredicate UPPERCASE = Character::isUpperCase;

    /**
     * @see Character#isLowerCase(char)
     */
    CharPredicate LOWERCASE = Character::isLowerCase;

    /**
     * @see Character#isDefined(char)
     */
    CharPredicate DEFINED = Character::isDefined;

    /**
     * @see Character#isMirrored(char)
     */
    CharPredicate MIRRORED = Character::isMirrored;

    /**
     * @see Character#isSurrogate(char)
     */
    CharPredicate SURROGATE = Character::isSurrogate;

    /**
     * @see Character#isHighSurrogate(char)
     */
    CharPredicate HIGH_SURROGATE = Character::isHighSurrogate;

    /**
     * @see Character#isLowerCase(char)
     */
    CharPredicate LOW_SURROGATE = Character::isLowSurrogate;

    /**
     * @see Character#isDigit(char)
     */
    CharPredicate DIGIT = Character::isDigit;

    /**
     * @see Character#isLetter(char)
     */
    CharPredicate LETTER = Character::isLetter;

    /**
     * @see Character#isLetterOrDigit(char) 
     */
    CharPredicate LETTER_OR_DIGIT = Character::isLetterOrDigit;

    /**
     * @see Character#isWhitespace(char)
     */
    CharPredicate WHITESPACE = Character::isWhitespace;

    /**
     * @see Character#isSpaceChar(char)
     */
    CharPredicate SPACE_CHAR = Character::isSpaceChar;

    /***
     * @see Character#isISOControl(char)
     */
    CharPredicate ISO_CONTROL = Character::isISOControl;

    /**
     * @see Character#isTitleCase(char)
     */
    CharPredicate TITLE_CASE = Character::isTitleCase;

    /**
     * @see Character#isJavaIdentifierStart(char)
     */
    CharPredicate JAVA_IDENTIFIER_START = Character::isJavaIdentifierStart;

    /**
     * @see Character#isJavaIdentifierPart(char)
     */
    CharPredicate JAVA_IDENTIFIER_PART = Character::isJavaIdentifierPart;

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param value
     *         the input argument
     * @return `true` if the input argument matches the predicate,
     * `false` otherwise
     */
    boolean test(char value);

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is `false`, then the `other`
     * predicate is not evaluated.
     *
     * Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * `other` predicate will not be evaluated.
     *
     * @param other
     *         a predicate that will be logically-ANDed with this predicate
     * @return a composed predicate that represents the short-circuiting logical
     * AND of this predicate and the `other` predicate
     * @throws NullPointerException
     *         if other is null
     */
    default CharPredicate and(CharPredicate other) {
        Objects.requireNonNull(other);
        return (value) -> test(value) && other.test(value);
    }

    /**
     * Returns a predicate that represents the logical negation of this
     * predicate.
     *
     * @return a predicate that represents the logical negation of this
     * predicate
     */
    default CharPredicate negate() {
        return (value) -> !test(value);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is `true`, then the `other`
     * predicate is not evaluated.
     *
     * Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * `other` predicate will not be evaluated.
     *
     * @param other
     *         a predicate that will be logically-ORed with this predicate
     * @return a composed predicate that represents the short-circuiting logical
     * OR of this predicate and the `other` predicate
     * @throws NullPointerException
     *         if other is null
     */
    default CharPredicate or(CharPredicate other) {
        Objects.requireNonNull(other);
        return (value) -> test(value) || other.test(value);
    }

    /**
     * Returns a {@link Predicate} of {@link String} that test
     * all char in a given string and return `true` if any one
     * of the char pass the test of this `CharPredicate`.
     *
     * If the given string is empty then the predicate will return
     * `false`.
     *
     * @return a {@link Predicate} as described above.
     */
    default <T extends CharSequence> Predicate<T> anyMatches() {
        return (s) -> {
            int len = s.length();
            for (int i = 0; i < len; ++i) {
                if (CharPredicate.this.test(s.charAt(i))) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * Returns a {@link Predicate} of {@link String} that test
     * all char in a given string and return `true` if none
     * of the char pass the test of this `CharPredicate`.
     *
     * If the given string is empty then the predicate will return
     * `true`.
     *
     * @return a {@link Predicate} as described above.
     */
    default <T extends CharSequence> Predicate<T> noneMatches() {
        return this.<T>anyMatches().negate();
    }

    /**
     * Returns a {@link Predicate} of {@link String} that test
     * all char in a given string and return `true` if all
     * of the char pass the test of this `CharPredicate`.
     *
     * If the given string is empty then the predicate will return
     * `false`.
     *
     * @return a {@link Predicate} as described above.
     */
    default <T extends CharSequence> Predicate<T> allMatches() {
        return negate().noneMatches();
    }

    /**
     * adapt this `CharPredicate` to a equivalent {@link IntPredicate}.
     *
     * @return a corresponding {@link IntPredicate}.
     */
    default IntPredicate toIntPredicate() {
        return (i) -> test((char)i);
    }

    /**
     * Apply this `CharPredicate` to all characters in a {@link CharSequence} and
     * return a {@link String} that contains only characters pass the test of
     * this `CharPredicate`.
     *
     * @param charSequence
     *      A {@link CharSequence} instance to be filtered
     * @return
     *      A {@link String} contains characters in `charSequence` that passed
     *      this `CharPredicate`
     */
    default String retainFrom(CharSequence charSequence) {
        StringBuilder sb = new StringBuilder();
        charSequence.chars().filter(toIntPredicate()).forEachOrdered(sb::append);
        return sb.toString();
    }

    default char[] retainFrom(char[] ca) {
        throw E.tbd();
    }

    default String removeFrom(String s) {
        return negate().retainFrom(s);
    }

    default char[] removeFrom(char[] ca) {
        return negate().retainFrom(ca);
    }

    default int countIn(String s) {
        return (int) s.chars().filter(toIntPredicate()).count();
    }

    default int countIn(char[] ca) {
        throw E.tbd();
    }


}
