#!/bin/bash


maxThreads=12
dictionary="Dictionaries/Dictionary_data.txt"
maxIteration=1
numberPss=5
method="Callable"

for i in {1..10}
do
  ans=$((numberPss * i))

  echo "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
  echo "Searching  $ans passwords  "
  echo "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
  echo ""

  java -jar /home/bazza/Scrivania/DES-Decrypting/out/artifacts/DES_Decrypting_jar/DES-Decrypting.jar $maxThreads $dictionary $maxIteration $ans $method

done



