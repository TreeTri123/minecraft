###README — MIPS → Minecraft Automation

## Table of Contents
- Files included
- Instructions Implemented
- How to Run and Test in Mars
- Simulation Running

# Files Included
- MinecraftLanguage.java
- MinecraftLanguage.jar
- MARS-LE
- mips1.asm : MIPS program that prints movement commands
- minecraft_simulator.py  : Python script that reads MIPS and then sends those intructions to Minecraft

# Instructions Implemented
| Instruction | Format                | Description                                         |
|-------------|------------------------|-----------------------------------------------------|
| itmadd      | `itmadd $rd, $rs, imm` | Add # of items to your stack in your hotbar         |
| drop        | `drop $rd, $rs, imm`   | Drop # of items from your hotbar                    |
| itmmul      | `itmmul $rd, $rs, imm` | Multiply an item in your hotbar by specified #      |
| give        | `give $rd, imm`        | Load item using ID number into hotbar slot          |
| switch      | `switch $rs`           | Quickly switch to wanted hotbar slot                |
| sr          | `sr imm`               | Scroll right in the hotbar a # of times             |
| sl          | `sl imm`               | Scroll left in the hotbar a # of times              |
| clear       | `clear`                | Clear all items                                     |
| xp          | `xp imm`               | Give # amount of XP to oneself                      |
| incspd      | `incspd imm`           | Increase speed to specified #                       |
| walk        | `walk imm`             | Walk forward for # of seconds                       |
| jump        | `jump`                 | Jump one time                                       |
| run         | `run imm`              | Run forward for # of seconds                        |
| eat         | `eat imm`              | Eat for # of seconds                                |
| swim        | `swim imm`             | Swim forward for # of seconds                       |
| break       | `break imm`            | Break blocks for # of seconds                       |
| interact    | `interact`             | Interact with an object one time                    |
| clmb        | `clmb imm`             | Climb up ladders or vines for # of seconds          |
| atck        | `atck imm`             | Attack entity a # of times                          |
| turn        | `turn imm`             | Rotate the player’s view by # of degrees            |

# How to Run and Test in Mars
 - Open mars.java (make sure in MARS-LE directory)
 ``java -jar mars.java``
 
 - GUI open Tools -> Language Switcher -> Select Language -> Minecraft Language

 - Import or create asm file with custom assembly code
 ``Example: give $t1, 5``

 - Save file and run by clicking the build and run button withi the GUI

# Simulation Run
- Preparation: Have Minecraft 1.17 open to a dedicated world 

- Make an asm script with custom Minecraft Language assembly within the minecraft directory
``vi asm file``

- Run Python Script with asm file 
``python minecraft_simulation.py file.asm``

- Click onto minecraft and watch the simulation run