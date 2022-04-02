@GrabConfig(systemClassLoader=true)
@Grab(group='org.postgresql', module='postgresql', version='42.3.3')
def url = System.getenv('LINK_INCORPEL')
println "Connecting to database..."
def link = java.sql.DriverManager.getConnection(url)
println "Sucessfully connected!"

import groovy.sql.Sql
def sql = new Sql(link)

def clean = {
    println "Cleaning some bugs..."
    sql.execute("DELETE FROM precos WHERE tabela = '' OR tabela IS NULL")
    println "Cleaned."
}

clean()

def dump = { name ->
    println "Dumping " + name + "..."
    new File(name).withWriter("utf-8") { writer ->
        sql.query('SELECT * FROM precos') { rst ->
            while (rst.next()) {
                def md = rst.getMetaData()
                for (col in 1..md.getColumnCount()) {
                    def val = (col > 1 ? "," : "") + rst.getString(col)
                    writer.write val
                }
                writer.write '\n'
            }
        }
    }
    println "Dumped " + name + "."
}

dump("antes.csv")

class Mutation {
    String grupo
    String subgrupo
    Float multiply
}

def mutate = {
    println "Starting the mutation..."

    def mutations = [
        new Mutation(grupo: '01', subgrupo: '051', multiply: 1.03),
        new Mutation(grupo: '02', subgrupo: '004', multiply: 1.03),
        new Mutation(grupo: '03', subgrupo: '005', multiply: 1.03),

        new Mutation(grupo: '01', subgrupo: '052', multiply: 1.06),
        new Mutation(grupo: '02', subgrupo: '001', multiply: 1.06),

        new Mutation(grupo: '01', subgrupo: '049', multiply: 1.07),
        new Mutation(grupo: '01', subgrupo: '063', multiply: 1.07),
        new Mutation(grupo: '02', subgrupo: '003', multiply: 1.07),
        new Mutation(grupo: '03', subgrupo: '004', multiply: 1.07),
        new Mutation(grupo: '03', subgrupo: '010', multiply: 1.07),
    ]

    for (mut in mutations) {
        println "Applying on grupo = " + mut.grupo +
            " e subgrupo = " + mut.subgrupo +
            " o multiplicador = " + mut.multiply + "..."
        for (tabela_base in ["NORINT", "SENINT"]) {
            println "Applying on tabela base = " + tabela_base
            sql.execute(
                """
                UPDATE precos SET valor = valor * ?
                WHERE tabela = ? AND produto IN (
                    SELECT codigo
                    FROM produtos
                    WHERE grupo = ?
                    AND subgrupo = ?
                )""",
                [mut.multiply, tabela_base, mut.grupo, mut.subgrupo])
            println "Applied over " + sql.updateCount + " registers!"
        }
    }

    println "Finished the mutation."
}

mutate()

enum By {
  MULT,
  ADDS,
}

class Relation {
    String destiny
    String origin
    By maked
    Float to
}

def relate = {
    println "Starting the relation..."

    def relations = [
        new Relation(destiny: "NORILH", origin: "NORINT", maked: By.MULT, to: 1.01),
        new Relation(destiny: "SENILH", origin: "SENINT", maked: By.MULT, to: 1.01),
        new Relation(destiny: "NORITA", origin: "NORILH", maked: By.ADDS, to: 0.01),
        new Relation(destiny: "SENITA", origin: "NORILH", maked: By.ADDS, to: 0.01),
        new Relation(destiny: "NORFX", origin: "NORINT", maked: By.MULT, to: 1.015),
        new Relation(destiny: "SENFX", origin: "SENINT", maked: By.MULT, to: 1.015),
        new Relation(destiny: "NOREXT", origin: "NORINT", maked: By.MULT, to: 1.02),
        new Relation(destiny: "SENEXT", origin: "SENINT", maked: By.MULT, to: 1.02),
        new Relation(destiny: "NORLAG", origin: "NORINT", maked: By.MULT, to: 1.03),
        new Relation(destiny: "SENLAG", origin: "SENINT", maked: By.MULT, to: 1.03),
        new Relation(destiny: "NORIA", origin: "NORINT", maked: By.MULT, to: 1.04),
        new Relation(destiny: "SENIA", origin: "SENINT", maked: By.MULT, to: 1.04),
        new Relation(destiny: "NORRS", origin: "NORINT", maked: By.MULT, to: 1.079),
        new Relation(destiny: "SENRS", origin: "SENINT", maked: By.MULT, to: 1.079),
        new Relation(destiny: "NORFEF", origin: "NORINT", maked: By.MULT, to: 1.08),
        new Relation(destiny: "SENFEF", origin: "SENINT", maked: By.MULT, to: 1.08),
        new Relation(destiny: "NORRJ", origin: "NORINT", maked: By.MULT, to: 1.10),
        new Relation(destiny: "SENRJ", origin: "SENINT", maked: By.MULT, to: 1.10),
    ]

    for (rel in relations) {
        println "Recreating " + rel.destiny + " as " + rel.origin + " " + rel.maked + " " + rel.to + "..."
        sql.execute('DELETE FROM precos WHERE tabela = ?', [rel.destiny])
        sql.query('SELECT produto, valor FROM precos WHERE tabela = ?', [rel.origin]) { rst ->
            while (rst.next()) {
                def produto = rst.getString(1)
                def valor = rst.getFloat(2)
                def newValor = rel.maked == By.MULT ? valor * rel.to : valor + rel.to
                sql.execute('INSERT INTO precos (tabela, produto, valor) VALUES (?, ?, ?)',
                    [rel.destiny, produto, newValor])
            }
        }
    }

    println "Finished the relation."
}

relate()

dump("depois.csv")

sql.close()
println "Sucessfully Closed!"
