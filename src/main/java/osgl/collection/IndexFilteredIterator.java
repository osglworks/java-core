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
import java.util.function.IntPredicate;

class IndexFilteredIterator<T> extends StatefulIterator<T> {
    private final Iterator<? extends T> itr;
    private final IntPredicate filter;
    private int cursor;

    public IndexFilteredIterator(Iterator<? extends T> iterator, IntPredicate filter) {
        itr = iterator;
        this.filter = filter;
    }

    private boolean rawHasNext() {
        return itr.hasNext();
    }

    private T rawNext() {
        cursor++;
        return itr.next();
    }

    @Override
    protected Option<T> getCurrent() {
        while (rawHasNext()) {
            int curCursor = cursor;
            T t = rawNext();
            if (filter.test(curCursor)) {
                return $.some(t);
            }
        }
        return $.none();
    }
}
