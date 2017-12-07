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

import osgl.exception.E;

import java.util.function.Consumer;

/**
 * A `ReversibleSequence` is a {@link Sequence} that can be iterated through
 * in a reverse direction, e.g. from tail to head.
 *
 * @param <T>
 *      The type of the elements in the sequence.
 */
public interface ReversibleSequence<T> extends Sequence<T> {

    default ReversibleSequence<T> head(int n) {
        if (n < 0) {
            return tail(-n);
        }
        throw E.tbd();
    }

    /**
     * Returns a `ReversibleSequence` that contains the last `n` elements of this
     * `ReversibleSequence` if `n` is positive and this `Sequence` contains more
     * than `n` elements.
     *
     * If `n` is negative then returns the first `-n` elements in this sequence.
     * See {@link #head(int)}.
     *
     * If this `ReversibleSequence` is {@link Sized}, then it will check the size
     * of this `ReversibleSequence` and return the reference to this `ReversibleSequence`
     * if size is less then or equal to `n`.
     *
     * @param n
     *      the number of elements to be returned from tail
     * @return
     *      A `ReversibleSequence` as described above.
     */
    default ReversibleSequence<T> tail(int n) {
        if (n < 0) {
            return head(-n);
        }
        if (sized()) {
            if (n >= size()) {
                return this;
            }
        }
        throw new UnsupportedOperationException();
    }


    /**
     * {@inheritDoc}
     *
     * @return
     *      a reference to this `ReversibleSequence`
     */
    @Override
    default ReversibleSequence<T> accept(Consumer<? super T> visitor) {
        forEach(visitor);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     *      a reference to this `ReversibleSequence`
     */
    @Override
    default ReversibleSequence<T> each(Consumer<? super T> visitor) {
        forEach(visitor);
        return this;
    }
}
