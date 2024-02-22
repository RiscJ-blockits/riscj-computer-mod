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
        //use gcc to compile the code to assembly
        String output = "";
        String temp;
        ProcessBuilder processBuilder = new ProcessBuilder();
        //processBuilder.directory(new File("tmp"));
        processBuilder.command("bash", "-c", "echo \"" + code + "\" > test.c");
        processBuilder.command("bash", "-c", "riscv64-unknown-elf-gcc -march=rv32imf -mabi=ilp32 -S test.c -o example.s");
        processBuilder.command("bash", "-c", "cat example.s");
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
        output = sanitizeOutput(output) + "\nebreak\n";
        return super.assemble(output, instructionSetData);
    }

    private String sanitizeOutput(String output) {
        //remove some lines
        return Arrays.stream(output.split("\\r?\\n"))
            .filter(line -> !line.contains(".file"))
            .filter(line -> !line.contains(".ident"))
            .filter(line -> !line.contains(".size"))
            .filter(line -> !line.contains(".attribute"))
            .filter(line -> !line.contains(".align"))
            .filter(line -> !line.contains(".globl"))
            .filter(line -> !line.contains(".type"))
            .filter(line -> !line.contains(".option"))
            .map(line -> line.replaceAll("\t", " ")) //remove tabs
            .collect(Collectors.joining("\n"));
    }

}
