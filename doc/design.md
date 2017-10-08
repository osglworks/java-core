# OSGL core design

This document takes notes on design decisions of coding OSGL core library

### Why not let `ProcX` extends `FuncX` or vice versa?

`Proc1` extends from `Consumer` and `Func1` extends `Function`, both defined the `andThen` method, which cause ambiguous call on the instance that implements both interfaces.

### `ProcX` fields/methods

* `NIL` - a proc that does nothing
* `run(...)` - the major logic of the procedure
* composition
    * `andThen(after)` - compose with after proc
    * `nowThat(before)` - compose with before proc
* fallback
    * `orElse(fallback)` - compose with fallback
    * `runOrElse(...fallback)` - run with fallback
* conversion
    * `toFunction()` - adapt to function
* factory
    * `of(Runnable)` - adapt from runnable
    * `of(FuncX)` - adapt from function

### `FuncX` fields/methods

* `NIL` - a func that does nothing and returns `null`
* `apply(...)` - the major logic of the function 
* composition
    * `andThen(after)` - compose with after function
    * `nowThat(before)` - compose with before function (except `Func0`)
* fallback
    * `applyOrElse(fallback)` - apply with fallback function
    * `orElse(fallback)` - compose with fallback function
    * `applyOrElse(fallbackValue)` - apply with fallback value
    * `orElse(fallbackValue)` - compose with fallback value
* conversation
    * `toProcedure()` - adapt to procedure
* factory
    * `constant(x)` - create function that returns contant `x`
