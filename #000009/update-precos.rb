require "pg"

uri = URI.parse(ENV["LINK_INCORPEL"])
link = PG.connect :host => uri.hostname, :port => uri.port, :dbname => uri.path[1..-1], :user => uri.user, :password => uri.password

# cSpell:disable
tabela_principal = "NORINT"
# cSpell:enable

# Fatores a serem aplicados na tabela principal
Produtos = Struct.new(:grupo, :subgrupo, :fator)

produtos = [
  Produtos.new("01", "002", 1.14),
  Produtos.new("01", "044", 1.14),
  Produtos.new("01", "045", 1.14),
  Produtos.new("01", "082", 1.14),
  Produtos.new("01", "083", 1.14),
  Produtos.new("01", "001", 1.10),
  Produtos.new("01", "018", 1.10),
  Produtos.new("01", "031", 1.10),
  Produtos.new("01", "050", 1.10),
  Produtos.new("01", "063", 1.10),
  Produtos.new("01", "072", 1.10),
  Produtos.new("01", "004", 1.08),
  Produtos.new("01", "011", 1.08),
  Produtos.new("01", "012", 1.08),
  Produtos.new("01", "025", 1.08),
  Produtos.new("01", "028", 1.08),
  Produtos.new("01", "029", 1.08),
  Produtos.new("01", "042", 1.08),
  Produtos.new("01", "043", 1.08),
  Produtos.new("01", "061", 1.08),
  Produtos.new("01", "084", 1.08),
  Produtos.new("01", "051", 1.06),
  Produtos.new("02", "003", 1.10),
  Produtos.new("02", "002", 1.08),
  Produtos.new("02", "064", 1.08),
  Produtos.new("02", "081", 1.08),
  Produtos.new("02", "004", 1.06),
  Produtos.new("03", "002", 1.14),
  Produtos.new("03", "004", 1.10),
  Produtos.new("03", "010", 1.10),
  Produtos.new("03", "001", 1.08),
  Produtos.new("03", "007", 1.08),
  Produtos.new("03", "008", 1.08),
  Produtos.new("03", "011", 1.08),
  Produtos.new("03", "005", 1.06),
]

# Fatores e somas a serem aplicados nas tabelas secundárias
Tabelas = Struct.new(:destino, :origem, :fator, :somar)

# cSpell:disable
tabelas = [
  Tabelas.new("SENINT", "NORINT", 0.95, 0.0),
  Tabelas.new("NORILH", "NORINT", 1.01, 0.0),
  Tabelas.new("SENILH", "NORILH", 0.95, 0.0),
  Tabelas.new("NORITA", "NORILH", 1.0, 0.01),
  Tabelas.new("SENITA", "SENILH", 1.0, 0.01),
  Tabelas.new("NORFX", "NORINT", 1.015, 0.0),
  Tabelas.new("SENFX", "NORFX", 0.95, 0.0),
  Tabelas.new("NOREXT", "NORINT", 1.02, 0.0),
  Tabelas.new("SENEXT", "NOREXT", 0.95, 0.0),
  Tabelas.new("NORLAG", "NORINT", 1.03, 0.0),
  Tabelas.new("SENLAG", "NORLAG", 0.95, 0.0),
  Tabelas.new("NORIA", "NORINT", 1.04, 0.0),
  Tabelas.new("SENIA", "NORIA", 0.95, 0.0),
  Tabelas.new("NORRS", "NORINT", 1.079, 0.0),
  Tabelas.new("SENRS", "NORRS", 0.95, 0.0),
  Tabelas.new("NORFEF", "NORINT", 1.08, 0.0),
  Tabelas.new("SENFEF", "NORFEF", 0.95, 0.0),
  Tabelas.new("NORRJ", "NORINT", 1.10, 0.0),
  Tabelas.new("SENRJ", "NORRJ", 0.95, 0.0),
]
# cSpell:enable

puts "Updating preços"

produtos.each do |produto|
  items = []
  consulta = link.exec "SELECT codigo FROM produtos" +
                       " WHERE grupo = '" + produto.grupo + "'" +
                       " AND subgrupo = '" + produto.subgrupo + "'"
  consulta.each do |item|
    items.push(item["codigo"])
  end

  if items.length == 0
    puts ""
    puts "Não há produtos" +
      " no grupo: '" + produto.grupo + "' e subgrupo: '" + produto.subgrupo + "'"
    next
  end

  puts ""
  puts "Atualizando a tabela principal '" + tabela_principal + "'" +
         " no grupo: '" + produto.grupo + "' e subgrupo: '" + produto.subgrupo + "'" +
         " com o fator: '" + produto.fator.to_s() + "'"
  items.each do |item|
    command = "UPDATE precos SET valor = valor * " + produto.fator.to_s() +
              " WHERE tabela = '" + tabela_principal + "'" +
              " AND produto = '" + item + "'"
    puts "Comando: " + command
  end

  tabelas.each do |tabela|
    if tabela.fator != 1.0
      puts ""
      puts "Atualizando a tabela secundária '" + tabela.destino + "'" +
             " no grupo: '" + produto.grupo + "' e subgrupo: '" + produto.subgrupo + "'" +
             " com o fator: '" + tabela.fator.to_s() + "' da origem: '" + tabela.origem + "'"
      items.each do |item|
        command = "UPDATE precos SET valor = (" +
                  " SELECT valor FROM precos" +
                  " WHERE tabela = '" + tabela.origem + "'" +
                  " AND produto = '" + item + "'" +
                  " ) * " + tabela.fator.to_s() +
                  " WHERE tabela = '" + tabela.destino + "'" +
                  " AND produto = '" + item + "'"
        puts "Comando: " + command
      end
    end
    if tabela.somar != 0.0
      puts ""
      puts "Atualizando a tabela secundária '" + tabela.destino + "'" +
             " no grupo: '" + produto.grupo + "' e subgrupo: '" + produto.subgrupo + "'" +
             " com o somar: '" + tabela.somar.to_s() + "' da origem: '" + tabela.origem + "'"
      items.each do |item|
        command = "UPDATE precos SET valor = (" +
                  " SELECT valor FROM precos" +
                  " WHERE tabela = '" + tabela.origem + "'" +
                  " AND produto = '" + item + "'" +
                  " ) + " + tabela.somar.to_s() +
                  " WHERE tabela = '" + tabela.destino + "'" +
                  " AND produto = '" + item + "'"
        puts "Comando: " + command
      end
    end
  end
end

puts ""
puts "Atualizado todos os preços com sucesso."
