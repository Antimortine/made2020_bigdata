from typing import Iterable, Callable, Any


def parse_csv_column(lines: Iterable[str],
                     column_idx: int,
                     converter: Callable[[str], Any],
                     sep: str = ',') -> Iterable[Any]:
    for line in lines:
        parts = line.split(sep)
        try:
            value = parts[column_idx]
            yield converter(value)
        except (IndexError, ValueError):
            continue


def parse_airbnb_price(lines: Iterable[str]) -> Iterable[int]:
    return parse_csv_column(lines, -7, int)


def combine_mean(mean1: float, mean2: float, n1: int, n2: int) -> float:
    return (n1 * mean1 + n2 * mean2) / (n1 + n2)


def combine_var(var1: float, var2: float, mean1: float, mean2: float, n1: int, n2: int) -> float:
    return (n1 * var1 + n2 * var2) / (n1 + n2) + n1 * n2 * ((mean1 - mean2) / (n1 + n2)) ** 2
