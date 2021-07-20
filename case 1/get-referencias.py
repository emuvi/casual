import glob
import re
from enum import Enum
from collections import namedtuple
from tkinter import *

abertura_geral = [
    re.compile(r"\s+componentes\s+curriculares:?\s+", re.I),
    re.compile(r"^", re.I)
]

abertura_unidade = [
    re.compile(r"\s+componente\s+curricular:?\s+", re.I),
    re.compile(r"\s+unidade\s+curricular:?\s+", re.I)
]

abertura_referencia_basica = [
    re.compile(r"\s+refer[êe]ncias?\s+b[áa]sicas?:?\s+", re.I),
    re.compile(r"\s+bibliografias?\s+b[áa]sicas?:?\s+", re.I),
]

abertura_referencia_complementar = [
    re.compile(r"\s+bibliografias?\s+complementare?s?:?\s+", re.I),
    re.compile(r"\s+complementare?s?:?\s+", re.I),
]

fechamento_geral = [
    re.compile(r"\s+est[áa]gio\s+curricular:?\s+", re.I),
    re.compile(r"\s+est[áa]gio\s+curricular\s+supervisionado:?\s+", re.I),
    re.compile(
        r"\s+parecer\s+da\s+coordena[çc][ãa]o\s+pedag[óo]gica:?\s+", re.I),
    re.compile(r"$", re.I),
]


class MatchType(Enum):
    ABERTURA_GERAL = 0
    ABERTURA_UNIDADE = 1
    ABERTURA_REFERENCIA_BASICA = 2
    ABERTURA_REFERENCIA_COMPLEMENTAR = 3
    FECHAMENTO_REFERENCIA_COMPLEMENTAR = 4
    FECHAMENTO_REFERENCIA_BASICA = 5
    FECHAMENTO_UNIDADE = 6
    FECHAMENTO_GERAL = 7


def getMatches(inText, fromPatterns):
    result = []
    for pattern in fromPatterns:
        for match in pattern.finditer(inText):
            inserted = False
            for idx, val in enumerate(result):
                if match.start() <= val.start():
                    result.insert(idx, match)
                    inserted = True
                    break
            if not inserted:
                result.append(match)
    return result


def getLine(inText, ofMatche):
    begin = inText.rfind("\n", 0, ofMatche.start())
    end = inText.find("\n", ofMatche.end(), len(inText))
    begin = begin + 1
    if (end < 0):
        end = len(inText)
    return inText[begin:end]


def printMatches(inText, withName, theMatches):
    print("------------ : " + withName + " : ------------")
    for match in theMatches:
        linha = getLine(inText, match)
        print("[" + str(match.start()) + "] = '" + linha + "'")


def insertMatches(inList, theMatches, ofType):
    for match in theMatches:
        inserted = False
        for idx, val in enumerate(inList):
            if (match.start() <= val[1].start()):
                inList.insert(idx, (ofType, match))
                inserted = True
                break
        if not inserted:
            inList.append((ofType, match))


def getNumberOfLines(ofDocument, untilPosition):
    result = 1
    textUntil = ofDocument[0:untilPosition]
    breaksFound = re.compile(r"\n").finditer(textUntil)
    for _ in breaksFound:
        result = result + 1
    return result


def logAllMatches(inList, ofDocument, ofFileName):
    destiny = ofFileName[0:-8] + "-ref.log"
    writer = open(destiny, 'w', encoding='utf-8')
    for type_and_match in inList:
        match_type = type_and_match[0]
        match = type_and_match[1]
        line = getLine(ofDocument, match)
        lineNumber = getNumberOfLines(ofDocument, match.start())
        writer.write("[" + str(lineNumber) +
                     "] {" + match_type.name +
                     "} = '" + line + "'\n")
        writer.write(
            "Match = '" + ofDocument[match.start():match.end()] + "'\n\n")
    writer.close()


AskFaseInicia = "^"
AskFaseTermina = "$"
AskFaseRepetir = False
AskUnidadeInicia = "^"
AskUnidadeTermina = "$"
AskUnidadeRepetir = False


