README — MIPS → Minecraft Automation

FILES INCLUDED:
- mymips.asm : MIPS program that prints movement commands
- bridge.py  : Python script that reads MIPS output and sends keys to Minecraft

YOU MUST DOWNLOAD MARS YOURSELF:
Download "Mars.jar" from:
https://courses.missouristate.edu/KenVollmar/mars/

Place Mars.jar in the SAME folder as mymips.asm and bridge.py.

INSTRUCTIONS:
1. Install Python (from python.org)
2. Install PyAutoGUI:
   pip install pyautogui
3. Put these files in one folder:
   - mymips.asm
   - bridge.py
   - Mars.jar (you download)
4. Open Minecraft and enter a world.
5. Run:
   python bridge.py
6. Within 3 seconds, focus Minecraft.
7. Your character will move based on MIPS program output.

STOPPING:
Press CTRL+C in the terminal.

ONLY USE IN SINGLEPLAYER OR SERVERS THAT ALLOW MACROS.
