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

import osgl.$;
import osgl.Option;

import java.util.Iterator;

class CompositeIterator<T> extends StatefulIterator<T> {
    private final Iterator<? extends T> head;
    private final Iterator<? extends T> tail;
    private volatile boolean headIterated;

    CompositeIterator(Iterator<? extends T> head, Iterator<? extends T> tail) {
        this.head = $.requireNotNull(head);
        this.tail = $.requireNotNull(tail);
    }

    @Override
    protected Option<T> getCurrent() {
        if (headIterated) {
            if (tail.hasNext()) {
                return $.any(tail.next());
            } else {
                return $.none();
            }
        } else {
            if (head.hasNext()) {
                return $.some(head.next());
            } else {
                headIterated = true;
                return getCurrent();
            }
        }
    }
}
