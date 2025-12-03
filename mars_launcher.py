import subprocess
import pyautogui
import time
import re
import sys

# Path to your Mars JAR
MARS_PATH = "MARS-LE/Mars.jar"

def launch_mars_gui():
    """Launch MARS GUI so you can select Dragon Ball Assembly"""
    print("[mars_launcher] Launching MARS GUI...")
    print("[mars_launcher] After loading, go to Settings > Language and select 'Dragon Ball Assembly'")
    print("[mars_launcher] Then load mydragonball.asm and run it")
    
    # Launch MARS with no arguments to get GUI
    proc = subprocess.Popen(
        ["java", "-jar", MARS_PATH],
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        bufsize=1,
        text=True,
    )
    
    try:
        # Keep the process running
        for line in proc.stdout:
            if line.strip():
                print("[MARS]", line.strip())
    except KeyboardInterrupt:
        print("\n[mars_launcher] MARS closed.")
    finally:
        try:
            proc.kill()
        except:
            pass

def launch_mars_gui_with_file():
    """Launch MARS GUI and load mydragonball.asm"""
    print("[mars_launcher] Launching MARS GUI with mydragonball.asm...")
    print("[mars_launcher] Go to Settings > Language and select 'Dragon Ball Assembly'")
    print("[mars_launcher] Then click Run to execute your program")
    
    # Launch MARS with the file but let GUI handle it
    proc = subprocess.Popen(
        ["java", "-jar", MARS_PATH, "mydragonball.asm"],
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        bufsize=1,
        text=True,
    )
    
    try:
        for line in proc.stdout:
            if line.strip():
                print("[MARS]", line.strip())
    except KeyboardInterrupt:
        print("\n[mars_launcher] MARS closed.")
    finally:
        try:
            proc.kill()
        except:
            pass

if __name__ == "__main__":
    print("\n=== MARS GUI Launcher for Dragon Ball Assembly ===\n")
    print("Choose an option:")
    print("1. Launch MARS GUI (blank)")
    print("2. Launch MARS GUI with mydragonball.asm pre-loaded")
    print("3. Exit")
    
    choice = input("\nEnter your choice (1-3): ").strip()
    
    if choice == "1":
        launch_mars_gui()
    elif choice == "2":
        launch_mars_gui_with_file()
    elif choice == "3":
        print("Exiting...")
    else:
        print("Invalid choice. Exiting...")
