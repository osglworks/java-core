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

public class Array<T> implements Sequence<T> {

    /**
     * The real array where element data is stored.
     */
    private final Object[] data;

    private final int size;

    public Array(T[] array) {
        this.data = array;
        this.size = data.length;
    }

    @Override
    public boolean sized() {
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Alias of {@link #size()}
     *
     * @return
     *      the number of elements in the array
     */
    public int length() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            int cur = 0;

            @Override
            public boolean hasNext() {
                return cur < size;
            }

            @Override
            public T next() {
                return (T) data[cur++];
            }
        };
    }

}
