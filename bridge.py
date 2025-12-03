import subprocess
import pyautogui
import time
import re
import sys

# Path to your Mars JAR
MARS_PATH = "Mars4_5.jar"

# MARS run command
EMU_CMD = ["java", "-jar", MARS_PATH, "mymips.asm", "nc", "sm", "we"]

DELAY_BEFORE_START = 3
HOLD_FORWARD_SECONDS = 0.5

def jump():
    print("[bridge] jumping (real keyDown/keyUp)")
    pyautogui.keyDown('space')
    time.sleep(0.08)   # Mimics a real human tap
    pyautogui.keyUp('space')

def move_forward():
    print("[bridge] moving forward")
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
    print(f"Starting in {DELAY_BEFORE_START} seconds... Switch to Minecraft.")
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
        print("Stopped by user.")
    finally:
        try:
            proc.kill()
        except:
            pass

if __name__ == "__main__":
    main()
