import subprocess
import pyautogui
import time
import re
import sys

# Path to your Mars JAR
MARS_PATH = "MARS-LE/Mars.jar"

# MARS run command using DragonBallAssembly
EMU_CMD = ["java", "-jar", MARS_PATH, "mydragonball.asm", "nc", "sm", "we"]

DELAY_BEFORE_START = 3
HOLD_FORWARD_SECONDS = 0.5

def jump():
    print("[dragonball_bridge] INSTANT TRANSMISSION - jumping (real keyDown/keyUp)")
    pyautogui.keyDown('space')
    time.sleep(0.08)   # Mimics a real human tap
    pyautogui.keyUp('space')

def move_forward():
    print("[dragonball_bridge] FORWARD ATTACK - moving forward")
    pyautogui.keyDown('w')
    time.sleep(HOLD_FORWARD_SECONDS)
    pyautogui.keyUp('w')

def handle_command(line):
    line = line.strip()
    if not line:
        return

    # Expect integers like "1" or "2"
    m = re.match(r"^(-?\d+)$", line)
    if not m:
        return

    code = int(m.group(1))

    if code == 1:
        move_forward()
    elif code == 2:
        jump()

def main():
    print(f"[dragonball_bridge] Starting in {DELAY_BEFORE_START} seconds... Switch to Minecraft.")
    print("[dragonball_bridge] Charging Ki energy with Dragon Ball Assembly!")
    time.sleep(DELAY_BEFORE_START)

    proc = subprocess.Popen(
        EMU_CMD,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        bufsize=1,
        text=True,
    )

    try:
        for line in proc.stdout:
            print("[MARS]", line.strip())
            handle_command(line)
    except KeyboardInterrupt:
        print("[dragonball_bridge] Battle stopped by user.")
    finally:
        try:
            proc.kill()
        except:
            pass

if __name__ == "__main__":
    main()
