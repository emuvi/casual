package pin

import pin.JarBox

println 'Changing CSV Style...'

void changeCSVStyle(folder) {
  for (inside in folder.listFiles()) {
    println inside
  }
}

for (arg in args) {
  changeCSVStyle(new File(arg))
}
