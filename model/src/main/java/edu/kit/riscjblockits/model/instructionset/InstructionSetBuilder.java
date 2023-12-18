package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class InstructionSetBuilder {
    public static InstructionSetModel buildInstructionSetModel (InputStream is) throws UnsupportedEncodingException {
        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        Gson gson = new GsonBuilder().registerTypeAdapter(MicroInstruction.class, new MicroInstructionsDeserializer()).create();
        InstructionSetModel instructionSet = gson.fromJson(reader, InstructionSetModel.class);
        instructionSet.generateOpcodeHashmap();
        return instructionSet;
    }
}
