tasks = """
{
  "version": "2.0.0",
  "tasks": [
    {
      "label": "Build",
      "type": "typescript",
      "tsconfig": "tsconfig.json",
      "problemMatcher": ["\$tsc"],
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

apps = "C:\\Users\\evert\\Devs\\Code\\apps"
for app = readdir(apps)
  println("Application: $app")
  isTyped = isfile(apps * "\\" * app * "\\tsconfig.json")
  hasTest = isfile(apps * "\\" * app * "\\test.bat")
  println("Is typed: $isTyped")
  println("Has test: $hasTest")
  if isTyped && hasTest
    println("Adding tasks to $app")
    folder = apps * "\\" * app * "\\.vscode"
    mkpath(folder)
    write(folder * "\\tasks.json", tasks)
  end
end
