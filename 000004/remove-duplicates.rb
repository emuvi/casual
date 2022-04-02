require 'pg'

uri = URI.parse(ENV['LINK_FLEXPEL'])
link = PG.connect :host => uri.hostname, :port => uri.port, :dbname => uri.path[1..-1], :user => uri.user, :password => uri.password

search = link.exec "SELECT cnpjcpf, COUNT(cnpjcpf) FROM pessoas GROUP BY cnpjcpf ORDER BY count DESC"

search.each do |possible|
  break if possible['count'].to_i <= 1
  duplicates = link.exec "SELECT codigo FROM pessoas WHERE cnpjcpf = '" + possible['cnpjcpf'] + "' ORDER BY codigo"
  alreadyDeleted = false  
  duplicates.each do |duplicate|
    begin
      break if alreadyDeleted
      link.exec "DELETE FROM pessoas WHERE codigo = '" + duplicate['codigo'] + "'"
      alreadyDeleted = true
      puts "Successfully deleted: " + duplicate['codigo'] + " of " + possible['cnpjcpf']
    rescue
      puts "Was not possible to delete: " + duplicate['codigo'] + " of " + possible['cnpjcpf']
    end
  end
end