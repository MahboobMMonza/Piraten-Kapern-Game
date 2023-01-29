# A1 - Piraten Karpen

  * Author: Mohammad Mahdi Mahboob
  * Email: mahbom2@mcmaster.ca

## Build and Execution

  * To clean your working directory:
    * `mvn clean`
  * The available strategies are ___RANDOM___ and ___COMBO___, and they must be entered with the correct spelling
    (case-insensitive)
    * You must enter 2 or more strategies.
  * To compile the project:
    * `mvn compile`
  * To run the project in development mode with the intended strategies (e.g. player 1 uses the _random_ strategy
      and player 2 uses the _combo_ strategy):
    * `mvn -q exec:java -Dexec.args="random combo"` (here, `-q` tells maven to be _quiet_)
  * To package the project as a turn-key artifact:
    * `mvn package`
  * To run the packaged delivery with the strategies used in the development mode example:
    * `java -jar target/piraten-karpen-jar-with-dependencies.jar random combo`

  Remark: **We are assuming here you are using a _real_ shell (e.g., anything but PowerShell on Windows)**

  __NOTE__: Trace logging is automatically conducted by the system and is available in the _logs_ folder.


## Feature Backlog

  * Status:
    * Pending (P), Started (S), Blocked (B), Done (D)
  * Definition of Done (DoD):
    * The feature works as intended, is efficient enough for the given scale, and is well documented
      or easy to understand. Additionally, code is written in such a manner that future MVPs that
      require modification of the feature can be implemented with minimal changes to its dependents.

### Backlog

| MVP? | ID  | Feature  | Status  |  Started  | Delivered |
| :-:  |:-:  |---       | :-:     | :-:       | :-:       |
| -   | F01 | Roll eight dices  | D | 01/01/2023 | 01/23/2023 |
| x   | F02 | End of turn with three cranes | D | 01/23/2023 | 01/25/2023 |
| -   | F03 | Player keeping random dice to end their turn | D | 01/25/2023 | 01/26/2023 |
| -   | F04 | Combo strategies | D | 01/26/2023 | 01/27/2023 |
| x   | F05 | Three skulls disqualifies player from scoring | D | 01/27/2023 | 01/27/2023 |
| -   | F06 | Score points: 3-of-a-kind (score calculator class) | D | 01/27/2023 | 01/27/2023 |
| x   | F07 | Score points: All sets (sets only) | D | 01/27/2023 | 01/27/2023 |
| x   | F08 | Score points: Full chest | D | 01/28/2023 | 01/28/2023 |
| -   | F09 | Drawing cards | D | 01/27/2023 | 01/28/2023 |
| x   | F10 | Sea Battle card integration | D | 01/28/2023 | 01/29/2023 |
| x   | F11 | Smart Strategy | B (F10) | | |
| x   | F12 | Shuffling the deck | P | | |
| ... | ... | ... |

