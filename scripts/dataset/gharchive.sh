# A script to fetch data from Github Archive
PREFIX=$HOME/sparser-data/data-gharchive
aggregated=mm2gharchive.json

# initial clean-up
if [ -e "$PREFIX"/$aggregated ]; then
  rm "$PREFIX"/$aggregated
fi
if [ -e "$PREFIX"/temp ]; then
  rm -rf "$PREFIX"/temp
fi

year=2019
month=05
day=28
# omit --day to fetch the whole month
python3 ./py/gharchive.py --year $year --month $month --day $day --directory "$PREFIX"/temp

# aggregate into a single file
sed -r "" "$HOME"/sparser-data/data-gharchive/temp/*.json >> "$PREFIX"/$aggregated

# delete stuff
rm -rf "$PREFIX"/temp