def askUsuario(tipo, withCorpo):
    global AskFaseInicia
    global AskFaseTermina
    global AskFaseRepetir
    global AskUnidadeInicia
    global AskUnidadeTermina
    global AskUnidadeRepetir
    iniciaInitial = "^"
    terminaInitial = "$"
    if tipo == "Fase":
        iniciaInitial = AskFaseInicia
        terminaInitial = AskFaseTermina
        if AskFaseRepetir :
            return (iniciaInitial, terminaInitial)
    elif tipo == "Unidade":
        iniciaInitial = AskUnidadeInicia
        terminaInitial = AskUnidadeTermina
        if AskUnidadeRepetir :
            return (iniciaInitial, terminaInitial)
    else:
        raise Exception("Você só pode perguntar ao usuário sobre a Fase e a Unidade.")
    root = Tk()
    iniciaVar = StringVar(root, value=iniciaInitial)
    terminaVar = StringVar(root, value=terminaInitial)
    titleLabel = Label(root, text="Encontrando a " + tipo + " de " + GettingReferenciasOf)
    titleLabel.grid(row=0, column= 0)
    corpoText = Text(root)
    corpoText.insert(INSERT, withCorpo)
    corpoScroll = Scrollbar(root, command=corpoText.yview)
    corpoText.configure(yscrollcommand=corpoScroll.set)
    corpoText.grid(row=1, column= 0)
    corpoScroll.grid(row=1, column=1, sticky="nsew")
    bottomFrame = Frame(root)
    bottomFrame.grid(row=2, column= 0)
    iniciaLabel = Label(bottomFrame, text="Inicia em:")
    iniciaLabel.grid(row=0, column=0)
    iniciaEntry = Entry(bottomFrame, textvariable=iniciaVar)
    iniciaEntry.grid(row=1, column=0, padx=3, pady=3)
    terminaLabel = Label(bottomFrame, text="Termina em:")
    terminaLabel.grid(row=0, column=1)
    terminaEntry = Entry(bottomFrame, textvariable=terminaVar)
    terminaEntry.grid(row=1, column=1, padx=3, pady=3)
    confirmarButton = Button(bottomFrame, text="Confirmar", command=root.destroy)
    confirmarButton.grid(row=0, column=2, padx=3, pady=3)
    def repetirAction():
        global AskFaseRepetir
        global AskUnidadeRepetir
        if tipo == "Fase":
            AskFaseRepetir = True
        elif tipo == "Unidade":
            AskUnidadeRepetir = True
        root.destroy()
    repetirEmDianteButton = Button(bottomFrame, text="Repetir em Diante", command=repetirAction)
    repetirEmDianteButton.grid(row=1, column=2, padx=3, pady=3)
    root.geometry("+{}+{}".format(45, 45))
    root.mainloop()
    if tipo == "Fase":
        AskFaseInicia = iniciaVar.get()
        AskFaseTermina = terminaVar.get()
    elif tipo == "Unidade":
        AskUnidadeInicia = iniciaVar.get()
        AskUnidadeTermina = terminaVar.get()
    return (iniciaVar.get(), terminaVar.get())


def findFase(ofDocument, withMatches, andReferenceIndex):
    
    return "FASE NÃO ENCONTRADA"


def findUnidade(ofDocument, withMatches, andReferenceIndex):
    for idx in range(andReferenceIndex -1,0,-1):
        type_and_match = withMatches[idx]
        match_type = type_and_match[0]
        matched = type_and_match[1]
        if match_type == MatchType.ABERTURA_UNIDADE:
            proximo = withMatches[idx +1]
            proximo_matched = proximo[1]
            unidadeCorpo = ofDocument[matched.end():proximo_matched.start()]
            inicia_termina = askUsuario("Unidade", unidadeCorpo)
            inicia_com = inicia_termina[0]
            termina_com = inicia_termina[1]
            inicia_match = re.search(inicia_com, unidadeCorpo, re.I)
            termina_match = re.search(termina_com, unidadeCorpo, re.I)
            if inicia_match and termina_match:
                if inicia_match.end() < termina_match.start():
                    result = unidadeCorpo[inicia_match.end():termina_match.start()]
                    result = result.replace("\n", "")
                    result = result.replace("\r", "")
                    result = result.strip()
                    if result != "":
                        return result
    return "UNIDADE NÃO ENCONTRADA"


Marked = namedtuple("Marked", "fase unidade tipo referencia")


