import argparse
import datetime
import gzip
import logging

import requests
import sys
import os

# type, year, month
TLCDATA = 'https://d37ci6vzurychx.cloudfront.net/trip-data/{}_tripdata_{}-{}.parquet'
# GHARCHIVE = 'http://data.githubarchive.org/{0}.json.gz'


def format_month(month):
    if len(str(month)) == 1:
        month = '0'+str(month)
    return month


# def download_day(year, month, day, directory):
#     date_start = datetime.datetime(year=year, month=month, day=day, hour=0)
#     date_end = datetime.datetime(year=year, month=month, day=day, hour=23)
#
#     while date_start <= date_end:
#         month, day = format_date(date_start.month, date_start.day)
#         formatted_date = DATE.format(date_start.year, month, day, date_start.hour)
#         archive_path = os.path.join(directory, formatted_date) + '.gz'
#
#         try:
#
#             if not os.path.exists(archive_path):
#                 response = requests.get(GHARCHIVE.format(formatted_date), stream=True)
#                 with open(archive_path, 'wb') as download:
#                     download.write(response.raw.read())
#
#             with gzip.open(archive_path, 'rb') as gzipfile, \
#                     open(os.path.join(directory, formatted_date) + '.json', 'wb') as jsonfile:
#                 jsonfile.write(gzipfile.read())
#
#             os.remove(archive_path)
#
#         except Exception as e:
#             logging.error("An error has occurred while downloading: %s", e)
#             continue
#
#         date_start += datetime.timedelta(hours=1)
#

def download_month(year, month, directory, type):
    file_path = os.path.join(directory, type+'-'+str(month)+'-'+str(year)) + '.parquet'

    try:
        if not os.path.exists(file_path):
            response = requests.get(TLCDATA.format(type, year, format_month(month)), stream=True)
            with open(file_path, 'wb') as download:
                download.write(response.raw.read())

        # with gzip.open(archive_path, 'rb') as gzipfile, \
        #         open(os.path.join(directory, formatted_date) + '.json', 'wb') as jsonfile:
        #     jsonfile.write(gzipfile.read())

    # os.remove(archive_path)
    except Exception as e:
        logging.error("An error has occurred while downloading: %s", e)



def parse(*args):
    parser = argparse.ArgumentParser(description="Data fetcher from Gharchive")

    parser.add_argument('--year', type=int, required=True)
    parser.add_argument('--month', type=int, required=True)
    parser.add_argument('--trip', type=str, required=True)
    parser.add_argument('--directory', help='Place to put data')

    return parser.parse_args(*args)


def main():
    logging.getLogger().setLevel(logging.DEBUG)

    args = parse(sys.argv[1:])

    year = args.year
    month = args.month
    type_ = args.trip
    directory = args.directory

    logging.debug("Download started")

    if directory is not None and not os.path.exists(directory):
        os.makedirs(directory)

    if year and month:
        download_month(year, month, directory, type_)


if __name__ == '__main__':
    main()

