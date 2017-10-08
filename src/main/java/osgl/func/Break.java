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

import osgl.exception.FastRuntimeException;

/**
 * Used to break out from functions in a iterating loop.
 *
 * A `Break` can carry a payload.
 */
public final class Break extends FastRuntimeException {

    /**
     * A payload to be carried through to the external caller.
     */
    private Object payload;

    /**
     * Construct a `Break` without payload.
     */
    public Break() {}

    /**
     * Construct a `Break` with payload.
     *
     * The payload can be accessed through {@link #payload()} call
     *
     * @param payload
     *      the payload
     */
    public Break(Object payload) {
        this.payload = payload;
    }

    /**
     * Returns the payload of the `Break` instance.
     *
     * @return
     *      the payload
     * @see #Break(Object)
     */
    public Object payload() {
        return payload;
    }

    /**
     * Create an new `Break` instance.
     * @return
     *      an new `Break` instance
     */
    public static Break breakOut() {
        return new Break();
    }

    /**
     * Create an new `Break` instance with payload specified.
     *
     * @param payload
     *      the payload to be carried out through the `Break`
     * @return
     *      A `Break` instance with payload
     */
    public static Break breakOut(Object payload) {
        return new Break(payload);
    }
}
