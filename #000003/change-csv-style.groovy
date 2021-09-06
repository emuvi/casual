package pin

import pin.jarbox.dat.CSVFile
import pin.jarbox.dat.FileMode

println('Changing CSV Style...')

founds = []

void searchForCSV(onPath) {
  if (!onPath.exists()) {
    return
  }
  if (onPath.isDirectory()) {
    println('Searching for CSV on ' + onPath.getName())
    for (inside in onPath.listFiles()) {
      searchForCSV(inside)
    }
  } else if (onPath.getName().toLowerCase().endsWith('.csv')) {
    founds.add(onPath)
  }
}

for (arg in args) {
  searchForCSV(new File(arg))
}

for (found in founds) {
  println('Reading: ' + found)
  lines = []
  try (reader = new CSVFile(found, FileMode.READ)) {
    line = reader.readLine()
    while (line != null) {
      lines.add(line)
      line = reader.readLine()
    }
  }
  edited = new File(found.getParent(), found.getName() + '-new');
  println('Writing: ' + edited)
  try (writer = new CSVFile(edited, FileMode.WRITE)) {
    for (line in lines) {
      writer.writeLine(*line)
    }
  }
  print('Replacing... ')
  found.delete()
  edited.renameTo(found)
  println('Done!')
}

println('Finished')
