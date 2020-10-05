#!/bin/bash

HW_DIR="/home/hadoop/a.kopnin/hw02"
SCRIPTS_DIR="$HW_DIR/python_solution"
DATASET_DIR="$HW_DIR/new-york-city-airbnb-open-data"
DATASET_PATH="$DATASET_DIR/AB_NYC_2019.csv"
RESULTS_PATH="$HW_DIR/results.txt"

HDFS_HW_DIR="/user/hadoop/a.kopnin/hw02"
HDFS_DATASET_DIR="$HDFS_HW_DIR/new-york-city-airbnb-open-data"
HDFS_DATASET_PATH="$HDFS_DATASET_DIR/AB_NYC_2019.csv"
HDFS_MEAN_OUTPUT_DIR="$HDFS_HW_DIR/output/mean"
HDFS_VAR_OUTPUT_DIR="$HDFS_HW_DIR/output/var"

nl=$'\n'
tab=$'\t'

# Результаты вычисления статистик через numpy
NUMPY_MEAN=152.7206871868289
NUMPY_VARIANCE=57672.84569843345

# Проверяем, существует ли директория в hdfs
hdfs dfs -test -d $HDFS_HW_DIR

# Если существует, то удалим вместе со всем содержимым
if [ $? -eq 0 ] # $? - статус последней выполненной команды. "hdfs dfs -test -d" возвращает 0, если директория существует
then 
    hdfs dfs -rm -r $HDFS_HW_DIR
fi

# Создаём необходимые директории в HDFS
hdfs dfs -mkdir -p $HDFS_DATASET_DIR

# Копируем датасет в HDFS
hdfs dfs -put $DATASET_PATH $HDFS_DATASET_DIR

# Запускаем MapReduce для вычисления среднего
hadoop jar /usr/lib/hadoop-mapreduce/hadoop-streaming.jar \
-files $SCRIPTS_DIR \
-mapper 'python3 python_solution/mean_mapper.py' \
-reducer 'python3 python_solution/mean_reducer.py' \
-input $HDFS_DATASET_PATH \
-output $HDFS_MEAN_OUTPUT_DIR \
-numReduceTasks 1

# Считываем результат работы в переменную MEAN
MEAN=`hdfs dfs -cat $HDFS_MEAN_OUTPUT_DIR/part-00000`

# Запускаем MapReduce для вычисления дисперсии
hadoop jar /usr/lib/hadoop-mapreduce/hadoop-streaming.jar \
-files $SCRIPTS_DIR \
-mapper 'python3 python_solution/variance_mapper.py' \
-reducer 'python3 python_solution/variance_reducer.py' \
-input $HDFS_DATASET_PATH \
-output $HDFS_VAR_OUTPUT_DIR \
-numReduceTasks 1

# Считываем результат работы в переменную VARIANCE
VARIANCE=`hdfs dfs -cat $HDFS_VAR_OUTPUT_DIR/part-00000`

REPORT="Results:${nl}Numpy mean${tab}${tab}=${tab}${NUMPY_MEAN}${tab}MapReduce mean${tab}${tab}=${tab}${MEAN}${nl}Numpy variance${tab}=${tab}${NUMPY_VARIANCE}${tab}MapReduce variance${tab}=${tab}${VARIANCE}"

echo "$REPORT"
echo "$REPORT" > $RESULTS_PATH
