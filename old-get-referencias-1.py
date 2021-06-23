import sys
import glob


abertura = ["bibliografia básica", "bibliografia complementar"]
fechamento = ["unidade curricular", "componente curricular",
              "estágio curricular", "estágio extracurricular"]

comecos_a_descartar = ["instituto federal de santa catarina",
                       "rua: 14 de julho, 150", "fone: (48) 3877-9000"]


def verifica_se_contem(linha, casos):
    for caso in casos:
        if caso in linha:
            return True
    return False


def verifica_se_comeca(linha, casos):
    for caso in casos:
        if linha.startswith(caso):
            return True
    return False


def getAutorPrimeiroNome(linha):
    posicoes = [linha.find("."), linha.find(";"), linha.find(","),
                linha.find("-"), linha.find(" ")]
    menor = -1
    for posicao in posicoes:
        if posicao > -1 and (menor == -1 or posicao < menor):
            menor = posicao
    return "" if menor == -1 else linha[0:menor]


def ehSomenteLetrasMaiusculas(nome):
    for letra in nome:
        if not letra.isupper():
            return False
    return True


def iniciaComAutor(linha):
    primeiroNome = getAutorPrimeiroNome(linha)
    if len(primeiroNome) <= 0:
        return False
    else:
        return ehSomenteLetrasMaiusculas(primeiroNome)


def makePasso2(fileName):
    print("Fazendo Passo 2 em: " + fileName)
    reader = open(fileName, 'r', encoding='utf-8')
    linhas = reader.readlines()
    maked = []
    aberto = False
    for linha in linhas:
        linha = linha.strip()
        while "  " in linha:
            linha = linha.replace("  ", " ")
        case = linha.lower()
        if ((not aberto) and (verifica_se_contem(case, abertura))):
            aberto = True
        if ((aberto) and (verifica_se_contem(case, fechamento))):
            aberto = False
        if aberto:
            maked.append(linha + "\n")
    reader.close()
    destiny = fileName[0:-5] + "2.txt"
    writer = open(destiny, 'w', encoding='utf-8')
    writer.writelines(maked)
    writer.close()
    makePasso3(destiny)


def makePasso3(fileName):
    print("Fazendo Passo 3 em: " + fileName)
    reader = open(fileName, 'r', encoding='utf-8')
    linhas = reader.readlines()
    maked = []
    for linha in linhas:
        if not linha.strip().isdigit():
            maked.append(linha)
    reader.close()
    destiny = fileName[0:-5] + "3.txt"
    writer = open(destiny, 'w', encoding='utf-8')
    writer.writelines(maked)
    writer.close()
    makePasso4(destiny)


def makePasso4(fileName):
    print("Fazendo Passo 4 em: " + fileName)
    reader = open(fileName, 'r', encoding='utf-8')
    linhas = reader.readlines()
    maked = []
    for linha in linhas:
        case = linha.strip().lower()
        if len(case) > 0 and not verifica_se_comeca(case, comecos_a_descartar):
            maked.append(linha)
    reader.close()
    destiny = fileName[0:-5] + "4.txt"
    writer = open(destiny, 'w', encoding='utf-8')
    writer.writelines(maked)
    writer.close()
    makePasso5(destiny)


def makePasso5(fileName):
    print("Fazendo Passo 5 em: " + fileName)
    reader = open(fileName, 'r', encoding='utf-8')
    linhas = reader.readlines()
    destiny = fileName[0:-5] + "5.csv"
    writer = open(destiny, 'w', encoding='utf-8')
    writer.write('Bibliografia;Referência')
    basica = True
    for linha in linhas:
        linha = linha.strip()
        case = linha.lower()
        if "bibliografia básica" in case:
            basica = True
        elif "bibliografia complementar" in case:
            basica = False
        elif iniciaComAutor(linha):
            writer.write("\n")
            writer.write('Básica;' if basica else 'Complementar;')
            writer.write(linha.replace(";", ".,."))
        else:
            writer.write(linha.replace(";", ".,."))
    reader.close()
    writer.close()


print("Pegar as Bibliografias dos PPC's")
val = input("Deseja continuar? (s/N): ")
val = val.strip().lower()
if (val != "s"):
    sys.exit(-1)

print("Começando a pegar as bibliografias...")

sources = []
for file in glob.glob("*-1.txt"):
    sources.append(file)

for source in sources:
    makePasso2(source)
