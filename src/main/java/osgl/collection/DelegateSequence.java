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

import java.util.Iterator;

class DelegateSequence<T> implements Sequence<T> {

    private final Sequence<? extends T> seq;

    DelegateSequence(Sequence<? extends T> seq) {
        this.seq = $.requireNotNull(seq);
    }

    @Override
    public Iterator<T> iterator() {
        return (Iterator<T>) seq.iterator();
    }

    static <T> DelegateSequence<T> of(Sequence<? extends T> seq) {
        if (seq instanceof DelegateSequence) {
            return (DelegateSequence<T>) seq;
        }
        return new DelegateSequence<>(seq);
    }

}
