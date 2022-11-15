# A script to fetch data from NYC Taxi and Limousine Commission
PREFIX=$HOME/sparser-data/data-tlc
json=tlc.json

# initial clean-up
if [ -e "$PREFIX"/$json ]; then
  rm "$PREFIX"/$json
fi
if [ -e "$PREFIX"/temp ]; then
  rm -rf "$PREFIX"/temp
fi

# options:
#   yellow - Yellow Taxi Trips
#   green - Green Taxi Trips
#   fhv - For-Hire Vehicle Trips
#   fhvhv - High Volume For-Hire Vehicle Trips
type=yellow
month=2
# options:
#   2009-2022
year=2019
python3 ./py/tlc.py --trip $type --year $year --month $month --directory "$PREFIX"/temp

# parquet to json
parquet cat "$PREFIX"/temp/$type-$month-$year.parquet >> "$PREFIX"/$json

# delete the parquet file
rm -rf $PREFIX/temp