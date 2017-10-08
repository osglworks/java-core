package osgl.func.util;

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

import osgl.func.Func1;
import osgl.func.Proc0;
import osgl.func.Proc1;

import java.util.Objects;

/**
 * Adapt a {@link Proc1} instance to {@link Func1} type.
 */
public class Proc1ToFunc1Adaptor<P1, R> implements Func1<P1, R> {

    private Proc1<P1> proc;

    /**
     * Construct the adaptor with a non-null {@link Proc0} instance.
     *
     * @param proc
     *      the procedure instance
     * @throws NullPointerException
     *      if the `proc` instance is `null`
     */
    public Proc1ToFunc1Adaptor(Proc1 proc) {
        Objects.requireNonNull(proc);
        this.proc = proc;
    }

    /**
     * Implement the {@link Func1#apply(Object)} by calling {@link Proc1#run(Object)} on
     * the `proc` instance, and return `null`.
     *
     * @param param
     *      a parameter
     * @return
     *      `null`
     */
    @Override
    public R apply(P1 param) {
        proc.run(param);
        return null;
    }
}
