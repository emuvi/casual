require 'csv'

text = File.read 'leave-on-csv.txt'
text.gsub! /\r\n?/, "\n"
leave = []
text.each_line do |line|
  line.strip!
  leave.push(line)
end
first_line = true
File.open('D:\Temp\new-public.pessoas.csv', 'w') do |writer|
  File.open('D:\Temp\public.pessoas.csv', 'r').each_line do |line|
    if first_line then
      writer.write line
      first_line = false
    else
      first_column = line.index ','
      if first_column > -1 then
        codigo = line[0,first_column]
        codigo.strip!
        if leave.include? codigo then
          writer.write line
        end
      end
    end;
  end
end

