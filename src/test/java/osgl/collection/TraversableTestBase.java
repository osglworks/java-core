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

import static osgl.Strings.Matcher.*;

import org.junit.Ignore;
import org.junit.Test;
import osgl.$;
import osgl.ut.TestBase;
import util.StringUtil;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Test any {@link Traversable} implementations
 */
@Ignore
public abstract class TraversableTestBase extends TestBase {

    protected abstract <T> Traversable<T> create(T ... elements);

    @Test
    public void testMap() {
        Traversable<String> target = create("foo", "X", "bar");
        Traversable<Integer> mapped = target.map(String::length);
        Iterator<Integer> itr = mapped.iterator();
        eq(3, itr.next());
        eq(1, itr.next());
        eq(3, itr.next());
    }

    @Test
    public void testFlatMap() {
        Traversable<String> target = create("ab", "xyz");
        Traversable<String> mapped = target.flatMap(StringUtil::permutationOf);
        Set<String> set = new HashSet<>();
        for (String s : mapped) {
            set.add(s);
        }
        eq(8, set.size());
    }

    @Test
    public void testReduce() {
        Traversable<String> target = create("foo", "bar");
        eq($.some("foobar"), target.reduce((s1, s2) -> s1 + s2));
    }

    @Test
    public void testReduceWithInitialValue() {
        Traversable<String> target = create("s", "he");
        eq(3, target.reduce(0, (n, s) -> n + s.length()));
    }

    @Test
    public void matchTests() {
        Traversable<String> target = create("Foo", "bar");

        no(target.allMatch(noUpperCase()));
        no(target.allMatch(hasUpperCase()));
        yes(create("FOO", "BAR").allMatch(noLowerCase()));
        yes(create("foo", "bar").allMatch(noUpperCase()));

        yes(target.anyMatch(noUpperCase()));
        no(target.anyMatch(allInUpperCase()));
        yes(target.noneMatch(allInLowerCase()));
    }

}
