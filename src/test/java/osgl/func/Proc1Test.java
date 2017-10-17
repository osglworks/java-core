package osgl.func;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import osgl.exception.E;
import osgl.ut.TestBase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public class Proc1Test extends TestBase {

    protected List<String> strings = new ArrayList<>();
    protected Proc1<String> addToStrings = (s) -> strings.add(s);
    @Mock
    private Proc1<Object> mockProc1;

    @Before
    public void prepare() {
        strings.clear();
    }

    @Test
    public void itShallCallRunIfInvoke() {
        Object param = new Object();
        Mockito.doCallRealMethod().when(mockProc1).accept(param);
        mockProc1.accept(param);
        Mockito.verify(mockProc1, Mockito.times(1)).run(param);
    }

    public static class CompositionTest extends Proc1Test {

        private Consumer<String> after = (s) -> strings.add("'" + s + "'");

        private Consumer<String> before = after;

        @Test
        public void itShallRunAfterProcedureAfterThisProcedure() {
            addToStrings.andThen(after).run("foo");
            eq("foo", strings.get(0));
            eq("'foo'", strings.get(1));
        }

        @Test
        public void itShallRunBeforeProcedureBeforeThisProcedure() {
            addToStrings.nowThat(before).run("foo");
            eq("foo", strings.get(1));
            eq("'foo'", strings.get(0));
        }
    }

    public static class FallbackTest extends Proc1Test {
        Proc1<String> failCase = (s) -> {throw E.unexpected();};
        Proc1<String> fallback = (s) -> strings.add("**" + s + "**");

        @Test
        public void itShallNotCallfallbackIfNoException() {
            addToStrings.runOrElse("foo", fallback);
            yes(strings.contains("foo"));
            no(strings.contains("**foo**"));

            strings.clear();
            addToStrings.orElse(fallback).run("foo");
            yes(strings.contains("foo"));
            no(strings.contains("**foo**"));
        }

        @Test
        public void itShallCallFallbackIfExceptionEncountered() {
            failCase.runOrElse("foo", fallback);
            no(strings.contains("foo"));
            yes(strings.contains("**foo**"));

            strings.clear();
            failCase.orElse(fallback).run("foo");
            no(strings.contains("foo"));
            yes(strings.contains("**foo**"));
        }
    }

    public static class ConversionTest extends Proc1Test {
        @Test
        public void testToFunction() {
            addToStrings.toFunction().apply("foo");
            yes(strings.contains("foo"));
        }

        @Test
        public void testCurrying() {
            addToStrings.curry("foo").run();
            yes(strings.contains("foo"));
        }
    }

    public static class FactoryTest extends Proc1Test {
        @Test
        public void testOfConsumer() {
            Consumer<CharSequence> consumer = (cs) -> strings.add(cs.toString());
            Proc1.of(consumer).run("foo");
            yes(strings.contains("foo"));
        }
    }
}
