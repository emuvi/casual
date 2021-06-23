import csv
import re

limiteRegex = re.compile(r"\b", re.I)
wordRegex = re.compile(r"\w", re.I)
digitoRegex = re.compile(r"\d", re.I)
doispontosRegex = re.compile(r"\:", re.I)
pontoRegex = re.compile(r"\.", re.I)
virgulaRegex = re.compile(r"\,", re.I)
edicaoRegex = re.compile(r"\b\d*\s*ed\b", re.I)
anoRegex = re.compile(r"\b\d{4}\b", re.I)
paginaRegex = re.compile(r"\b\d+\s*p.*", re.I)

oldFile = open('todos.csv', 'r', encoding='utf-8')
newFile = open('todos-new.csv', 'w', encoding='utf-8')
reader = csv.reader(oldFile)
writer = csv.writer(newFile)
rowIndex = 0

primeiro = True

for row in reader:
  if primeiro:
    primeiro = False
    writer.writerow(row)
    continue

  print("RI: " + str(rowIndex))
  rowIndex += 1

  referencia = row[13]
  autor = ""
  titulo = ""
  edicao = ""
  local = ""
  ano = ""
  editora = ""

  autorAndTitulo = referencia
  localAndResto = ""

  edicaoMatch = edicaoRegex.search(referencia)
  if edicaoMatch:
    edicaoStart = edicaoMatch.start()
    edicaoEnd = edicaoMatch.end()
    edicao = referencia[edicaoStart:edicaoEnd]
    limitesAnteriores = list(limiteRegex.finditer(referencia, 0, edicaoStart))
    limiteIndex = len(limitesAnteriores) -1
    while not digitoRegex.search(edicao) and limiteIndex >= 0:
      edicaoStart = limitesAnteriores[limiteIndex].start()
      edicao = referencia[edicaoStart:edicaoEnd]
      limiteIndex -= 1
    autorAndTitulo = referencia[0:edicaoStart]
    localAndResto = referencia[edicaoEnd:]
  else:
    doispontosMatches = list(doispontosRegex.finditer(referencia))
    if len(doispontosMatches) > 0:
      ultimoDoisPontos = doispontosMatches[-1]
      pontosAnteriores = list(pontoRegex.finditer(referencia, 0, ultimoDoisPontos.start()))
      divisao = len(referencia)
      if len(pontosAnteriores) > 0:
        ultimoPonto = pontosAnteriores[-1]
        divisao = ultimoPonto.end()
      else:
        espacosAnteriores = list(pontoRegex.finditer(referencia, 0, ultimoDoisPontos.start() - 3))
        if len(espacosAnteriores) > 0:
          ultimoEspaco = espacosAnteriores[-1]
          divisao = ultimoEspaco.end()
      autorAndTitulo = referencia[0:divisao]
      localAndResto = referencia[divisao:]

  print("P1: " + autorAndTitulo)
  print("P2: " + localAndResto)

  autorAndTitulo = autorAndTitulo.strip()
  if not autorAndTitulo.endswith("."):
    autorAndTitulo = autorAndTitulo + "."

  titulo = autorAndTitulo

  pontosMatches = list(pontoRegex.finditer(autorAndTitulo))
  if len(pontosMatches) >= 2:
    autor = autorAndTitulo[:pontosMatches[-2].end()].strip()
    titulo = autorAndTitulo[pontosMatches[-2].end():].strip()
  
  anoMatch = anoRegex.search(localAndResto)
  if (anoMatch):
    ano = localAndResto[anoMatch.start():anoMatch.end()]
    localAndResto = localAndResto[:anoMatch.start()] + localAndResto[anoMatch.end():]

  doispontosMatch = doispontosRegex.search(localAndResto)
  if doispontosMatch:
    local = localAndResto[:doispontosMatch.start()]
    localAndResto = localAndResto[doispontosMatch.end():]
    wordMatches = list(wordRegex.finditer(local))
    if len(wordMatches) >= 2:
      local = local[wordMatches[0].start():wordMatches[-1].end()]

  paginaMatch = paginaRegex.search(localAndResto)
  if paginaMatch:
    localAndResto = localAndResto[:paginaMatch.start()]

  virgulaMatch = virgulaRegex.search(localAndResto)
  if virgulaMatch:
    editora = localAndResto[:virgulaMatch.start()]
  else:
    editora = localAndResto

  wordMatches = list(wordRegex.finditer(editora))
  if len(wordMatches) >= 2:
    editora = editora[wordMatches[0].start():wordMatches[-1].end()]
      
  row[7] = autor
  row[8] = titulo
  row[9] = edicao
  row[10] = local
  row[11] = ano
  row[12] = editora
  writer.writerow(row)
  
