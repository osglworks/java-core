package osgl.collection;

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

import org.junit.Test;

import java.util.Iterator;
import java.util.function.Predicate;

public class FilteredIteratorTest extends IteratorTestBase {

    @Test
    public void testFilterAll() {
        Predicate<String> filter = (s) -> s.startsWith("F") || s.startsWith("B");
        ceq(iterator("Foo", "Bar"), new FilteredIterator<>(testTarget.iterator(), filter, FilteredIterator.Type.ALL));
    }

    @Test
    public void testFilterWhile() {
        Predicate<String> filter = (s) -> Character.isUpperCase(s.charAt(0));
        ceq(iterator("Foo"), new FilteredIterator<>(testTarget.iterator(), filter, FilteredIterator.Type.WHILE));
    }

    @Test
    public void testFilterUtil() {
        Predicate<String> filter = (s) -> Character.isLowerCase(s.charAt(0));
        ceq(iterator("foo", "Bar", "bar"), new FilteredIterator<>(testTarget.iterator(), filter, FilteredIterator.Type.UNTIL));
    }

    @Test
    public void testEmptyIterator() {
        Predicate<String> filter = (s) -> Character.isLowerCase(s.charAt(0));
        Iterator<String> empty = iterator();
        ceq(empty, new FilteredIterator<>(empty, filter));
    }

}
