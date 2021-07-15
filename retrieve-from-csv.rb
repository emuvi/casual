text = File.read 'retrieve-from-csv.txt'
text.gsub! /\r\n?/, "\n"
text.each_line do |line|
  print line
end