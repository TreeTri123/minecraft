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
               System.out.println("Switching hotbar slots");          
               }
         }));
      instructionList.add(
         new BasicInstruction("sr imm", 
         "Scroll right on your hotbar a # of times",
         BasicInstructionFormat.I_FORMAT,
         "000101 00000 00000 0000000000000000",
         new SimulationCode()
         {
            public void simulate(ProgramStatement statement) throws ProcessingException
            {
               int[] operands = statement.getOperands();
               int ticks = operands[0]; // immediate
               System.out.println("Scrolling right on hotbar " + ticks + " times");         
               }
         }));
      instructionList.add(
         new BasicInstruction("sl imm", 
         "Scroll left on your hotbar a # of times",
         BasicInstructionFormat.I_FORMAT,
         "000110 00000 00000 0000000000000000",
         new SimulationCode()
         {
            public void simulate(ProgramStatement statement) throws ProcessingException
            {
               int[] operands = statement.getOperands();
               int ticks = operands[0]; // immediate
               System.out.println("Scrolling left on hotbar " + ticks + " times");         
               }
         }));
         //CHECK
      instructionList.add(
         new BasicInstruction("clear", 
         "Clear all items from your inventory",
         BasicInstructionFormat.R_FORMAT,
         "000111 00000 00000 0000000000000000",
         new SimulationCode()
         {
            public void simulate(ProgramStatement statement) throws ProcessingException
            {
               int[] hotbarRegs = {9, 10, 11, 12, 13, 14, 15, 24, 25};
               for (int reg : hotbarRegs) {
                  RegisterFile.updateRegister(reg,0);
               }
            }
         }));
      instructionList.add(
         new BasicInstruction("xp imm", 
         "Give # amount of experience levels to oneself",
         BasicInstructionFormat.I_FORMAT,
         "001000 00000 00000 0000000000000000",
         new SimulationCode()
         {
            public void simulate(ProgramStatement statement) throws ProcessingException
            {
               int[] operands = statement.getOperands();
               int levels = operands[0]; // immediate
               System.out.println("Giving " + levels + " levels to player");         
               }
         }));
      instructionList.add(
         new BasicInstruction("incspd imm", 
         "increase speed of player by specified amplifier",
         BasicInstructionFormat.I_FORMAT,
         "001001 00000 00000 0000000000000000",
         new SimulationCode()
         {
            public void simulate(ProgramStatement statement) throws ProcessingException
            {
               int[] operands = statement.getOperands();
               int amp = operands[0]; // immediate
               System.out.println("Increasing speed by amplifier of " + amp);         
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
         new BasicInstruction("run imm",
         "runs forward for imm amount of time (press w and control)",
         BasicInstructionFormat.J_FORMAT,
         "100011 00000 00000 0000000000000000",
         new SimulationCode()
            {
               public void simulate(ProgramStatement statement) throws ProcessingException
               {
                  int[] operands = statement.getOperands();
                  int time = operands[0]; // immediate
                  System.out.println("Running forward for " + time + " seconds.");
               }
         }));
      instructionList.add(
         new BasicInstruction("eat imm",
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
         new BasicInstruction("interact",
         "interact with an object one time",
         BasicInstructionFormat.J_FORMAT,
         "100111 00000 00000 0000000000000000",
         new SimulationCode() 
            {
               public void simulate(ProgramStatement statement) throws ProcessingException
               {
                  System.out.println("Interacting with object.");
               }
         }));
      instructionList.add(
         new BasicInstruction("clmb imm",
         "goes up ladder for imm amount of seconds",
         BasicInstructionFormat.J_FORMAT,
         "101000 00000 00000 0000000000000000",
         new SimulationCode()
            {
               public void simulate(ProgramStatement statement) throws ProcessingException
               {
                  int[] operands = statement.getOperands();
                  int time = operands[0]; // immediate
                  System.out.println("Climbing ladder for " + time + " seconds.");
               }
         }));
      instructionList.add(
         new BasicInstruction("atck imm",
         "attacks entity in front of player for imm amount of clicks",
         BasicInstructionFormat.J_FORMAT,
         "101001 00000 00000 0000000000000000",
         new SimulationCode()
            {
               public void simulate(ProgramStatement statement) throws ProcessingException
               {
                  int[] operands = statement.getOperands();
                  int time = operands[0]; // immediate
                  System.out.println("Attacking entity for " + time + " clicks.");
                  }
         }));
      instructionList.add(
         new BasicInstruction("turn imm",
            "rotate the player's view by imm of degrees",
            BasicInstructionFormat.J_FORMAT,
            "101010 00000 00000 0000000000000000",
            new SimulationCode() 
            {
               public void simulate(ProgramStatement statement) throws ProcessingException
               {
                  int[] operands = statement.getOperands();
                  int degrees = operands[0]; // immediate
                  System.out.println("Turning player view by " + degrees + " degrees.");
               }
         }));
    }
}