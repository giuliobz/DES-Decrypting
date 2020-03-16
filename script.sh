#!/bin/bash

startThreads=12
maxIteration=10
numberPss=50
dictionary="Dictionary_data"
method="Callable"
tetsType="pss"
#tetsType="thread"


#for i in {1..10}
#do


#  if [ "$tetsType" = "pss" ]
#  then
#    ans=$((numberPss * i))
#  fi

#  if [ "$tetsType" = "thread" ]
#  then
#    ans=$numberPss
#  fi


#  echo "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
#  echo "Searching  $ans passwords  "
#  echo "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
#  echo ""

#  java -jar /home/bazza/Scrivania/DES-Decrypting/out/artifacts/DES_Decrypting_jar/DES-Decrypting.jar $startThreads "Dictionaries/$dictionary.txt" $maxIteration $ans $method $tetsType

#done


path=$PWD/DictResults/$method"_"$dictionary"_"$tetsType

source ~/anaconda3/etc/profile.d/conda.sh
conda activate minigrid
python plotting.py  $path $tetsType