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

import java.util.Iterator;

class CompositeSequence<T> implements Sequence<T> {

    private final Sequence<? extends T> head;

    private final Sequence<? extends T> tail;

    CompositeSequence(Sequence<? extends T> head, Sequence<? extends T> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.composite(head.iterator(), tail.iterator());
    }

    static <T> CompositeSequence<T> of(Sequence<? extends T> head, Sequence<? extends T> tail) {
        return new CompositeSequence<>(head, tail);
    }
}