def markReferencias(ofFileName, ofDocument, withMatches):
    result = []
    destiny = ofFileName[0:-8] + "-log.txt"
    writer = open(destiny, 'w', encoding='utf-8')
    ja_abriu_geral = False
    ja_abriu_unidade = False
    ja_abriu_referencia = False
    geral_esta_aberto = False
    unidade_esta_aberta = False
    referencias_esta_aberta = False
    for idx, type_and_match in enumerate(withMatches):
        match_type = type_and_match[0]
        matched = type_and_match[1]
        match_str = ofDocument[matched.start():matched.end()]
        lineNumber = getNumberOfLines(ofDocument, matched.start())
        if match_type == MatchType.ABERTURA_GERAL:
            writer.write(
                "[" + str(lineNumber) + "] Abertura Geral. Match: '" + match_str + "'\n")
            if ja_abriu_geral and ja_abriu_unidade and ja_abriu_referencia:
                raise Exception("Provavelmente tem mais de um PPC nesse PDF.")
            geral_esta_aberto = True
            unidade_esta_aberta = False
            referencias_esta_aberta = False
            ja_abriu_geral = True
        elif match_type == MatchType.ABERTURA_UNIDADE:
            if not geral_esta_aberto:
                writer.write(
                    "[" + str(lineNumber) + "] Descartada Abertura de Unidade. Geral NÃO está aberto. Match: '" + match_str + "'\n")
                continue
            writer.write(
                "[" + str(lineNumber) + "] Abertura de Unidade. Match: '" + match_str + "'\n")
            unidade_esta_aberta = True
            referencias_esta_aberta = False
            ja_abriu_unidade = True
        elif match_type == MatchType.ABERTURA_REFERENCIA_BASICA:
            if not geral_esta_aberto:
                writer.write(
                    "[" + str(lineNumber) + "] Descartada Abertura de Unidade. Geral NÃO está aberto. Match: '" + match_str + "'\n")
                continue
            if not unidade_esta_aberta:
                writer.write(
                    "[" + str(lineNumber) + "] Descartada Abertura de Referência Básica. Unidade NÃO está aberta. Match: '" + match_str + "'\n")
                continue
            writer.write(
                "[" + str(lineNumber) + "] Abertura de Referência Básica. Match: '" + match_str + "'\n")
            proximo = withMatches[idx + 1]
            proximo_matched = proximo[1]
            encontrado = ofDocument[matched.end():proximo_matched.start()].strip()
            if encontrado:
                writer.write("Referência Básica:---------\n\n" +
                             encontrado + "\n\n-------------------------" + "\n")
                foundFase = findFase(ofDocument, withMatches, idx)
                foundUnidade = findUnidade(ofDocument, withMatches, idx)
                result.append(Marked(fase=foundFase, unidade=foundUnidade,
                                     tipo="Básica", referencia=encontrado))
            referencias_esta_aberta = True
        elif match_type == MatchType.ABERTURA_REFERENCIA_COMPLEMENTAR:
            if not geral_esta_aberto:
                writer.write(
                    "[" + str(lineNumber) + "] Descartada Abertura de Unidade. Geral NÃO está aberto. Match: '" + match_str + "'\n")
                continue
            if not unidade_esta_aberta or not referencias_esta_aberta:
                writer.write(
                    "[" + str(lineNumber) + "] Descartada Abertura de Referência Complementar. Unidade NÃO está aberta. Match: '" + match_str + "'\n")
                continue
            if idx == 0:
                writer.write(
                    "[" + str(lineNumber) + "] Descartada Abertura de Referência Complementar. Complementar NÃO pode ser a primeira. Match: '" + match_str + "'\n")
                continue
            anterior = withMatches[idx - 1]
            anterior_type = anterior[0]
            if not (anterior_type == MatchType.ABERTURA_REFERENCIA_BASICA or anterior_type == MatchType.ABERTURA_REFERENCIA_COMPLEMENTAR):
                writer.write(
                    "[" + str(lineNumber) + "] Descartada Abertura de Referência Complementar. Anterior NÃO é referência. Match: '" + match_str + "'\n")
                continue
            writer.write(
                "[" + str(lineNumber) + "] Abertura de Referência Complementar. Match: '" + match_str + "'\n")
            proximo = withMatches[idx + 1]
            proximo_matched = proximo[1]
            encontrado = ofDocument[matched.end():proximo_matched.start()].strip()
            if encontrado:
                writer.write("Referência Complementar:---------\n\n" +
                             encontrado + "\n\n-------------------------" + "\n")
                foundFase = findFase(ofDocument, withMatches, idx)
                foundUnidade = findUnidade(ofDocument, withMatches, idx)
                result.append(Marked(fase=foundFase, unidade=foundUnidade,
                                     tipo="Complementar", referencia=encontrado))
        elif match_type == MatchType.FECHAMENTO_REFERENCIA_COMPLEMENTAR:
            writer.write(
                "[" + str(lineNumber) + "] Fechamento de Referência Complementar. Match: '" + match_str + "'\n")
            referencias_esta_aberta = False
        elif match_type == MatchType.FECHAMENTO_REFERENCIA_BASICA:
            writer.write(
                "[" + str(lineNumber) + "] Fechamento de Referência Básica. Match: '" + match_str + "'\n")
        elif match_type == MatchType.FECHAMENTO_UNIDADE:
            writer.write(
                "[" + str(lineNumber) + "] Fechamento de Unidade. Match: '" + match_str + "'\n")
            unidade_esta_aberta = False
            referencias_esta_aberta = False
        elif match_type == MatchType.FECHAMENTO_GERAL:
            writer.write(
                "[" + str(lineNumber) + "] Fechamento Geral. Match: '" + match_str + "'\n")
            geral_esta_aberto = False
            unidade_esta_aberta = False
            referencias_esta_aberta = False
    return result


