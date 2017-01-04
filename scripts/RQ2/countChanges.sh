#!/bin/bash

while read line 
do 
   echo $line
   cd "/home/laerte/Documents/SANER/Dataset/"$line
   uri=`git config --get remote.origin.url`
  
   tags=(`git tag`)
  
   count=0
   for tag in ${tags[@]};
   do
      dates[$count]=`git log -1 --format=%ai $tag | cut -c1-10`
      count=$((count+1))
   done

    cd "/home/laerte/Documents/SANER/RQ2/Experimentos"
	echo ${dates[@]}
	dateCount=0
	for date in ${dates[@]};
   	do
		if [ ! -d "$line""_older" ]; then
			git clone --branch ${tags[$((dateCount))]} $uri "$line""_older"
		fi

		#echo ${dates[$((dateCount))]}

		dateCount=$((dateCount+1))
		git clone --branch ${tags[$((dateCount))]} $uri "$line""_newer"
		
		if [ -d "$line""_newer" ]; then
			java -Xmx20g -Xms18g -jar APIChangeCounter.jar "$line" ${dates[$((dateCount-1))]} ${dates[$((dateCount))]} "$line""_older" "$line""_newer"  >> outSemPlatform.csv
			rm -rf "$line""_older"
			mv "$line""_newer" "$line""_older"
		fi
		
	done
	rm -rf "$line""_older"

		#git clone --branch ${tags[$((count-1))]} $uri "$line""_older"
    
    #echo "Project;TypeBC;EnumBC;FieldBC;MethodBC;AllBC;TypeNBC;EnumNBC;FieldNBC;MethodNBC;AllNBC;TypeAdd;TypeRemoval;TypeModif;TypeDepOp;EnumAdd;
	#		EnumRemoval;EnumModif;EnumDepOp;FieldAdd;FieldRemoval;FieldModif;FieldDepOp;MethodAdd;MethodRemoval;MethodModif;MethodDepOp" >> out.csv
    #java -Xmx20g -Xms18g -jar APIChangeCounter.jar "$line""_older" "/home/laerte/Documents/SANER/Dataset/"$line  >> out.csv

		#rm -r -f "$line""_v1"
		#rm -r -f "$line""_v2"

   # daysAdd=$(( ( $date -ud ${dates[$((count-1))]} +'%s') - $(date -ud ${dates[0]} +'%s') )/60/60/24/2 ))
   
   # dateMiddle=`date --date="${dates[0]} +$daysAdd days" +%Y-%m-%d`

   # #echo $dateMiddle	   
   # count=0
   # for date in ${dates[@]};
   # do
   #   if [ $(date -d $date +%s) -gt $(date -d $dateMiddle +%s) ];
   #   then
   #      cd "/home/gleison/CloneMiddleTag"
   #      git clone --branch ${tags[$((count-1))]} $uri
   #      #echo "versao meio: ${tags[$((count-1))]}"
   #      break
   #   fi 
   # count=$((count+1))
   # done


   #$HOME/CloneProjects/cloneMiddleTag.sh
   #cd "/home/gleison/CloneProjects"
done < $1
