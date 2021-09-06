require 'pg'

uri = URI.parse(ENV['LINK_INCORPEL'])
link = PG.connect :host => uri.hostname, :port => uri.port, :dbname => uri.path[1..-1], :user => uri.user, :password => uri.password

Trasition = Struct.new(:toRemove, :toMaintain)
transitions = [
  Trasition.new("006597", "003013"),
  Trasition.new("007253", "007311"),
  Trasition.new("004275", "007503"),
]

transitions.each do |transition|
  link.exec "UPDATE lancamentos SET pessoa = '" + transition.toMaintain + "'" +
  " WHERE pessoa = '" + transition.toRemove + "'"
  link.exec "UPDATE lancamentos SET cobranca = '" + transition.toMaintain + "'" +
  " WHERE cobranca = '" + transition.toRemove + "'"
  link.exec "UPDATE lancamentos SET representante = '" + transition.toMaintain + "'" +
  " WHERE representante = '" + transition.toRemove + "'"
  link.exec "UPDATE faturas SET cliente = '" + transition.toMaintain + "'" +
  " WHERE cliente = '" + transition.toRemove + "'"
  link.exec "UPDATE faturas SET cobranca = '" + transition.toMaintain + "'" +
  " WHERE cobranca = '" + transition.toRemove + "'"
  link.exec "UPDATE faturas SET entrega = '" + transition.toMaintain + "'" +
  " WHERE entrega = '" + transition.toRemove + "'"
  link.exec "UPDATE faturas SET representante = '" + transition.toMaintain + "'" +
  " WHERE representante = '" + transition.toRemove + "'"
  link.exec "UPDATE faturas SET transportadora = '" + transition.toMaintain + "'" +
  " WHERE transportadora = '" + transition.toRemove + "'"
  link.exec "UPDATE pedidos SET cliente = '" + transition.toMaintain + "'" +
  " WHERE cliente = '" + transition.toRemove + "'"
  link.exec "UPDATE pedidos SET cobranca = '" + transition.toMaintain + "'" +
  " WHERE cobranca = '" + transition.toRemove + "'"
  link.exec "UPDATE pedidos SET entrega = '" + transition.toMaintain + "'" +
  " WHERE entrega = '" + transition.toRemove + "'"
  link.exec "UPDATE pedidos SET representante = '" + transition.toMaintain + "'" +
  " WHERE representante = '" + transition.toRemove + "'"
  link.exec "UPDATE prepedidos SET cliente = '" + transition.toMaintain + "'" +
  " WHERE cliente = '" + transition.toRemove + "'"
  link.exec "UPDATE prepedidos SET cobranca = '" + transition.toMaintain + "'" +
  " WHERE cobranca = '" + transition.toRemove + "'"
  link.exec "UPDATE prepedidos SET entrega = '" + transition.toMaintain + "'" +
  " WHERE entrega = '" + transition.toRemove + "'"
  link.exec "UPDATE prepedidos SET representante = '" + transition.toMaintain + "'" +
  " WHERE representante = '" + transition.toRemove + "'"
  link.exec "DELETE FROM pessoas_comissoes" +
  " WHERE pessoa = '" + transition.toRemove + "'"
  link.exec "DELETE FROM pessoas" +
  " WHERE codigo = '" + transition.toRemove + "'"
end
