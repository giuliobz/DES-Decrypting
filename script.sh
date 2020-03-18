#!/bin/bash

maxIteration=10
numberPss=100
dictionary="Dictionary_data"
method="Callable"
tetsType="pss"
#tetsType="thread"

if [ "$tetsType" = "pss" ]
then
    end=10
    startThreads=12
    path=$PWD/DictResults/$method"_"$dictionary"_"$tetsType
fi

if [ "$tetsType" = "thread" ]
then
    end=1
    startThreads=2
    path=$PWD/DictResults/$method"_"$dictionary"_"$tetsType"/"$method"Speedup_"$numberPss".csv"
fi


for (( i = 1 ; i <= $end ; i += 1 )) ;
do


  if [ "$tetsType" = "pss" ]
  then
    ans=$((numberPss * i))
  fi

  if [ "$tetsType" = "thread" ]
  then
    ans=$numberPss
  fi


  echo "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
  echo "Searching  $ans passwords  "
  echo "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
  echo ""

  java -jar /home/bazza/Scrivania/DES-Decrypting/out/artifacts/DES_Decrypting_jar/DES-Decrypting.jar $startThreads "Dictionaries/$dictionary.txt" $maxIteration $ans $method $tetsType

done


save_path=$PWD/DictResults/$method"_"$dictionary"_"$tetsType

source ~/anaconda3/etc/profile.d/conda.sh
conda activate minigrid
python plotting.py  $path $tetsType $save_path $method
