package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Builds an InstructionSetModel from a json file.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public final class InstructionSetBuilder {

    private InstructionSetBuilder() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * Builds an InstructionSetModel from a given InputStream.
     * @param is InputStream of the instruction set
     * @return InstructionSetModel
     * @throws InstructionBuildException if the instruction set cannot be built.
     */
    public static InstructionSetModel buildInstructionSetModel (InputStream is) throws InstructionBuildException {
        Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        Gson gson = new GsonBuilder().registerTypeAdapter(MicroInstruction.class, new MicroInstructionsDeserializer()).create();
        InstructionSetModel instructionSet;
        try {
            instructionSet = gson.fromJson(reader, InstructionSetModel.class);
        } catch (JsonSyntaxException e) {
            throw new InstructionBuildException("Error while parsing the instruction set: " + e.getMessage());
        }
        if(instructionSet == null) {
            throw new InstructionBuildException("Error while parsing the instruction set: The instruction set is empty.");
        }
        instructionSet.generateHashMaps();
        return instructionSet;
    }

    /**
     * Builds an InstructionSetModel from a given String.
     * @param s String
     * @return InstructionSetModel
     * @throws InstructionBuildException if the instruction set cannot be built.
     */
    public static InstructionSetModel buildInstructionSetModel (String s) throws InstructionBuildException {
        InputStream stream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        return buildInstructionSetModel(stream);
    }

}
