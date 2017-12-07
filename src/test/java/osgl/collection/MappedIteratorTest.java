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

public class MappedIteratorTest extends IteratorTestBase {

    private Function<String, Integer> mapper = String::length;

    @Test
    public void testMappedIterator() {
        Iterator<Integer> numbers = numbers(3, 3, 3, 3);
        ceq(numbers, new MappedIterator<>(testTarget.iterator(), mapper));
    }

    @Test
    public void testEmptyFlatMappedIterator() {
        ceq(numbers(), new MappedIterator<>(iterator(), mapper));
    }

    private Iterator<Integer> numbers(int ... numbers) {
        List<Integer> list = new ArrayList<>();
        for (int n: numbers) {
            list.add(n);
        }
        return list.iterator();
    }

}
