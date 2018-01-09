TITLE MongoDB Import CSV
SET "MONGO_HOME=C:\Program Files\MongoDB\Server\3.6"
SET dataset=D:\machine_learning\datasets\iris\iris_with_head.csv
SET collection=iris
SET db=datasets

"%MONGO_HOME%\bin\mongoimport.exe" -d %db% -c %collection% --type CSV --file %dataset% --headerline
PAUSE
