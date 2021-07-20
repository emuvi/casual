require 'pg'

url_links = ['LINK_INCORPEL', 'LINK_FLEXPEL']
url_links.each do |url_link|
  puts "Listing on " + url_link
  uri = URI.parse(ENV[url_link])
  link = PG.connect :host => uri.hostname, :port => uri.port, :dbname => uri.path[1..-1], :user => uri.user, :password => uri.password

  search = link.exec "SELECT cnpjcpf, COUNT(cnpjcpf) FROM pessoas GROUP BY cnpjcpf ORDER BY count DESC"

  search.each do |possible|
    break if possible['count'].to_i <= 1
    duplicates = link.exec "SELECT codigo FROM pessoas WHERE cnpjcpf = '" + possible['cnpjcpf'] + "' ORDER BY codigo"
    duplicates.each do |duplicate|
        puts "CNPJCPF: " + possible['cnpjcpf'] + " -> " + duplicate['codigo']
    end
  end
end