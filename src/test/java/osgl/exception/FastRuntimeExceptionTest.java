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

import org.junit.Test;
import osgl.ut.TestBase;

public class FastRuntimeExceptionTest extends TestBase {

    protected FastRuntimeException ex;

    @Test
    public void itShallNotHaveStacktraceFilled() {
        ex = new FastRuntimeException();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        eq(0, stackTrace.length);
    }

    @Test
    public void testConstructWithMessage() {
        String message = "m" + System.currentTimeMillis();
        ex = new FastRuntimeException(message);
        eq(message, ex.getMessage());
    }

    @Test
    public void testConstructWithMessageFormat() {
        String template = "m%s";
        long ms = System.currentTimeMillis();
        ex = new FastRuntimeException(template, ms);
        eq(String.format(template, ms), ex.getMessage());
    }

    @Test
    public void testConstructWithCause() {
        Throwable cause = new NullPointerException();
        ex = new FastRuntimeException(cause);
        eq(cause, ex.getCause());
    }

    @Test
    public void testConstructWithCauseAndMessageFormat() {
        String template = "m%s";
        long ms = System.currentTimeMillis();
        Throwable cause = new NullPointerException();
        ex = new FastRuntimeException(cause, template, ms);
        eq(cause, ex.getCause());
        eq(String.format(template, ms), ex.getMessage());
    }

}
