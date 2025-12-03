# mydragonball.asm - Using Dragon Ball Assembly custom language
.data
newline: .asciiz "\n"
forward_msg: .asciiz "FORWARD ATTACK!\n"
jump_msg: .asciiz "INSTANT TRANSMISSION!\n"

.text
.globl main

main:
    # Initialize power level
    li $t0, 0
    
loop:
    # Increase power level with pow (Power up)
    pow $t0, 5
    
    # Check if power is strong enough with it (Instant Transmission - conditional jump)
    it $t0, do_jump
    
    # If not strong enough, output forward command
    li $v0, 1
    li $a0, 1
    syscall
    
    # Print newline
    li $v0, 4
    la $a0, newline
    syscall
    
    # Delay
    li $t1, 4000000
delay1:
    addi $t1, $t1, -1
    bnez $t1, delay1
    
    j loop
    
do_jump:
    # Power is high enough - output jump command
    li $v0, 1
    li $a0, 2
    syscall
    
    # Print newline
    li $v0, 4
    la $a0, newline
    syscall
    
    # Use Kaioken to boost power further
    kai $t0, $t1
    
    # Delay
    li $t1, 4000000
delay2:
    addi $t1, $t1, -1
    bnez $t1, delay2
    
    # Reset power level
    li $t0, 0
    
    j loop
