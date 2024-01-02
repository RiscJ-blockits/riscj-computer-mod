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

    public static InstructionSetModel buildInstructionSetModelMima() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetMIMA.jsonc");
        try {
            return buildInstructionSetModel(is);
        }  catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static InstructionSetModel buildInstructionSetModelRiscV() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetRiscV.jsonc");
        try {
            return buildInstructionSetModel(is);
        }  catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
