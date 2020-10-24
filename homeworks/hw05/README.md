В качестве датасета взят [Boston house prices dataset](https://scikit-learn.org/stable/datasets/index.html#boston-dataset)  
В тетрадке "Python dataset preparation and model validation.ipynb" осуществляется разбиение данных на train и test, а также сравнение результатов решения на Scala&Breeze и через sklearn.linear_model.LinearRegression  

Исходники проекта лежат в "src/main/scala/linreg"  
В "build.sbt" указаны зависимости. Помимо Breeze используется optparse для разбора аргументов командной строки.  

