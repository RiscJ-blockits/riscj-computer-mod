# riscjblockits

If you find issues or have questions, feel free to join our [discord server](https://discord.gg/pwSuvEJBfC) or report the issue to our [GitHub](https://github.com/RiscJ-blockits/riscj-computer-mod/issues).

## Build:
1) import this project
2) wait for setup to finish
3) run Gradle Task fabric/genSources for MC setup (takes a while)
4) use Gradle Task fabric/runClient to test
(https://fabricmc.net/wiki/tutorial:setup)

## Features:
### Introduction
This mod is designed to provide a hands-on understanding of computer architecture. It allows you to construct a virtual computer within the Minecraft universe using specific blocks that represent different components of a computer system. These components include registers, a control unit, an arithmetic logic unit (ALU), and memory. 
 By placing these blocks onto the same bus, you can create a fully functional computer simulation. This setup mirrors a simplified structure of a real-world computer, where different components communicate and interact through a shared bus system.

 Programming and Execution \
 Once your computer is built, you can bring it to life using a program assembled in the programming block. This feature introduces the concept of assembly language and how instructions are executed in a computer system.
 You can write programs, load them into your computer, and watch as your instructions are carried out in the Minecraft world. This interactive experience provides a tangible understanding of how software and hardware interact in a computer system.

 Learning and Fun \
 Whether you’re a computer science student looking for a fun way to supplement your studies, a teacher seeking an engaging educational tool, or a Minecraft enthusiast interested in computers, this mod offers a unique blend of learning and gameplay.

We hope this mod will spark your curiosity and deepen your understanding of computer systems. Enjoy building, programming, and exploring the world of computer architecture in Minecraft!

 Please refer to the subsequent sections of this Readme
 for detailed instructions on how to use each component and assemble your computer. Happy crafting!,

### Programming a Computer
Programming Your Virtual Computer in Minecraft \
Welcome to the programming section, where you'll learn how to breathe life into your MIMA/RISCV computer simulation by writing and executing programs in Minecraft.

Understanding Programming in Minecraft \
In our mod, programming is the process of creating a sequence of instructions that your virtual computer will execute. These instructions are written in assembly language, providing a simplified representation of machine code. Through the programming block, you can input your code and witness the dynamic execution of your program in the Minecraft world.

Writing Programs \
To write a program, interact with the programming block's GUI. Here, you can input assembly code, defining the steps your virtual computer will take. As you craft your programs, consider the instruction set architecture chosen in the control unit, as it influences the commands available for programming.

Executing Programs \
Once your program is written, load it into the computer through the programming block. The control unit interprets the instructions, and the bus facilitates data flow between components, ensuring the program's execution. Witness your virtual computer come to life as it follows the programmed steps, providing a tangible connection between your code and the Minecraft world.

Experiment, iterate, and have fun exploring the intersection of coding and gameplay. Use programming to unlock the full potential of your virtual computer in Minecraft!

Please refer to the subsequent sections of this Readme
 for detailed instructions on writing programs, loading them into the computer, and troubleshooting common programming issues.

### The Instruction Set
The Instruction Set: Defining Computer Commands \
Welcome to the instruction set section, where you’ll learn about specifying the different types of commands available for programming your virtual computer in Minecraft.

Understanding the Instruction Set \
In computer architecture, the instruction set is a specification a group of commands that a CPU can understand and execute. These commands are the building blocks of programs, allowing the CPU to perform operations such as arithmetic, logic, data transfer, and control flow. In our Minecraft mod, you can choose between two instruction sets: MIMA and RISC-V. You can also manipulate these and define your own instruction set.

MIMA Instruction Set \
The MIMA instruction set is based on the Minimal Machine architecture, a simplified model of a computer system. It includes commands such as ADD, SUB, LDC, STV, and JMP, among others. For a full list of the instructions, insert the instruction set item in the programming block and click the ?-Symbol. The MIMA instruction set provides a straightforward introduction to computer architecture, making it an ideal starting point for learning about programming and execution in Minecraft.

RISC-V Instruction Set \
The RISC-V instruction set is based on the RISC-V architecture, an open standard instruction set architecture based on established principles of computer design. It includes commands such as ADD, SUB, AND, OR, and JAL, among others. For a full list of all instructions, insert the instruction set item into the programming block and click the ?-Symbol. The RISC-V instruction set offers a more comprehensive set of commands, providing a deeper understanding of computer architecture and programming in Minecraft.

Selecting an Instruction Set \
To choose an instruction set for your computer, insert the item in the control unit block. Experiment with different instruction sets to understand how they impact the capabilities of your virtual computer.

#### Defining Your Own Instruction Set \
You can also define your own instruction set by modifying the MIMA or RISC-V instruction set items. To do so, right click while holding the item in your hand. This feature allows you to create custom commands and experiment with different programming paradigms in Minecraft.
To write your own instruction set, please adhere to the following guidelines:

- Each instruction should have a unique name.
- The .json structure should include the sections name, instruction_length, registers, memory, alu_operations, fetch, address_change, program_start_label, data_storage_keywords, and instructions.
- The instruction_length section should specify the length of the instruction in bits.
- The registers section should include the subsections program_counter, alu, float, integer, and initial_values.
- The memory section should include the subsections word_length, address_length, access_delay, byte_order, possible_opcode_lengths, and opcode_position.
- The alu_operations section should include a list of available ALU operations.
- The fetch section should include a list of fetch commands.
- The address_change section should include a list of address change command regexes.
- The program_start_label section should include the label of the start of the program.
- The data_storage_keywords section should include a list of data storage keyword regexes.
- The Values of these regexes should contain `[your-group-name]<value-length-in-bytes>´ to indicate what the value is supposed to be parsed to. A plus can be added at the end to indicate that the regex value should be viewed as ascii characters and therefore repeated.
- The instructions section should include a list of available instructions, each with a unique name and the subsections arguments, opcode, execution, and translation.
- The arguments subsection should include a list of arguments for the instruction.
- The opcode subsection should include the opcode for the instruction.
- The execution subsection should include a list of execution commands for the instruction.
- The translation subsection should include the scheme for binary translation of the instruction.
- For a detailed example, refer to the sample instruction set below.

The so-called microinstructions used in the execution section of the instructions and in the fetch phase are defined as follows ("[]" indicates that there is an argument to be inserted in that field, "&[]" indicates that the argument is to be treated as a register, "&f[]" means a float register, and "<mem_vis>" means that this is only for visualization of memory access and does not perform any data movement):
- Data Transfer: `json ["[destination]", "[origin]", "[memory_flag]", Storage-Operation] `
- ALU Operation: `json ["[operation]", "[alu-dest]", "[alu-origin 1]", "[alu-origin 2]", "[memory flag]", Storage-Operation] `
- Conditional Operation: `json ["IF", "[comparator1]", "[comparator2]", "[comparing_operation]", "[destination]", "[origin]", "[memory_flag]", Storage-Operation] `
- Storage Operation: `json ["[destination]", "[origin]"] `

Once you’ve defined your instruction set, insert the item in the control unit block to apply the changes to your virtual computer. Experiment with different instruction sets to understand how they impact the capabilities of your virtual computer.

#### Sample Instruction Set
```json
{
  "name": "MIMA",             // declare name
  "instruction_length": 24,   // declade instruction length

  "registers": {                  // define existing (required) registers
    "program_counter": "IAR",   // register containing the current program address
    "alu": [                    // alu specific registers
      "X",
      "Y",
      "Z"
    ],
    "float": {},            // float registers
    "integer": {            // 
      "IR": 1,
      "EINS": 2,
      "AKKU": 3,
      "SAR": 4,
      "SDR": 5
    },
    "initial_values": {
      "EINS": "01"
    }
  },
  "memory": {
    "word_length": 24,
    "address_length": 20,
    "access_delay": 3,
    "byte_order": "le",
    "possible_opcode_lengths": [4, 8],
    "opcode_position": "MOST"
  },
  "alu_operations": [
    "None",
    "ADD",
    "RR",
    "AND",
    "OR",
    "XOR",
    "NEG",
    "JMN"
  ],
  "fetch": [
    ["SAR", "IAR", "r", "<mem_vis>", "SAR"],
    ["X", "IAR", "r" ,"" ,""],
    ["Y", "EINS", "r", "", ""],
    ["ADD", "Z", "X", "Y" ,"", "", ""],
    ["IAR", "Z", "r", "SDR", "Mem[SAR]"],
    ["IR", "SDR", "", "", ""]
  ],
  "address_change": {
    " *\\* *= *(?<address>\\w+) *": "[address]"
  },
  "program_start_label":
  "START",
  "data_storage_keywords": {
    " *DS * (?<value>(?:(?:0x)|(?:0b))?\\d+)": "[value]<24>"
  },
  "instructions": {
    "LDC": {                    // load constant
      "arguments": ["[const]"],
      "opcode": "0000",
      "execution": [
        ["AKKU", "[const]", "", "", ""]     // set AKKU to contents of constant
      ],
      "translation": [
        "0000",                     // op-code
        "[const]<20>"               // 20 bit constant
      ]

    },
    "ADD": {
      "arguments": ["[addr]"],
      "opcode": "0011",
      "execution": [
        ["SAR", "[addr]", "r", "<mem_vis>", "SAR"],
        // to     from memoryAction [to         from]
        ["X", "AKKU", "r", "", ""],
        ["", "", "r", "SDR", "Mem[SAR]"],
        ["Y", "SDR", "", "", ""],
        ["ADD", "Z", "X", "Y", "", "", ""],
        //alu-op to   x   y  memAction
        ["AKKU", "Z", "", "", ""]
      ],
      "translation": [
        "0011",
        "[addr]<20>"
      ]
    },
    "LDV": {
      "arguments": ["[addr]"],
      "opcode": "0001",
      "execution": [
        ["SAR", "[addr]", "r", "<mem_vis>", "SAR"],
        ["", "", "r", "", ""] ,
        ["", "", "r", "SDR", "Mem[SAR]"],
        ["AKKU", "SDR", "", "", ""]
      ],
      "translation": [
        "0001",
        "[addr]<20>"
      ]
    },
    "HALT": {
      "arguments": [],
      "opcode": "11110000",
      "execution": [
        ["PAUSE", "", "", "", "", "", ""]
      ],
      "translation": [
        "11110000",
        "0000000000000000"
      ]
    },
    "EQL": {
      "arguments": ["[addr]"],
      "opcode": "0111",
      "execution": [
        ["SAR", "[addr]", "r", "<mem_vis>", "SAR"],
        ["X", "AKKU", "r", "", ""],
        ["", "", "r", "SDR", "Mem[SAR]"],
        ["Y", "SDR", "", "", ""],
        ["EQ", "Z", "X", "Y", "" ,"", ""],
        ["AKKU", "Z", "", "", ""]
      ],
      "translation": [
        "0111",
        "[addr]<20>"
      ]
    },
    "JMN": {
      "arguments": ["[addr]"],
      "opcode": "1001",
      "execution": [
        ["X", "AKKU", "", "", ""],
        ["Y", "[addr]", "", "", ""],
        ["IF", "X", "0", "<", "IAR", "[addr]", "", "", ""]
      ],
      "translation": [
        "1001",
        "[addr]<20>"
      ]
    }
  }
}
```
### Control Unit
The Control Unit: Heart of Your Virtual Computer \
 Welcome to the control unit, the central hub of your MIMA/RISCV computer simulation in Minecraft. This is where you’ll set the instruction set for your computer and determine what components are needed to complete your build.

 Understanding the Control Unit \
 The control unit is the part of the computer that controls the operation of the processor. It directs the flow of data between the CPU and other devices. In our Minecraft mod, the control unit block serves a similar function. It’s where you set the instruction set architecture for your computer, such as MIMA or RISC-V.

 Setting the Instruction Set \
 The instruction set is a group of commands that the CPU can understand and execute. Different types of computers use different instruction sets. In our mod, you can choose between the MIMA or RISC-V instruction sets. Once you’ve set the instruction set in the control unit, your virtual computer will be able to execute instructions in that format.

 Building a Complete Computer \
 Once you’ve set your instruction set, the control unit will help you identify what parts are missing for a complete computer. It will guide you in placing the necessary blocks like registers, ALU, and memory onto the same bus.

 Remember, building a computer is just the beginning. Once your system is complete, you can start programming and see your instructions come to life in the Minecraft world. Enjoy the journey of exploration and discovery!

 Please refer to the subsequent sections of this Readme
 for detailed instructions on how to use the other components.

### Register
The Register Block: Storing and Viewing Data \
Welcome to the register block, a crucial component of your MIMA/RISCV computer simulation in Minecraft. This is where you’ll set the register type and view the stored value.

Understanding the Register Block \
In computer architecture, a register is a small amount of storage available as part of a CPU or other digital processor. These registers are used to quickly accept, store, and transfer data and instructions that are being used immediately by the CPU. In our Minecraft mod, the register block serves a similar function.

Setting the Register Type \
The register block allows you to set the register type. Different types of registers include the accumulator, program counter, instruction register, memory address register, and general-purpose registers, among others. Each type of register has a specific role in the functioning of a computer. The available register types are defined by the instruction set inserted in the control unit on the same bus as the register. To set the register type, open the GUI and click on the book icon. There, click on a register type with a red X to select and again to deselect.

Viewing Register Values \
Once you’ve set the register type, you can view the value stored in the register. This feature provides a real-time insight into the workings of your virtual computer. As you execute programs, you can see how data moves through your system and how different components interact.

### Arithmetic Logic Unit (ALU)
The Arithmetic Logic Unit (ALU): Powering Computations \
Welcome to the ALU, a critical component of your MIMA/RISCV computer simulation in Minecraft. The ALU is responsible for performing arithmetic and logic operations, making it the powerhouse of your virtual computer.

Understanding the ALU \
In computer architecture, the ALU is a key element of the CPU that executes arithmetic and logic operations on data. In our Minecraft mod, the ALU block emulates this functionality. It performs operations such as addition, subtraction, AND, OR, and more, enabling your virtual computer to process and manipulate data.

Integration with Other Components \
The ALU operates on the ALU registers, collecting input values from two input registers and writing the result in an output register. The ALU registers have to be placed next to the ALU Block.

### Bus
The Bus: Enabling Communication Between Components \
Welcome to the bus, the communication backbone of your MIMA/RISCV computer simulation in Minecraft. The bus manages data transfer between different components of your virtual computer, allowing them to work together.

Understanding the Bus \
In computer architecture, a bus is a communication system that transfers data between components. Similarly, in our Minecraft mod, the bus block serves as the medium through which registers, the control unit, ALU, and memory exchange information. Placing all components on the same bus is crucial for the proper functioning of your virtual computer.

Connecting Components \
To connect components to the bus, simply place them adjacent to it. This establishes a communication link, enabling data transfer.

### Memory
The Memory Block: Storing and Retrieving Data \
Welcome to the memory block, an essential component of your MIMA/RISCV computer simulation in Minecraft. The memory block allows your virtual computer to store and retrieve data, providing a crucial element for program execution.

Understanding the Memory \
In computer systems, memory is used to store data and instructions. The memory block in our Minecraft mod emulates this functionality, offering a space where your virtual computer can save and access information during program execution.

Writing a Program into Memory \
To write a program in your computer's memory, insert a program item on which you have written a programm into the memory block. This will store the program in the memory, making it available for execution by the control unit.

Looking into Memory \
To view the contents of the memory, open the GUI of the memory block. This will display the stored data, providing a real-time insight into the workings of your virtual computer.

### System Clock
The System Clock: Synchronizing Computer Operations \
Welcome to the system clock, a vital component of your MIMA/RISCV computer simulation in Minecraft. The system clock enables the execution of your virtual computer’s operations tick by tick.

Understanding the System Clock \
In computer systems, the system clock is a signal used to synchronize the operation of different components. In our Minecraft mod, the system clock block emulates this functionality. It provides a pulse that triggers the execution of instructions, ensuring that different components of your virtual computer work together seamlessly.

Integrating the System Clock \
To use the system clock, connect it to the control unit and the other components of your virtual computer via a common bus system. To be able to use the step and fast mode of the system clock, attach a trigger of a short redstone signal to the system clock, e.g. by putting a button on or next to it.

Setting the Clock Mode and Speed \
The system clock provides three clock modes - step, tick and fast mode. \
To set it to step mode, put the needle in the system clock GUI to the leftmost position. In step mode, you need to trigger every tick manually by a redstone signal through e.g. the button. \
To set the system clock to fast mode, put the needle in the system clock GUI to the rightmost position. In fast mode, the system clock will trigger every tick automatically as fast as it can. You need to start fast mode by a redstone signal through e.g. pushing the button once. \
To set the system clock to tick mode, use any other needle position depending on the speed you want. In tick mode, the system clock will trigger every tick automatically in reference to the Minecraft game tick.

### Engineering Googles
The Engineering Googles: Visualizing Computer Components \
Welcome to the engineering googles, a powerful tool for visualizing and interacting with the components of your MIMA/RISCV computer simulation in Minecraft. The engineering googles allow you to see the type of the block you are looking at and the data it contains.

Using the Engineering Googles \
To use the engineering googles, simply wear them by opening the inventory and dragging them into the helmet slot and look at the blocks in your virtual computer. The googles will display the type of each block and the data it contains, providing a visual representation of your computer’s operation. This feature is especially useful for debugging and understanding how different components interact.

### Redstone Input and Output
The Redstone Input and Output Blocks: Interfacing with the Minecraft World \
Welcome to the redstone input and output blocks, which allow your MIMA/RISCV computer simulation to interact with the Minecraft world. These blocks enable you to input and output redstone signals, providing a bridge between your virtual computer and the Minecraft environment.

Understanding Redstone Input and Output \
In Minecraft, redstone is a material that can transmit power and signals. The redstone input and output blocks in our mod allow your virtual computer to interact with the Minecraft world by sending and receiving redstone signals.

Inputting and Outputting Signals \
To input a redstone signal, connect a redstone input block to a lever, button, or other redstone source. The redstone signal valueing between 0 and 15 will be written to the redstone input register and can then be used as an input to your virtual computer, influencing its operations. To output a redstone signal, connect a redstone output block to a redstone device such as a lamp, door, or piston. Your virtual computer can control these devices by sending redstone signals to them by setting values between 0 and 15. Values outside of that range will set the value to 15.

### Quantum State Register
The Quantum State Register: Harnessing Quantum Computing \
Welcome to the quantum state register, an advanced component of your MIMA/RISCV computer simulation in Minecraft. The quantum state register block allows you to quantum entangle a computer register with the quantum state register over any distance when both blocks are loaded by Minecraft.

Connecting the Quantum State Register \
To connect a Quantum State Register to your computer, use a Quantum State Register Block for the register inside your computer which you want to entangle. Then while holding another Quantum Entanglement Block in your hand, right click on the Quantum Entanglement Register in your computer. The registers are now connected and you can place the register from your hand anywhere in the world. You can set the register types independently and you can include both registers in different computers to let them interact.

### Terminal
The Terminal Block: Displaying Output from and Receiving Input To Your Virtual Computer \
Welcome to the terminal block, a powerful component of your MIMA/RISCV computer simulation in Minecraft. The terminal block allows you to view output from your virtual computer and input ascii text.

Understanding the Terminal Block \
In computer systems, a terminal is a device that allows users to interact with a computer and view its output. In our Minecraft mod, the terminal block emulates this functionality. It displays output from your virtual computer and enables you to input ascii text.

Viewing Program Output \
To use the terminal block, connect it to the bus of your virtual computer like any other register and set its type. As you execute programs and write in the register, the terminal will display output appending it to the end of the current display string.

Providing Input to your Computer \
As you type input to the terminal and click Enter, the ascii text will be written into the register and can then be used as an input for your program. There is no wait functionality for input so you either have to write before starting the simulation or you need to implement a value check yourself.

}
