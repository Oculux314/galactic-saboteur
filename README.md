# Galactic Saboteur

## Quickstart

Either:
- Double click `start.bat`.
- Run `./start.bat` in the terminal at the root (where `pom.xml` is located).

## API Setup

- Add in the root of the project (i.e., the same level where `pom.xml` is located) a file named `apiproxy.config`.
- Add OpenAI API credentials to `apiproxy.config`.

Example:

```
email: "upi123@aucklanduni.ac.nz"
apiKey: "YOUR_KEY"
```

This will enable the project to use OpenAI's Chat-GPT 3.5 model to provide interaction.

## Codestyle Grading Setup

- Repeat API setup process with codestyle credentials in `codestyle.config`.

Example:

```
email: "upi123@aucklanduni.ac.nz"
accessToken: "YOUR_KEY"
```

Not necessary to run the game, but retained for completeness.

## Commands

### Run the game

`./mvnw clean javafx:run`

### Debug the game

`./mvnw clean javafx:run@debug` then in VS Code "Run & Debug", then run "Debug JavaFX"

### Execute codestyle grader

`./mvnw clean compile exec:java@style`

### Package the game as a jar file

`./mvnw clean package`
