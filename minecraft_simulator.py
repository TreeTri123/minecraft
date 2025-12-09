import re
import pyautogui
import time

# Delay before starting
DELAY_BEFORE_START = 3

# Timing configurations for actions
WALK_SPEED = 0.5  # seconds per unit
RUN_SPEED = 0.3   # seconds per unit
JUMP_DURATION = 0.08
SCROLL_DELAY = 0.1

def switch_hotbar(slot):
    """Switch to hotbar slot (1-9)"""
    print(f"[ACTION] Switching to hotbar slot {slot}")
    pyautogui.press(str(slot))
    time.sleep(0.2)

def give_item(item_id):
    """Give item with specified ID to hotbar"""
    print(f"[ACTION] Giving item with ID {item_id} to hotbar")
    pyautogui.press('/')  # Open chat
    time.sleep(0.2)
    pyautogui.typewrite(f"give @p {item_id}\n")
    time.sleep(0.5)  

def drop_items(amount):
    """Drop items from hotbar"""
    print(f"[ACTION] Dropping {amount} items from hotbar")
    for _ in range(amount):
        pyautogui.keyDown('q')
        time.sleep(0.1)
        pyautogui.keyUp('q')
        time.sleep(0.1)
    time.sleep(0.5)

def clear_inventory():
    """Clear all items from inventory"""
    print(f"[ACTION] Clearing all items from inventory")
    pyautogui.press('/')  # Open chat
    time.sleep(0.2)
    pyautogui.typewrite("clear\n")

def walk(duration):
    """Walk forward for specified duration"""
    print(f"[ACTION] Walking forward for {duration} seconds")
    time.sleep(0.2)
    pyautogui.keyDown('w')
    time.sleep(duration * WALK_SPEED)
    pyautogui.keyUp('w')
    time.sleep(0.5)

def jump():
    """Jump"""
    print(f"[ACTION] Jumping!")
    pyautogui.keyDown('space')
    time.sleep(JUMP_DURATION)
    pyautogui.keyUp('space')

def eat(duration):
    """Eat food for specified duration"""
    print(f"[ACTION] Eating food for {duration} seconds")
    pyautogui.mouseDown(button='right')
    time.sleep(duration)
    pyautogui.mouseUp(button='right')

def break_block(duration):
    """Mine block in front of player"""
    print(f"[ACTION] Mining block for {duration} seconds")
    pyautogui.mouseDown(button='left')
    time.sleep(duration)
    pyautogui.mouseUp(button='left')

def parse_and_execute(line, registers):
    """Parse and execute a single line of MinecraftLanguage assembly"""
    line = line.strip()
    
    # Skip empty lines and comments
    if not line or line.startswith('#'):
        return
    
    # Parse register references (convert $t1-$t9 to their values)
    def get_register_value(operand):
        if operand.startswith('$'):
            return registers.get(operand, 0)
        return int(operand)
    
    # give $t1, imm - Give item with ID
    if re.match(r'give\s+\$\w+,\s*\d+', line, re.IGNORECASE):
        match = re.search(r'give\s+\$(\w+),\s*(\d+)', line, re.IGNORECASE)
        if match:
            reg_name = match.group(1).lower()
            item_id = int(match.group(2))
            
            num = re.findall(r'\d+', reg_name)
            if num:
                slot = int(num[0])
            else:
                slot = 1  # Default to slot 1 if no number found
                
            switch_hotbar(slot)
            give_item(item_id)
    
    # drop $t1, $t1, imm - Drop items
    elif re.match(r'drop\s+\$\w+,\s*\$\w+,\s*\d+', line, re.IGNORECASE):
        match = re.search(r'drop\s+\$(\w+),\s*\$(\w+),\s*(\d+)', line, re.IGNORECASE)
        if match:
            amount = int(match.group(3))
            drop_items(amount)
    
    # switch $t1 - Switch hotbar slot
    elif re.match(r'switch\s+\$\w+', line, re.IGNORECASE):
        match = re.search(r'switch\s+\$(\w+)', line, re.IGNORECASE)
        if match:
            reg_name = match.group(1).lower()
            num = re.findall(r'\d+', reg_name)
            if num:
                slot = int(num[0])
            else:
                slot = 1  # Default to slot 1 if no number found
            switch_hotbar(slot)
    
    # clear - Clear inventory
    elif re.match(r'clear\s*$', line, re.IGNORECASE):
        clear_inventory()
    
    # walk imm - Walk forward
    elif re.match(r'walk\s+\d+', line, re.IGNORECASE):
        match = re.search(r'walk\s+(\d+)', line, re.IGNORECASE)
        if match:
            duration = int(match.group(1))
            walk(duration)
    
    # jump - Jump
    elif re.match(r'jump\s*$', line, re.IGNORECASE):
        jump()
    
    # eat imm - Eat food
    elif re.match(r'eat\s+\d+', line, re.IGNORECASE):
        match = re.search(r'eat\s+(\d+)', line, re.IGNORECASE)
        if match:
            duration = int(match.group(1))
            eat(duration)
    
    # eat without parameter (instant eat)
    elif re.match(r'eat\s*$', line, re.IGNORECASE):
        eat(0.5)
    
    # break imm - Mine block
    elif re.match(r'break\s+\d+', line, re.IGNORECASE):
        match = re.search(r'break\s+(\d+)', line, re.IGNORECASE)
        if match:
            duration = int(match.group(1))
            break_block(duration)

def main():
    import sys
    
    # Get assembly file from command-line argument
    if len(sys.argv) < 2:
        print("Usage: python minecraft_simulator.py <assembly_file>")
        print("Example: python minecraft_simulator.py mips1.asm")
        sys.exit(1)
    
    asm_file = sys.argv[1]
    
    try:
        with open(asm_file, 'r') as f:
            lines = f.readlines()
    except FileNotFoundError:
        print(f"Error: {asm_file} not found!")
        return
    
    # Initialize registers (MIPS registers)
    registers = {f'${reg}': 0 for reg in ['t0', 't1', 't2', 't3', 't4', 't5', 't6', 't7', 't8', 't9']}
    
    print(f"Starting Minecraft Simulator in {DELAY_BEFORE_START} seconds... Switch to Minecraft!")
    time.sleep(DELAY_BEFORE_START)
    
    print(f"[SIMULATOR] Reading and executing {asm_file}")
    print(f"[SIMULATOR] Total lines: {len(lines)}")
    
    try:
        for i, line in enumerate(lines, 1):
            print(f"[LINE {i}] {line.strip()}")
            parse_and_execute(line, registers)
            time.sleep(1)  # Small delay between instructions
    
    except KeyboardInterrupt:
        print("\n[SIMULATOR] Stopped by user")
    except Exception as e:
        print(f"\n[ERROR] {e}")

if __name__ == "__main__":
    main()
