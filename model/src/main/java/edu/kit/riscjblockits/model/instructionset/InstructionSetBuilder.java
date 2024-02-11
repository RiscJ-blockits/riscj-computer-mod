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
public class InstructionSetBuilder {

    /**
     * Builds an InstructionSetModel from a given InputStream.
     * @param is InputStream of the instruction set
     * @return InstructionSetModel
     * @throws UnsupportedEncodingException if the character encoding is not supported
     */
    public static InstructionSetModel buildInstructionSetModel (InputStream is) throws UnsupportedEncodingException, InstructionBuildException {
        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        Gson gson = new GsonBuilder().registerTypeAdapter(MicroInstruction.class, new MicroInstructionsDeserializer()).create();
        InstructionSetModel instructionSet;
        try {
            instructionSet = gson.fromJson(reader, InstructionSetModel.class);
        } catch (JsonSyntaxException e) {
            throw new InstructionBuildException("Error while parsing the instruction set: " + e.getMessage());
        }
        instructionSet.generateOpcodeHashmap();
        return instructionSet;
    }

    /**
     * Builds an InstructionSetModel from a given String.
     * @param s String
     * @return InstructionSetModel
     * @throws UnsupportedEncodingException if the character encoding is not supported
     */
    public static InstructionSetModel buildInstructionSetModel (String s) throws UnsupportedEncodingException, InstructionBuildException {
        InputStream stream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        return buildInstructionSetModel(stream);
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
