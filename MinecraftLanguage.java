    package mars.mips.instructions.customlangs;
    import mars.simulator.*;
    import mars.mips.hardware.*;
    import mars.*;
    import mars.util.*;
    import mars.mips.instructions.*;
/**
 * To create a custom language, you must extend the CustomAssembly abstract class and override its three methods.
 * It must also be part of the mars.mips.instructions.customlangs package.
 * 
 * The populate() method is where the magic happens - you must specify your instructions to be added here.
 * For more examples regarding the instruction format, you can view the implementation of the MIPS instructions in mars/mips/instructions/MipsAssembly.java.
 * 
 * Instructions to get your custom language into MARS:
 * Navigate to the MARS folder in your command terminal and build a JAR file from your custom assembly file.
 * Ensure that the internal folder structure is correct, or the JAR will be broken and MARS won't recognize it.
 * To do this on Windows, input the following commands after navigating to .../MARS/:
 * 
 * javac -d out mars/mips/instructions/customlangs/{NAME OF YOUR LANGUAGE}.java
 * jar cf {NAME OF YOUR LANGUAGE}.jar -C out .
 * rmdir /S /Q out
 * 
 * This will leave you with a working JAR file in the MARS directory containing your custom language. 
 * Drop it into the customlangs folder and it will appear under the Language Switcher tool.
 * @see CustomAssembly
 */
public class MinecraftLanguage extends CustomAssembly{
    @Override
    public String getName(){
        return "Example Custom Language";
    }

    @Override
    public String getDescription(){
        return "A language to demonstrate the basics of the CustomAssembly abstract class";
    }

