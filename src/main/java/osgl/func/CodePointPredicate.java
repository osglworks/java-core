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

import java.util.PrimitiveIterator;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * `CodePointPredicate` is a specialized {@link IntPredicate} with
 * extension methods to create {@link Predicate} of {@link String}
 * based on the `CodePointPredicate`.
 */
public interface CodePointPredicate extends IntPredicate {


    /**
     * @see Character#isUpperCase(int)
     */
    CodePointPredicate UPPERCASE = Character::isUpperCase;

    /**
     * @see Character#isLowerCase(int)
     */
    CodePointPredicate LOWERCASE = Character::isLowerCase;

    /**
     * @see Character#isDefined(int)
     */
    CodePointPredicate DEFINED = Character::isDefined;

    /**
     * @see Character#isValidCodePoint(int)
     */
    CodePointPredicate VALID = Character::isValidCodePoint;

    /**
     * @see Character#isMirrored(int)
     */
    CodePointPredicate MIRRORED = Character::isMirrored;

    /**
     * @see Character#isBmpCodePoint(int)
     */
    CodePointPredicate BMP = Character::isBmpCodePoint;

    /**
     * @see Character#isIdeographic(int)
     */
    CodePointPredicate IDEO_GRAPHIC = Character::isIdeographic;

    /**
     * @see Character#isDigit(int)
     */
    CodePointPredicate DIGIT = Character::isDigit;

    /**
     * @see Character#isLetter(int)
     */
    CodePointPredicate LETTER = Character::isLetter;

    /**
     * @see Character#isLetterOrDigit(int)
     */
    CodePointPredicate LETTER_OR_DIGIT = Character::isLetterOrDigit;

    /**
     * @see Character#isWhitespace(int)
     */
    CodePointPredicate WHITESPACE = Character::isWhitespace;

    /**
     * @see Character#isSpaceChar(int)
     */
    CodePointPredicate SPACE_CHAR = Character::isSpaceChar;

    /***
     * @see Character#isISOControl(int)
     */
    CodePointPredicate ISO_CONTROL = Character::isISOControl;

    /**
     * @see Character#isTitleCase(int)
     */
    CodePointPredicate TITLE_CASE = Character::isTitleCase;

    /**
     * @see Character#isJavaIdentifierStart(int)
     */
    CodePointPredicate JAVA_IDENTIFIER_START = Character::isJavaIdentifierStart;

    /**
     * @see Character#isJavaIdentifierPart(int)
     */
    CodePointPredicate JAVA_IDENTIFIER_PART = Character::isJavaIdentifierPart;

    /**
     * Returns a {@link Predicate} of {@link String} that test
     * all char in a given string and return `true` if any one
     * of the {@link Character#codePointAt(CharSequence, int) code point}
     * pass the test of this `CharPredicate`.
     *
     * If the given string is empty then the predicate will return
     * `false`.
     *
     * @return a {@link Predicate} as described above.
     */
    default Predicate<String> any() {
        return (s) -> {
            int len = s.length();
            for (int i = 0; i < len; ++i) {
                if (CodePointPredicate.this.test(Character.codePointAt(s, i))) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * Returns a {@link Predicate} of {@link String} that test
     * all char in a given string and return `true` if none
     * of the {@link Character#codePointAt(CharSequence, int) code point}
     * pass the test of this `CharPredicate`.
     *
     * If the given string is empty then the predicate will return
     * `true`.
     *
     * @return a {@link Predicate} as described above.
     */
    default Predicate<String> none() {
        return any().negate();
    }

    /**
     * Returns a {@link Predicate} of {@link String} that test
     * all char in a given string and return `true` if all
     * of the {@link Character#codePointAt(CharSequence, int) code point}
     * pass the test of this `CharPredicate`.
     *
     * If the given string is empty then the predicate will return
     * `false`.
     *
     * @return a {@link Predicate} as described above.
     */
    default Predicate<String> all() {
        return negate().none();
    }

    /**
     * Overwrite {@link IntPredicate#negate()} method so that
     * it returns `CodePointPredicate` type instead of
     * {@link IntPredicate} type.
     *
     * @return
     *      a predicate that represents the logical negation of this
     *      predicate
     */
    default CodePointPredicate negate() {
        return (value) -> !test(value);
    }

}
