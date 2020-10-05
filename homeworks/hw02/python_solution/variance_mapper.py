import sys
from utils import parse_airbnb_price

if __name__ == '__main__':
    count = 0
    prices_sum = 0.0
    squared_prices_sum = 0.0
    for price in parse_airbnb_price(sys.stdin):
        count += 1
        prices_sum += price
        squared_prices_sum += price ** 2
    mean_price = prices_sum / count
    mean_squared_price = squared_prices_sum / count
    variance = mean_squared_price - mean_price ** 2
    print(f'1\t{count}\t{mean_price}\t{variance}')
