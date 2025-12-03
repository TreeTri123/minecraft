# mymips.asm  (for MARS)
.data
newline: .asciiz "\n"

.text
.globl main

main:
loop:
    # print 1  (forward)
    li $v0, 1
    li $a0, 1
    syscall

    # newline
    li $v0, 4
    la $a0, newline
    syscall

    # delay
    li $t0, 4000000
delay1:
    addi $t0, $t0, -1
    bnez $t0, delay1

    # print 2 (jump)
    li $v0, 1
    li $a0, 2
    syscall

    # newline
    li $v0, 4
    la $a0, newline
    syscall

    # delay
    li $t0, 4000000
delay2:
    addi $t0, $t0, -1
    bnez $t0, delay2

    j loop
