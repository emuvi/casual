rust_tasks = """
{
  "version": "2.0.0",
  "tasks": [
    {
      "label": "Build",
      "type": "shell",
      "command": "cargo build",
      "group": {
        "kind": "build",
        "isDefault": true
      }
    },
    {
      "label": "Test",
      "type": "shell",
      "command": "cmd /c test.bat",
      "group": {
        "kind": "test",
        "isDefault": true
      }
    }
  ]
}
"""

go_tasks = """
{
  "version": "2.0.0",
  "tasks": [
    {
      "label": "Build",
      "type": "shell",
      "command": "go build",
      "group": {
        "kind": "build",
        "isDefault": true
      }
    },
    {
      "label": "Test",
      "type": "shell",
      "command": "cmd /c test.bat",
      "group": {
        "kind": "test",
        "isDefault": true
      }
    }
  ]
}
"""

cmds = "C:\\Users\\evert\\Devs\\Code\\cmds"
for cmd = readdir(cmds)
  println("Application: $cmd")
  hasTest = isfile(cmds * "\\" * cmd * "\\test.bat")
  isRust = isfile(cmds * "\\" * cmd * "\\Cargo.toml")
  isGo = isfile(cmds * "\\" * cmd * "\\go.mod")
  println("Has test: $hasTest")
  println("Is rust: $isRust")
  println("Is go: $isGo")
  if hasTest && isRust
    println("Adding tasks to $cmd")
    folder = cmds * "\\" * cmd * "\\.vscode"
    mkpath(folder)
    write(folder * "\\tasks.json", rust_tasks)
  elseif hasTest && isGo
    println("Adding tasks to $cmd")
    folder = cmds * "\\" * cmd * "\\.vscode"
    mkpath(folder)
    write(folder * "\\tasks.json", go_tasks)
  end
end
