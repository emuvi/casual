import glob
import csv

def putUnidades(originName):
    print("Getting Unidades for: " + originName)
    sourceName = originName[0:-8] + "-mrk.csv"
    sourceReader = open(sourceName, 'r', encoding='utf-8')
    sourceCSV = csv.reader(sourceReader)
    # for sourceRow in sourceCSV:
    #     print(sourceRow)
    
    originReader = open(originName, 'r', encoding='utf-8')
    originCSV = csv.reader(originReader)
    for originRow in originCSV:
        print(originRow)

    destinyFile = originName[0:-8] + "-qtu.csv"
    destinyReader = open(destinyFile, 'r', encoding='utf-8')
    

sources = []
for file in glob.glob("data/*-qtd.csv"):
    sources.append(file)

for source in sources:
    putUnidades(source)