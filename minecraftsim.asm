.text
.globl main

main:
    # Initialize power level
    li $t5, 0
    
# load a cookie into hotbar slot 5
	give $t5, 357
	
	# double the amount of cookies 
	itmmul $t5, $t5, 2

	eat 
	eat 
	eat	
