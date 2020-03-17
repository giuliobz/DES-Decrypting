#!/bin/bash

startThreads=12
maxIteration=10
numberPss=50
dictionary="Dictionary_data"
method="Callable"
#tetsType="pss"
tetsType="thread"


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


if [ "$tetsType" = "pss" ]
then
    path=$PWD/DictResults/$method"_"$dictionary"_"$tetsType
fi

if [ "$tetsType" = "thread" ]
then
    path=$PWD/DictResults/$method"_"$dictionary"_"$tetsType"/"$method"Speedup_"$numberPss".csv"
fi

save_path=$PWD/DictResults/$method"_"$dictionary"_"$tetsType

source ~/anaconda3/etc/profile.d/conda.sh
conda activate minigrid
python plotting.py  $path $tetsType $save_path $method