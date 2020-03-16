#!/bin/bash


startThreads=12
dictionary="Dictionaries/Dictionary_data.txt"
maxIteration=10
numberPss=50
method="Callable"
tetsType="pss"
#tetsType="thread"

for i in {1..10}
do

  ans=$numberPss

  if [ $testType = "pss" ]
  then
    ans=$((numberPss * i))
  fi


  echo "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
  echo "Searching  $ans passwords  "
  echo "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
  echo ""

  java -jar /home/bazza/Scrivania/DES-Decrypting/out/artifacts/DES_Decrypting_jar/DES-Decrypting.jar $startThreads $dictionary $maxIteration $ans $method $tetsType

done



