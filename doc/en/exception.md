# OSGL Core - Exception Utilities

OSGL core provides ways for easy Exception processing/handling

## `FastRuntimeException`

The `FastRuntimeException` is provided mainly to support the case when it needs to break through control flow, for example break out an iteration from within functions; or return a result directly from deep inside the HTTP request handling.

A major thing about `FastRuntimeException` is that it has an empty implementation of `fillInStackTrace()` which is most expensive parts of constructing an `Exception` instance.