    @Override
    protected void populate(){
      instructionList.add(
         new BasicInstruction("itmadd $t1,$t1, 32",
          "Add # of items to your stack in your hotbar",
         BasicInstructionFormat.I_FORMAT,
         "000001 00000 01001 0000000000000000",
         new SimulationCode()
        {
            public void simulate(ProgramStatement statement) throws ProcessingException
           {
              int[] operands = statement.getOperands();
              int destnReg = operands[0];
              int add1 = operands[2]; // imm
              int add2 = RegisterFile.getValue(destnReg);
              int sum = add1 + add2;
           // overflow on A+B detected when A and B have same sign and A+B has other sign.
              if ((add1 >= 0 && add2 >= 0 && sum < 0)
                 || (add1 < 0 && add2 < 0 && sum >= 0))
              {
                 throw new ProcessingException(statement,
                     "arithmetic overflow",Exceptions.ARITHMETIC_OVERFLOW_EXCEPTION);
              }
              RegisterFile.updateRegister(operands[0], sum);
           }
        }));
      instructionList.add(
         new BasicInstruction("itmmul $t1,$t1, 2",
          "multiply an item in your hotbar by specified factor",
         BasicInstructionFormat.I_FORMAT,
         "000011 01001 01001 0000000000000000",
         new SimulationCode()
        {
            public void simulate(ProgramStatement statement) throws ProcessingException
           {
              int[] operands = statement.getOperands();
              int destnReg = operands[0];
              int factor = operands[2]; // imm
              int amt = RegisterFile.getValue(destnReg);
              int prod = factor * amt;
           // overflow on A+B detected when A and B have same sign and A+B has other sign.
              if (factor < 0 || amt < 0)
              {
                 throw new ProcessingException(statement,
                     "negative factor or amount not allowed",Exceptions.ARITHMETIC_OVERFLOW_EXCEPTION);
              }
              if (prod > 64) {
                  prod = 64;
              }
              if (prod < 0) {
                  prod = 0;
              }
              RegisterFile.updateRegister(operands[0], prod);
              }
            }));
      instructionList.add(
         new BasicInstruction("give $t1, item_id",
            "takes minecraft item id and loads them into your hotbar slot",
         BasicInstructionFormat.I_FORMAT,
         "000100 01001 00000 0000000000000000",
         new SimulationCode()
         {
               public void simulate(ProgramStatement statement) throws ProcessingException
             {
               int[] operands = statement.getOperands();
               
               //get the item id as a immediate value
               int item_id = operands[1]; // immediate
               //Store item id directly into that register
               RegisterFile.updateRegister(operands[0], item_id);
               System.out.println("Gave item with ID: " + item_id + " to hotbar slot.");
               }  
          }));
      instructionList.add(
         new BasicInstruction("drop $t1,$t1, 32",
          "Drop # of items from your hotbar",
         BasicInstructionFormat.I_FORMAT,
         "000010 01001 01001 0000000000000000",
         new SimulationCode()
        {
            public void simulate(ProgramStatement statement) throws ProcessingException
           {
              int[] operands = statement.getOperands();
              int destnReg = operands[0];
              int add1 = operands[2]; // imm
              int add2 = RegisterFile.getValue(destnReg);
              int differn = add2 - add1;
           // overflow on A+B detected when A and B have same sign and A+B has other sign.
              if ((add1 < 0 && add2 >= 0 && differn < 0)
                 || (add1 >= 0 && add2 < 0 && differn >= 0))
              {
                 throw new ProcessingException(statement,
                     "arithmetic overflow",Exceptions.ARITHMETIC_OVERFLOW_EXCEPTION);
              }
              if (differn < 0) {
                  differn = 0;
              }
                  RegisterFile.updateRegister(operands[0], differn);
                 }
               }));
      instructionList.add(
         new BasicInstruction("switch target", 
         "switch to the wanted hotbar slot $t1-$t9",
         BasicInstructionFormat.J_FORMAT,
         "000110 01001 00000 0000000000000000",
         new SimulationCode()
         {
            public void simulate(ProgramStatement statement) throws ProcessingException
            {
               int[] operands = statement.getOperands();
               RegisterFile.updateRegister("$t1", operands[0]);
               Globals.instructionSet.processJump(
                  ((RegisterFile.getProgramCounter() & 0xF0000000)
                  | (operands[0] << 2)));            
               }
            }));
      instructionList.add(
         new BasicInstruction("walk imm",
         "walks forward for imm amount of time (press w)",
         BasicInstructionFormat.J_FORMAT,
         "100001 00000 00000 0000000000000000",
         new SimulationCode()
            {
               public void simulate(ProgramStatement statement) throws ProcessingException
               {
                  int[] operands = statement.getOperands();
                  int time = operands[0]; // immediate
                  System.out.println("Walking forward for " + time + " seconds.");
               }
         }));
      instructionList.add(
         new BasicInstruction("jump",
         "jumps (press jump)",
         BasicInstructionFormat.J_FORMAT,
         "100010 00000 00000 0000000000000000",
         new SimulationCode()
            {
               public void simulate(ProgramStatement statement) throws ProcessingException
               {
                  System.out.println("Jumping!");
               }
         }));
      instructionList.add(
         new BasicInstruction("Eat imm",
         "eats food in hand for imm amount of seconds",
         BasicInstructionFormat.J_FORMAT,
         "100100 00000 00000 0000000000000000",
         new SimulationCode()
            {
               public void simulate(ProgramStatement statement) throws ProcessingException
               {
                  int[] operands = statement.getOperands();
                  int time = operands[0]; // immediate
                  System.out.println("Eating food for " + time + " seconds.");
               }
         }));
      instructionList.add(
         new BasicInstruction("swim imm",
         "swim forward for imm amount of seconds",    
         BasicInstructionFormat.J_FORMAT,
         "100101 00000 00000 0000000000000000",
         new SimulationCode()
            {
               public void simulate(ProgramStatement statement) throws ProcessingException
               {
                  int[] operands = statement.getOperands();
                  int time = operands[0]; // immediate
                  System.out.println("Swimming forward for " + time + " seconds.");
               }
         }));
      instructionList.add(
         new BasicInstruction("break imm",
         "mines block in front of player for imm amount of seconds",
         BasicInstructionFormat.J_FORMAT,
         "100110 00000 00000 0000000000000000",
         new SimulationCode()
            {
               public void simulate(ProgramStatement statement) throws ProcessingException
               {
                  int[] operands = statement.getOperands();
                  int time = operands[0]; // immediate
                  System.out.println("Mining block for " + time + " seconds.");
               }
         }));
      instructionList.add(
                new BasicInstruction("print $t1, label",
            	 "example",
                BasicInstructionFormat.I_BRANCH_FORMAT,
                "110000 fffff 00000 ssssssssssssssss",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int[] operands = statement.getOperands();             
                     char ch = 0;
                     // Get the name of the label from the token list
                     String label = statement.getOriginalTokenList().get(2).getValue();
                     // Look up the label in the program symbol table to get its address
                     int byteAddress = Globals.program.getLocalSymbolTable().getAddressLocalOrGlobal(label);
                     RegisterFile.updateRegister(operands[0], byteAddress);

                     try
                        {
                           ch = (char) Globals.memory.getByte(byteAddress);
                                             // won't stop until NULL byte reached!
                           while (ch != 0)
                           {
                              SystemIO.printString(new Character(ch).toString());
                              byteAddress++;
                              ch = (char) Globals.memory.getByte(byteAddress);
                           }
                        } 
                           catch (AddressErrorException e)
                           {
                              throw new ProcessingException(statement, e);
                           }
                     
                  }
                           
               }));
         instructionList.add(
                new BasicInstruction("bne $t1,$t2,label",
                "Branch if not equal : Branch to statement at label's address if $t1 and $t2 are not equal",
            	 BasicInstructionFormat.I_BRANCH_FORMAT,
                "000100 fffff sssss tttttttttttttttt",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int[] operands = statement.getOperands();
                  
                     if (RegisterFile.getValue(operands[0])
                        != RegisterFile.getValue(operands[1]))
                     {
                        Globals.instructionSet.processBranch(operands[2]);
                     }
                  }
               }));
    }
}