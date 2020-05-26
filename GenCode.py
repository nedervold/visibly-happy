#!/usr/local/bin/python3
from typing import List

# MONAD = "Reader"
MONAD = "State"
PARAM = MONAD[0]


def generic(i: int) -> str:
    return chr(i + ord("A"))


def arg(i: int) -> str:
    return chr(i + ord("a"))


def arg2(i: int) -> str:
    return arg(i) + "2"


def generics(i: int) -> List[str]:
    return [generic(i) for i in range(0, i + 1)]


def result_generics(i: int) -> List[str]:
    res = list(PARAM)
    res.extend(generics(i))
    return res


def wrap(cs: List[str]) -> str:
    return "<" + ", ".join(cs) + ">"


def monadic(i: int) -> str:
    return MONAD + wrap([PARAM, generic(i)])


def liftN(i: int) -> str:
    return "lift%d" % i


def functionN(i: int) -> str:
    return "Function%d" % i


def func_args(i: int) -> str:
    args = [f"{functionN(i)}{wrap(generics(i))} func"]
    args.extend(monadic_args(i))
    return ", ".join(args)


def monadic_arg(i: int) -> str:
    return f"{monadic(i)} {arg(i)}"


def monadic_args(n: int) -> List[str]:
    return [monadic_arg(i) for i in range(0, n)]


def curried_type(n: int) -> str:
    def ty(i: int) -> str:
        if i + 1 == n:
            return f"Function1<{generic(i)}, {generic(i+1)}>"
        else:
            return f"Function1<{generic(i)}, {ty(i+1)}>"

    return ty(0)


def applies(i: int) -> str:
    lst = ["func"]
    lst.extend([f"apply{a}" for a in curried_args(i)])
    return ".".join(lst)


def curried_args(n: int) -> List[str]:
    return [f"({arg2(i)})" for i in range(0, n)]


def curried_value(i: int) -> str:
    args = curried_args(i)
    args.append(applies(i))
    return " -> ".join(args)


def curried(i: int) -> str:
    return f"{curried_type(i)} curried = {curried_value(i)};"


def return_val(i: int) -> str:
    assert i >= 0
    if i == 0:
        return f"{arg(i)}.map(curried)"
    else:
        return f"ap({return_val(i-1)}, {arg(i)})"


for i in range(2, 9):
    print(
        "public static",
        wrap(result_generics(i)),
        monadic(i),
        liftN(i),
        "(",
        func_args(i),
        "){",
    )
    print(curried(i))
    print(f"return {return_val(i-1)};")
    print("}")