GettingReferenciasOf = ""


def getReferencias(ofFileName):
    global GettingReferenciasOf
    GettingReferenciasOf = ofFileName
    print("Pegando Referências de " + ofFileName)
    reader = open(ofFileName, 'r', encoding='utf-8')
    document = reader.read()
    reader.close()

    document = re.sub(" +", " ", document)

    print("Procurando...")
    all_matches = []
    matches_abertura_geral = getMatches(document, abertura_geral)
    insertMatches(all_matches, matches_abertura_geral,
                  MatchType.ABERTURA_GERAL)
    matches_abertura_unidade = getMatches(document, abertura_unidade)
    insertMatches(all_matches, matches_abertura_unidade,
                  MatchType.ABERTURA_UNIDADE)
    matches_abertura_referencia_basica = getMatches(
        document, abertura_referencia_basica)
    insertMatches(all_matches, matches_abertura_referencia_basica,
                  MatchType.ABERTURA_REFERENCIA_BASICA)
    matches_abertura_referencia_complementar = getMatches(
        document, abertura_referencia_complementar)
    insertMatches(all_matches, matches_abertura_referencia_complementar,
                  MatchType.ABERTURA_REFERENCIA_COMPLEMENTAR)
    matches_fechamento_geral = getMatches(document, fechamento_geral)
    insertMatches(all_matches, matches_fechamento_geral,
                  MatchType.FECHAMENTO_GERAL)

    print("Processando...")
    markeds = markReferencias(ofFileName, document, all_matches)
    markFile = ofFileName[0:-8] + "-mrk.csv"
    markWriter = open(markFile, 'w', encoding='utf-8')
    for marked in markeds:
        markWriter.write("\"")
        markWriter.write(marked.fase.replace("\r", "").replace("\n", "").replace("\"", "\"\""))
        markWriter.write("\",\"")
        markWriter.write(marked.unidade.replace("\r", "").replace("\n", "").replace("\"", "\"\""))
        markWriter.write("\",\"")
        markWriter.write(marked.tipo.replace("\r", "").replace("\n", "").replace("\"", "\"\""))
        markWriter.write("\",\"")
        markWriter.write(marked.referencia.replace("\r", "").replace("\n", "").replace("\"", "\"\""))
        markWriter.write("\"\n")
    markWriter.close()

    destiny = ofFileName[0:-8] + "-ref.csv"
    writer = open(destiny, 'w', encoding='utf-8')
    writer.write("Fase,Unidade,Bibliografia,Quantidade,Referência\n")
    writer.close()


sources = []
for file in glob.glob("data/*-ppc.txt"):
    sources.append(file)

for source in sources:
    AskFaseInicia = "^"
    AskFaseTermina = "$"
    AskFaseRepetir = False
    AskUnidadeInicia = "^"
    AskUnidadeTermina = "$"
    AskUnidadeRepetir = False
    getReferencias(source)
