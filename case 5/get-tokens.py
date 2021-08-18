import os
import glob
import csv
import sys

body = 'D:\\PacBody'
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
    checker = row[2][row[2].find(':') + 1:].strip()
    name_index = row[1].rfind('\\')
    if name_index == -1:
        name_index = row[1].rfind('/')
    file_name = row[1][name_index + 1:].strip()
    first_level = checker[0:3]
    second_level = checker[3:6]
    lookup = os.path.join(body, first_level, second_level, checker, 'org-*')
    founds = glob.glob(lookup)
    for found in founds:
        token_name = found[found.rfind('\\')+1:].strip()
        old_token = token_name[:token_name.find('.')] + ".tkn"
        token_name = token_name[:token_name.find('.')] + ".tkmn"
        destiny_name = body
        destiny_name += '\\' + first_level
        destiny_name += '\\' + second_level
        destiny_name += '\\' + checker
        if not os.path.exists(destiny_name):
            print("ERROR: this folder should exist: " + destiny_name)
            print(row)
            sys.exit(1)
        old_token = destiny_name + '\\' + old_token
        try:
            os.remove(old_token)
        except:
            print("There was no old token to delete.")
        destiny_name += '\\' + token_name
        print("Writing: " + destiny_name)
        f = open(destiny_name, "w")
        f.write("file_name = " + file_name + "\n")
        f.close()
        break


if __name__ == '__main__':
    csv_logs = glob.glob(os.path.join(logs, '*.csv'))
    for csv_log in csv_logs:
        with open(csv_log) as csv_file:
            reader = csv.reader(csv_file)
            for row in reader:
                if __hasTokens(row):
                    __getTokens(row)
