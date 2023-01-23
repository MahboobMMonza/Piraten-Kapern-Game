# A1 - Piraten Karpen

  * Author: Mohammad Mahdi Mahboob
  * Email: mahbom2@mcmaster.ca

## Build and Execution

  * To clean your working directory:
    * `mvn clean`
  * To compile the project:
    * `mvn compile`
  * To run the project in development mode:
    * `mvn -q exec:java` (here, `-q` tells maven to be _quiet_)
  * To package the project as a turn-key artifact:
    * `mvn package`
  * To run the packaged delivery:
    * `java -jar target/piraten-karpen-jar-with-dependencies.jar`

Remark: **We are assuming here you are using a _real_ shell (e.g., anything but PowerShell on Windows)**

## Feature Backlog

 * Status:
   * Pending (P), Started (S), Blocked (B), Done (D)
 * Definition of Done (DoD):
   * The feature works as intended, is efficient enough for the given scale, and is well documented.
     Additionally, code is written in such a manner that future MVPs that require modification of
     the feature can be implemented with minimal changes to its dependents.

### Backlog

| MVP? | Id  | Feature  | Status  |  Started  | Delivered |
| :-:  |:-:  |---       | :-:     | :-:       | :-:       |
| -   | F01 | Roll eight dices  | D | 01/01/2023 | 01/23/2023 |
| x  | F02 | End of turn with three cranes | B (F01) | |
| -   | F03 | Player keeping random dice at their turn | B (F01) | |
| x   | F04 | Three skulls disqualifies player | B (F03) | |
| -   | F05 | Score points: 3-of-a-kind | P | |
| x   | F06 | Score points: All sets | B (F05) | |
| ... | ... | ... |

