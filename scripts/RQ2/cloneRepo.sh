#!/bin/bash

while read line 
do  
	cd "/home/laerte/Documents/SANER/Dataset"
	git clone "$line" 
done < $1
