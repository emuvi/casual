require 'pg'

uri = URI.parse(ENV['LINK_INCORPEL'])
link = PG.connect :host => uri.hostname, :port => uri.port, :dbname => uri.path[1..-1], :user => uri.user, :password => uri.password

Produtos = Struct.new(:grupo, :subgrupo, :fator)
produtos = [
  Produtos.new("01", "001", 1.2),
  Produtos.new("01", "004"),
  Produtos.new("01", "011"),
  Produtos.new("01", "012"),
  Produtos.new("01", "018"),
  Produtos.new("01", "025"),
  Produtos.new("01", "031"),
  Produtos.new("01", "043"),
  Produtos.new("01", "050"),
  Produtos.new("01", "051"),
  Produtos.new("01", "052"),
  Produtos.new("01", "061"),
  Produtos.new("01", "063"),
  Produtos.new("02", "001"),
  Produtos.new("02", "003"),
  Produtos.new("02", "004"),
  Produtos.new("02", "064"),
  Produtos.new("02", "081"),
  Produtos.new("03", "004"),
  Produtos.new("03", "005"),
  Produtos.new("03", "006"),
  Produtos.new("03", "007"),
  Produtos.new("03", "008"),
  Produtos.new("03", "010"),
  Produtos.new("03", "011"),
]

tabelas = ["NORINT", "NORILH", "NORITA", "NORFX", "NOREXT", "NORLAG", "NORIA", "NORRS", "NORFEF", "NORRJ", "SENINT", "SENILH", "SENITA", "SENFX", "SENEXT", "SENLAG", "SENIA", "SENRS", "SENFEF", "SENRJ"]

puts "Updating preços..."

produtos.each do |produto|
  tabelas.each do |tabela|
    puts "Updating on grupo: '" + produto.grupo + "' subgrupo: '" + produto.subgrupo + 
    "' tabela: '" + tabela + "'..."
    command = "UPDATE precos SET valor = valor * 1.05 WHERE tabela = '" + tabela + "'" +
    " AND produto IN (SELECT codigo FROM produtos WHERE grupo = '" + produto.grupo + "'" +
    " AND subgrupo = '" + produto.subgrupo + "')"
    puts "Command: " + command
    result = link.exec command
    puts "Updated successfully a total of " + result.cmd_tuples().to_s() + " records."
    puts "-------------------------------"
  end
end

puts "Updated everthing successfully."