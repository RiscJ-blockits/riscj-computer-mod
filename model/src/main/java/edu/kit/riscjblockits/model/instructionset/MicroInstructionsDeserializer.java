package edu.kit.riscjblockits.model.instructionset;


import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Deserializer for MicroInstructions to customize Json parsing to match the instruction set json format.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class MicroInstructionsDeserializer implements JsonDeserializer<MicroInstruction> {

    //ToDo: error handling
    //ToDo: consider alu register checking while parsing alu instructions, but don't know how yet

    /**
     * Deserializes a JsonElement to a MicroInstruction.
     * @param jsonElement JsonElement to be deserialized
     * @param type Type of the JsonElement
     * @param jsonDeserializationContext Context of the deserialization
     * @return MicroInstruction created from the JsonElement
     * @throws JsonParseException if the JsonElement is not in the correct format
     */
    @Override
    public MicroInstruction deserialize(JsonElement jsonElement, Type type,
                                        JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        if(jsonElement.isJsonArray()){
            JsonArray asJsonArray = jsonElement.getAsJsonArray();
            return parseJsonArray(asJsonArray);
        }
        return null;

    }

    /**
     * Parse a JsonArray to a MicroInstruction while distinguishing MicroInstruction types.
     * @param jsonArray MicroInstruction of one of the three possible formats
     * @return MicroInstruction Instance matching the instruction type
     */
    private MicroInstruction parseJsonArray(JsonArray jsonArray) {
        int size = jsonArray.size();
        if(size == 5){
            return parseDataMovementInstruction(jsonArray);
        }
        else if(size == 9) {
            return parseConditionedInstruction(jsonArray);
        }
        else if(size == 7) {
            return parseAluInstruction(jsonArray);
        }
        return null;
    }

    /**
     * Generate DataMovementInstruction from json of form ["[destination]", "[origin]", "[memory_flag]", "[storage operation]"]
     * @param jsonArray ["[destination]", "[origin]", "[memory_flag]", "[storage operation]"]
     * @return DataMovementInstruction
     */
    private DataMovementInstruction parseDataMovementInstruction(JsonArray jsonArray) {

        List<JsonElement> elementList = jsonArray.asList();
        String[] from = new String[]{elementList.get(1).toString()};
        String to = elementList.get(0).toString();
        String memoryFlag = elementList.get(2).toString();
        MemoryInstruction memoryInstruction = parseMemoryInstruction(elementList.get(3).toString(), elementList.get(4).toString(), memoryFlag);


        return new DataMovementInstruction(from, to, memoryFlag, memoryInstruction);
    }

    /**
     * Generate MemoryInstruction from json of form [destination, origin]
     * @param to destination
     * @param from origin
     * @param flag flag
     * @return MemoryInstruction
     */
    private MemoryInstruction parseMemoryInstruction(String to, String from, String flag) {

        return new MemoryInstruction(new String[]{from}, to, flag);
    }

    /**
     * Generate AluInstruction from json of form
     * ["operation", "[alu-dest]", "[alu-origin 1]", "[alu-origin 2]", "[memory flag]", "[storage operation]"]
     *
     * @param jsonArray ["operation", "[alu-dest]", "[alu-origin 1]", "[alu-origin 2]", "[memory flag]", "[storage operation]"]
     * @return AluInstruction aus dem Array
     */
    private AluInstruction parseAluInstruction(JsonArray jsonArray) {

        List<JsonElement> elementList = jsonArray.asList();
        String to = elementList.get(1).toString();
        String[] from = new String[]{elementList.get(2).toString(), elementList.get(3).toString()};
        String memoryFlag = elementList.get(4).toString();
        MemoryInstruction memoryInstruction = parseMemoryInstruction(elementList.get(5).toString(), elementList.get(6).toString(), memoryFlag);
        String action = elementList.get(0).toString();


        return new AluInstruction(from, to, memoryFlag, memoryInstruction, action);
    }

    /**
     * Generate ConditionedInstruction from json of form
     * ["IF", "[comparator1]", "[comparator2]", "comparing_operation", then_to, then_from, "memory_flag", "storage_operation"]
     * @param jsonArray ["IF", "[comparator1]", "[comparator2]", "comparing_operation", then_to, then_from, "memory_flag", "storage_operation"]
     * @return ConditionedInstruction
     */
    private ConditionedInstruction parseConditionedInstruction(JsonArray jsonArray) {

        List<JsonElement> elementList = jsonArray.asList();
        String comparator1 =  elementList.get(1).toString();
        String comparator2 = elementList.get(2).toString();
        String comparingOperation = elementList.get(3).toString();

        InstructionCondition instructionCondition = new InstructionCondition(comparingOperation, comparator1, comparator2);

        String thenTo = elementList.get(4).toString();
        String[] thenFrom = new String[]{elementList.get(5).toString()};
        String memoryFlag = elementList.get(6).toString();
        MemoryInstruction memoryInstruction = parseMemoryInstruction(elementList.get(7).toString(), elementList.get(8).toString(), memoryFlag);

        return new ConditionedInstruction(thenFrom, thenTo, memoryFlag, memoryInstruction, instructionCondition);


    }
}
