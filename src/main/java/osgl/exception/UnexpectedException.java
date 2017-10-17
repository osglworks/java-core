package osgl.exception;

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

/**
 * A {@link RuntimeException} class with more convenient constructors.
 */
public class UnexpectedException extends RuntimeException {

    /**
     * Construct a `UnexpectedException` instance.
     */
    public UnexpectedException() {
        super();
    }

    /**
     * Construct a `UnexpectedException` instance with a given message.
     *
     * @param message the message
     */
    public UnexpectedException(String message) {
        super(message);
    }

    /**
     * Construct a `UnexpectedException` instance with a string
     * format template and arguments.
     *
     * This method will call to {@link String#format(String, Object...)}
     * to build the final message and pass it to super constructor
     *
     * @param message the format template
     * @param args    the format arguments
     */
    public UnexpectedException(String message, Object... args) {
        super(String.format(message, args));
    }

    /**
     * Construct a `UnexpectedException` instance with a {@link Throwable cause}.
     *
     * @param cause the cause
     */
    public UnexpectedException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct a `UnexpectedException` instance. Below are required parameters:
     *
     * * a {@link Throwable cause}
     * * a string format template
     * * the string format arguments
     *
     * @param cause   the cause
     * @param message the string format template
     * @param args    the string format arguments
     * @see #UnexpectedException(Throwable)
     * @see #UnexpectedException(String, Object...)
     */
    public UnexpectedException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause);
    }

}
