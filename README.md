# Dungeon Escape

## Video Link

We made a video instead of doing a tutorial. Here is the video link: https://youtu.be/5NqbUtwRaLg

## Generating Javadocs

Run the command `mvn site`, then open the html file at `target/site/apidocs/index.html`

## Generating a JAR file

Run the command `mvn clean package`. The JAR file will then be at `target/group-10-game-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Building

Run the command `mvn clean package`

## Running

Make sure to build the game first.

Then, to run the game, you must first make sure you are in the root directory of the project. This only applies before version 1.1 of our game, which includes our group's submission for phase 2. Next, run the command `java -jar target/group-10-game-1.0-SNAPSHOT-jar-with-dependencies.jar`

After version 1.1 (so for our submissions for phase 3 and phase 4), you can run the game JAR file from any directory. For example, if you are currently in the `target` directory inside the project root directory, run the game with the command `java -jar group-10-game-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Testing

Run the command `mvn clean test`

Note that the tests take a while to complete. Many windows will keep popping up. Make sure to not close any window or click anywhere on the windows, as it can cause errors for the tests. Also, some individual tests take a while to complete. For instance, there is a test that takes about 60 seconds, and another which takes about 19 seconds. The approximately 19 second test is that long since it needs to wait until a bonus reward has guaranteed to spawn (bonus rewards spawn at random times, but are guaranteed to appear after a certain amount of time). The approximately 60 second test needs to wait for the game timer to hit one minute.

## Artifact Locations

Some artifacts have also been pushed to our GitLab repository (of course, such artifacts can still be created using the information in this README file). The location of the JAR file is `target/group-10-game-1.0-SNAPSHOT-jar-with-dependencies.jar`. The location of the Javadocs is `target/site/apidocs` (open the `index.html` file in the `target/site/apidocs` folder).

## Asset Credits

### Images

- Dungeon Tileset II - Extended: https://nijikokun.itch.io/dungeontileset-ii-extended
  - Niji
  - Note that the above tileset is an extension of the following tileset by Robert (0x72): https://0x72.itch.io/dungeontileset-ii

### Audio

- Door Open And Close: https://opengameart.org/content/door-open-door-close
  - Ivan Gabovitch
- Coin: https://opengameart.org/content/plingy-coin
  - Fupi
- Bomb explosion: https://opengameart.org/content/bombexplosion8bit
  - Luke.RUSTLTD
- Game Theme: https://opengameart.org/content/summer-adventure-background-music
  - by: DJ Soul 22
- Main Menu Theme: https://opengameart.org/content/adventure-theme-0
  - Markus Lindner
- Level Map Swap Error: https://opengameart.org/content/error
  - EZduzziteh
- Level Map Swap Laser Sound: https://www.kenney.nl/assets/digital-audio
  - Kenney
