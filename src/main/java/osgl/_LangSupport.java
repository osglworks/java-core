package osgl;

import static osgl._ArraySupport.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class _LangSupport {

    private static final Class[] _primitiveTypes = {
            boolean.class, byte.class, short.class, char.class,
            int.class, long.class, float.class, double.class,
            boolean[].class, byte[].class, short[].class, char[].class,
            int[].class, long[].class, float[].class, double[].class,
    };

    private static final Map<Object, Class> _primitiveTypeLookup = new HashMap<>();
    static final Map<Object, Class> primitiveTypeLookup;

    private static final Map<Object, Object> _primitiveInstanceLookup = new HashMap<Object, Object>();
    static final Map primitiveInstanceLookup;

    private static final Map<Class, Class> _primitiveToWrapperLookup = new HashMap<>();
    static final Map<Class, Class> primitiveToWrapperLookup;

    private static final Map<Class, Class> _wrapperToPrmitiveLookup = new HashMap<>();
    static final Map<Class, Class> wrapperToPrimitiveLookup;

    static {
        _primitiveInstanceLookup.put(boolean.class, false);
        _primitiveInstanceLookup.put(byte.class, 0);
        _primitiveInstanceLookup.put(short.class, 0);
        _primitiveInstanceLookup.put(char.class, 0);
        _primitiveInstanceLookup.put(int.class, 0);
        _primitiveInstanceLookup.put(long.class, 0L);
        _primitiveInstanceLookup.put(float.class, 0f);
        _primitiveInstanceLookup.put(double.class, 0d);
        _primitiveInstanceLookup.put(boolean[].class, empty_boolean_array);
        _primitiveInstanceLookup.put(byte[].class, empty_byte_array);
        _primitiveInstanceLookup.put(short[].class, empty_short_array);
        _primitiveInstanceLookup.put(char[].class, empty_char_array);
        _primitiveInstanceLookup.put(int[].class, empty_int_array);
        _primitiveInstanceLookup.put(long[].class, empty_long_array);
        _primitiveInstanceLookup.put(float[].class, empty_float_array);
        _primitiveInstanceLookup.put(double[].class, empty_double_array);
        for (Class c : _primitiveTypes) {
            String cn = c.getSimpleName();
            String cnClass = cn + ".class";
            _primitiveTypeLookup.put(cn, c);
            _primitiveTypeLookup.put(cnClass, c);
            Object inst = _primitiveInstanceLookup.get(c);
            _primitiveInstanceLookup.put(cn, inst);
            _primitiveInstanceLookup.put(cnClass, inst);
        }
        primitiveInstanceLookup = Collections.unmodifiableMap(_primitiveInstanceLookup);
        primitiveTypeLookup = Collections.unmodifiableMap(_primitiveTypeLookup);

        _primitiveToWrapperLookup.put(int.class, Integer.class);
        _primitiveToWrapperLookup.put(boolean.class, Boolean.class);
        _primitiveToWrapperLookup.put(byte.class, Byte.class);
        _primitiveToWrapperLookup.put(short.class, Short.class);
        _primitiveToWrapperLookup.put(char.class, Character.class);
        _primitiveToWrapperLookup.put(long.class, Long.class);
        _primitiveToWrapperLookup.put(float.class, Float.class);
        _primitiveToWrapperLookup.put(double.class, Double.class);
        _primitiveToWrapperLookup.put(int[].class, Integer[].class);
        _primitiveToWrapperLookup.put(boolean[].class, Boolean[].class);
        _primitiveToWrapperLookup.put(byte[].class, Byte[].class);
        _primitiveToWrapperLookup.put(short[].class, Short[].class);
        _primitiveToWrapperLookup.put(char[].class, Character[].class);
        _primitiveToWrapperLookup.put(long[].class, Long[].class);
        _primitiveToWrapperLookup.put(float[].class, Float[].class);
        _primitiveToWrapperLookup.put(double[].class, Double[].class);
        primitiveToWrapperLookup = Collections.unmodifiableMap(_primitiveToWrapperLookup);

        for (Map.Entry<Class, Class> entry : _primitiveToWrapperLookup.entrySet()) {
            _wrapperToPrmitiveLookup.put(entry.getValue(), entry.getKey());
        }
        wrapperToPrimitiveLookup = Collections.unmodifiableMap(_wrapperToPrmitiveLookup);
    }

}
