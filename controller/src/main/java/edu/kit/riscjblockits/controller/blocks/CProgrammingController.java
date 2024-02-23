package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.model.data.IDataElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CProgrammingController extends ProgrammingController {

    @Override
    public IDataElement assemble(String code, IDataElement instructionSetData) throws AssemblyException {
        return super.assemble(translateC(code), instructionSetData);
    }

    public String translateC(String code) throws AssemblyException {
        //use gcc to compile the code to assembly
        String output = "";
        String temp;
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c","echo \"" + code + "\" | riscv64-unknown-elf-gcc -march=rv32imf -mabi=ilp32 -S -xc - -o-");
        processBuilder.redirectErrorStream(true);
        Process process;
        try {
            process = processBuilder.start();
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((temp = buf.readLine()) != null) {
                output = output + temp + "\n";
            }
        } catch (IOException e) {
            throw new AssemblyException("gcc could not compile the code: " + e.getMessage());
        }
        //sanitize the output
        return sanitizeOutput(output) + "\nebreak\n";
    }

    private String sanitizeOutput(String output) {
        //remove some lines
        String code = Arrays.stream(output.split("\\r?\\n"))
            .filter(line -> !line.contains(".file"))
            .filter(line -> !line.contains(".ident"))
            .filter(line -> !line.contains(".size"))
            .filter(line -> !line.contains(".attribute"))
            .filter(line -> !line.contains(".align"))
            .filter(line -> !line.contains(".globl"))
            .filter(line -> !line.contains(".type"))
            .filter(line -> !line.contains(".option"))
            .filter(line -> !line.contains("nop")) //we don't need nops
            .map(line -> line.replaceAll("\t", " ")) //remove tabs
            .map(line -> line.replaceAll(",", ", ")) //add spaces for assembler
            .collect(Collectors.joining("\n"));
        //replace pseudo instructions
        for (String line : code.split("\n")) {
            if (line.matches("\\s*li \\w*, \\w*\\s*")) { //replace out li pseudo instructions
                line = line.trim();
                String number = line.split(" ")[2];
                String register = line.split(" ")[1].replace(",", "");
                code = code.replace(line, "addi " + register + ", zero, " + number);
            } else if (line.matches("\\s*mv \\w*, \\w*\\s*")) { //replace out mv pseudo instructions
                line = line.trim();
                String source = line.split(" ")[2];
                String destination = line.split(" ")[1].replace(",", "");
                code = code.replace(line, "addi " + destination + ", " + source + ", 0");
            } else if (line.matches("\\s*jr \\w*\\s*")) { //replace out jr pseudo instructions
                line = line.trim();
                String register = line.split(" ")[1].replace(",", "");
                //code = code.replace(line, "jalr zero, " + register);  //FixME
                code = code.replace(line, "");
            }
        }
        return code;
    }

}
