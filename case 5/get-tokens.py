import os
import glob
import csv

body = 'D:\PacBody'
print('Body: ' + body)

logs = os.path.join(body, '(logs)')
print('Logs: ' + logs)


def __hasTokens(row):
    if len(row) == 4:
        return row[1].startswith('Feeding:') and row[2].startswith('Checker:') \
        and row[3] == 'Success!'
    else:
        return False


def __getTokens(row):
    name = row[1][row[1].rfind('/') +1:].strip()
    checker = row[2][row[2].find(':') +1:].strip()
    first_level = checker[0:3]
    second_level = checker[3:6]
    lookup = os.path.join(body, first_level, second_level, checker, 'org-*')
    founds = glob.glob(lookup)
    for found in founds:
        print(found)
    print(name)
    print(checker)
    print(first_level)
    print(second_level)


if __name__ == '__main__':
    csv_logs = glob.glob(os.path.join(logs, '*.csv'))
    for csv_log in csv_logs:
        with open(csv_log) as csv_file:
            reader = csv.reader(csv_file)
            for row in reader:
                if __hasTokens(row):
                    __getTokens(row)



