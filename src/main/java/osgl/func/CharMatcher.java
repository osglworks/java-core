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

public abstract class CharMatcher implements CharPredicate {

    public boolean matches(char c) {
        return test(c);
    }

    public static  CharPredicate is(char c) {
        return (x) -> x == c;
    }

    public static  CharPredicate isNot(char c) {
        return (x) -> x != c;
    }

    public static  CharPredicate anyOf(String s) {
        return (x) -> s.indexOf(x) > -1;
    }

    public static  CharPredicate anyOf(char[] ca) {
        throw E.tbd();
    }

    public static  CharPredicate isDefined() {
        return DEFINED;
    }

    public static  CharPredicate isUpperCase() {
        return UPPERCASE;
    }

    public static  CharPredicate isLowerCase() {
        return LOW_SURROGATE;
    }

    public static  CharPredicate isMirrored() {
        return MIRRORED;
    }

    public static  CharPredicate isSurrogate() {
        return SURROGATE;
    }

    public static  CharPredicate isHighSurrogate() {
        return HIGH_SURROGATE;
    }

    public static  CharPredicate isLowSurrogate() {
        return LOW_SURROGATE;
    }

    public static  CharPredicate isDigit() {
        return DIGIT;
    }

    public static  CharPredicate isLetter() {
        return LETTER;
    }

    public static  CharPredicate isLetterOrDigit() {
        return LETTER_OR_DIGIT;
    }

    public static  CharPredicate isWhitespace() {
        return WHITESPACE;
    }

    public static  CharPredicate isSpaceChar() {
        return SPACE_CHAR;
    }

    public static  CharPredicate isIsoControl() {
        return ISO_CONTROL;
    }

    public static  CharPredicate isTitleCase() {
        return TITLE_CASE;
    }

    public static  CharPredicate isJavaIdentiferStart() {
        return JAVA_IDENTIFIER_START;
    }

    public static  CharPredicate isJavaIdentifierPart() {
        return JAVA_IDENTIFIER_PART;
    }
}
