#!/usr/bin/env bash
# A script to fetch data from Github Archive

year=2013
month=07
day=07

# fetch all github action happened on 07.07.2013
# omit --day to fetch the whole month
python3 ./py/gharchive.py --year $year --month $month --day $day --directory $HOME/Downloads/data-gharchive/

# aggregate into a single file
aggregated=gharchive.json
cat $HOME/Downloads/data-gharchive/*.json >> $HOME/Downloads/data-gharchive/$aggregated

# delete stuff
rm $HOME/Downloads/data-gharchive/$year*.json
