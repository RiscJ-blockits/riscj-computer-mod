package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * Builds an InstructionSetModel from a json file.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class InstructionSetBuilder {

    /**
     * Builds an InstructionSetModel from a given InputStream.
     * @param is InputStream of the instruction set
     * @return InstructionSetModel
     * @throws UnsupportedEncodingException if the character encoding is not supported
     */
    public static InstructionSetModel buildInstructionSetModel (InputStream is) throws UnsupportedEncodingException {
        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        Gson gson = new GsonBuilder().registerTypeAdapter(MicroInstruction.class, new MicroInstructionsDeserializer()).create();
        InstructionSetModel instructionSet = gson.fromJson(reader, InstructionSetModel.class);
        instructionSet.generateOpcodeHashmap();
        return instructionSet;
    }

    //Test method
    public static InstructionSetModel buildInstructionSetModelMima() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetMIMA.jsonc");
        try {
            return buildInstructionSetModel(is);
        }  catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    //Test method
    public static InstructionSetModel buildInstructionSetModelRiscV() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetRiscV.jsonc");
        try {
            return buildInstructionSetModel(is);
        }  catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}