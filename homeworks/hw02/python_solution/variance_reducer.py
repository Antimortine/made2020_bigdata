import sys
from utils import combine_mean, combine_var

if __name__ == '__main__':
    current_size, current_mean, current_var = 0, 0.0, 0.0
    for line in sys.stdin:
        key, split_size, split_mean, split_var = line.split('\t', 3)
        split_size = int(split_size)
        split_var = float(split_var)
        split_mean = float(split_mean)
        current_var = combine_var(current_var, split_var, current_mean, split_mean, current_size, split_size)
        current_mean = combine_mean(current_mean, split_mean, current_size, split_size)
        current_size += split_size
    print(current_var)
