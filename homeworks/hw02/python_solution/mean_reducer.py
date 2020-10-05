import sys
from utils import combine_mean

if __name__ == '__main__':
    current_size, current_mean = 0, 0.0
    for line in sys.stdin:
        key, split_size, split_mean = line.split('\t', 2)
        split_size = int(split_size)
        split_mean = float(split_mean)
        current_mean = combine_mean(current_mean, split_mean, current_size, split_size)
        current_size += split_size
    print(current_mean)
