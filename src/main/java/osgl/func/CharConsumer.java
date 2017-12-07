package osgl.func;

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

import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single `char`-valued argument and
 * returns no result.  This is the primitive type specialization of
 * {@link Consumer} for `char`.  Unlike most other functional interfaces,
 * `CharConsumer` is expected to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(char)}.
 *
 * @see Consumer
 */
@FunctionalInterface
public interface CharConsumer {

    /**
     * Performs this operation on the given argument.
     *
     * @param value
     *         the input argument
     */
    void accept(char value);

    /**
     * Returns a composed `CharConsumer` that performs, in sequence, this
     * operation followed by the `after` operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the `after` operation will not be performed.
     *
     * @param after
     *         the operation to perform after this operation
     * @return a composed `CharConsumer` that performs in sequence this
     * operation followed by the `after` operation
     * @throws NullPointerException
     *         if `after` is null
     */
    default CharConsumer andThen(CharConsumer after) {
        $.requireNotNull(after);
        return (char x) -> {
            accept(x);
            after.accept(x);
        };
    }
}
