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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class FlatMappedIteratorTest extends IteratorTestBase {

    private Function<String, Iterable<Character>> mapper = (s) -> {
        List<Character> list = new ArrayList<>();
        for (char c : s.toCharArray()) {
            list.add(c);
        }
        return list;
    };

    @Test
    public void testFlatMappedIterator() {
        Iterator<Character> chars = chars(
                'F', 'o', 'o',
                'f', 'o', 'o',
                'B', 'a', 'r',
                'b', 'a', 'r'
        );
        ceq(chars, new FlatMappedIterator<>(testTarget.iterator(), mapper));
    }

    @Test
    public void testEmptyFlatMappedIterator() {
        ceq(chars(), new FlatMappedIterator<>(iterator(), mapper));
    }

    private Iterator<Character> chars(char ... chars) {
        List<Character> list = new ArrayList<>();
        for (char c : chars) {
            list.add(c);
        }
        return list.iterator();
    }

}
