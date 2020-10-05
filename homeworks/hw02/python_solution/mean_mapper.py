import sys
from utils import parse_airbnb_price

if __name__ == '__main__':
    count = 0
    prices_sum = 0
    for price in parse_airbnb_price(sys.stdin):
        count += 1
        prices_sum += price
    mean_price = prices_sum / count
    print(f'1\t{count}\t{mean_price}')
