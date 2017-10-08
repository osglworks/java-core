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

/**
 * Visitor is another expression of {@link Proc1}.
 *
 * OSGL Visitor is designed to implement {@link Proc1} with
 * a default implementation of {@link Proc1#run(Object)} that
 * delegate the call to {@link #visit(Object)} method.
 *
 * @param <P1>
 *     the type of the parameter
 */
@FunctionalInterface
public interface Visitor<P1> extends Proc1<P1> {

    /**
     * Implementation shall provide visiting logic
     * in this method.
     *
     * @param param
     *      The object to be visited.
     */
    void visit(P1 param);

    /**
     * In this implementation will call the {@link #visit(Object)}
     * method.
     *
     * @param param
     *      a parameter
     */
    default void run(P1 param) {
        visit(param);
    }

}
