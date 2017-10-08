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
 * Procedure that take one parameter.
 *
 * `Proc1` extends JDK {@link Consumer} by delegating {@link Consumer#accept(Object)}
 * call to {@link #run(Object)} call.
 *
 * @param <P1>
 *     the type of the parameter
 */
public interface Proc1<P1> extends Consumer<P1> {

    /**
     * A `Proc1` that does nothing.
     */
    Proc1<?> NIL = (param) -> {};

    /**
     * Implement the logic of the procedure.
     *
     * @param param1
     *      a parameter
     */
    void run(P1 param1);

    /**
     * Implement {@link Consumer#accept(Object)} by calling
     * {@link #run(Object)} method.
     *
     * @param param1
     *      a parameter
     */
    @Override
    default void accept(P1 param1) {
        run(param1);
    }

    /**
     * Returns a `Proc1` instance that when called will
     * run this `Proc1` and followed by the `after` {@link Consumer}.
     *
     * @param after
     *      A {@link Consumer} to be run after this `Proc1`.
     * @return
     *      A composite `Proc1` instance as described above.
     */
    default Proc1<P1> andThen(Consumer<? super P1> after) {
        $.ensureNotNull(after);
        return (P1 param) -> {
            run(param);
            after.accept(param);
        };
    }

    default Proc1<P1> nowThat(Consumer<? super P1> before) {
        $.ensureNotNull(before);
        return (P1 param) -> {
            before.accept(param);
            run(param);
        };
    }

}
