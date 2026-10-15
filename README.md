# Java Gui
* [JavaFX](https://openjfx.io/)
* slim JAR and packaging all dependencies in Docker

## Commands
* Load dependencies: ```mvn dependency:copy-dependenciese```
* Compile: ```mvn compile```
* Package: ```mvn package```
* Run Jar as modules: ```java --module-path "target/*.jar:target/dependency" --module com.brakid.gui/com.brakid.gui.Main```
* Run Class via Maven: ```mvn exec:exec```

### Docker (rather useless)

```
docker build -t javagui .
docker run --rm --name javaguijavagui
```
