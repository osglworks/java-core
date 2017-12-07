# OSGL core design

This document takes notes on design decisions of coding OSGL core library

## Function extensions

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
    * `curry()` - return a procedure that takes one less arguments of this procedure with supplied parameter
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
    * `curry()` - return a function that takes one less arguments of this function with supplied parameter
* factory
    * `constant(x)` - create function that returns contant `x`

## Collection framework extensions

### Remove `isMutable()` and `isImutable()` methods from `Traversable`



### Remove mutability methods from `Traversable`

I decide to remove the following methods from `Traversable`:

* `isMutable()`
* `isImmutable()`
* `toMutable()`
* `toImmutable()`

These methods create a lot of workload to testing and it is very hard to make it clear semantic. For example
`Traversable.toMutable` might create a `Traversable` that is **not** mutable if the implementation is a 
delegating `Traversable` and the real datastructure itself is not mutable. The same thing happens to 
`isImmutable()` method also. It is very possible for a `Traversable` returns `true` for `isMutable()` call
but the underline datastructure is actually immutable.

### Should we create our own `Option` class or just use JDK `Optional` directly?

We go create our own `Option` class because JDK `Optinoal` is not `Serializable`

### `Sequence.head()` return type

1. let `Sequence.head()` returns `Option(T)`
2. let `Sequence.head()` returns `T` and throw out `NoSuchElementException` if `Sequence` has no element.

Decision: go with #2. The reason is `Sequence`'s first element might be `null` in which case `Option` will 
return `Option.none()` which will be the same case with calling `head()` on an empty `Sequence`.
 
### Why we don't overwrite `flatMap(Function)` method in `Sequence`

The `Traversable.flatMap` takes a `Function<? super T, ? extends Iterable<? extends R>>` as
parameter. The function applied on elements in the data container and returns an `Iterable`,
which might not be a `Sequence`. However we can't overwrite the method signature to something 
like

```java
Sequence<R> flatMap(Function<? super T, ? extends Sequence<? extends R>>); 
```

In which case compiler will complain that "name clash: <R>flatMap(java.util.function.Function<? super T,? extends java.lang.Iterable<? extends R>>) in osgl.collection.Traversable and <R>flatMap(java.util.function.Function<T,osgl.collection.Sequence<R>>) in osgl.collection.Sequence have the same erasure, yet neither overrides the other"

