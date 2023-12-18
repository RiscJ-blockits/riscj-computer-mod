package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class MicroInstructionsDeserializer implements JsonDeserializer<MicroInstruction> {

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
     *
     * @param jsonArray MicroInstruction of one of the three possible formats
     * @return MicroInstruction Instance matching the instruction type
     */
    private MicroInstruction parseJsonArray(JsonArray jsonArray) {
        int size = jsonArray.size();
        if(size == 4){
            return parseDataMovementInstruction(jsonArray);
        }
        else if(size == 5) {
            return parseConditionedInstruction(jsonArray);
        }
        else if(size == 6) {
            return parseAluInstruction(jsonArray);
        }
        return null;
    }

    /**
     * Generate DataMovementInstruction from json of form [destination, origin, memory flag, storage operation]
     * @param jsonArray [destination, origin, memory flag, storage operation]
     * @return DataMovementInstruction
     */
    private DataMovementInstruction parseDataMovementInstruction(JsonArray jsonArray) {

        List<JsonElement> elementList = jsonArray.asList();
        String[] from = new String[]{elementList.get(1).toString()};
        String to = elementList.get(0).toString();
        String memoryFlag = elementList.get(2).toString();
        MemoryInstruction memoryInstruction = parseMemoryInstruction(elementList.get(3));


        return new DataMovementInstruction(from, to, memoryFlag, memoryInstruction);
    }

    /**
     * Generate MemoryInstruction from json of form [from, to]
     * @param jsonElement [from, to]
     * @return MemoryInstruction
     */
    private MemoryInstruction parseMemoryInstruction(JsonElement jsonElement) {

        if(jsonElement.isJsonArray()){
            JsonArray asJsonArray = jsonElement.getAsJsonArray();

            String[] from = new String[]{asJsonArray.get(1).toString()};
            String to = asJsonArray.get(0).toString();

            return new MemoryInstruction(from, to);
        }
        return null;
    }

    /**
     * Generate AluInstruction from json of form
     * [operation, alu-dest, alu-origin 1, alu-origin 2, memory flag, storage operation]
     *
     * @param jsonArray [operation, alu-dest, alu-origin 1, alu-origin 2, memory flag, storage operation]
     * @return AluInstruction aus dem Array
     */
    private AluInstruction parseAluInstruction(JsonArray jsonArray) {

        List<JsonElement> elementList = jsonArray.asList();
        String to = elementList.get(1).toString();
        String[] from = new String[]{elementList.get(2).toString(), elementList.get(3).toString()};
        String memoryFlag = elementList.get(4).toString();
        MemoryInstruction memoryInstruction = parseMemoryInstruction(elementList.get(5));
        String action = elementList.get(0).toString();


        return new AluInstruction(from, to, memoryFlag, memoryInstruction, action);
    }

    /**
     * Generate ConditionedInstruction from json of form
     * ["IF", ["[comparator1]", "[comparator2]", "comparing_operation"], [then_from, then_to], "", ""]
     * @param jsonArray ["IF", ["[comparator1]", "[comparator2]", "comparing_operation"], [then_to, then_from], "", ""]
     * @return ConditionedInstruction
     */
    private ConditionedInstruction parseConditionedInstruction(JsonArray jsonArray) {

        List<JsonElement> elementList = jsonArray.asList();
        JsonElement condition =  elementList.get(1);
        if(condition.isJsonArray()) {
            InstructionCondition instructionCondition = parseInstructionCondition(condition.getAsJsonArray());
            JsonElement thenInstruction = elementList.get(2);

            if(!thenInstruction.isJsonArray()) {
                return null;
            }

            JsonArray thenInstructionArray = thenInstruction.getAsJsonArray();
            String thenTo = thenInstructionArray.get(0).toString();
            String[] thenFrom = new String[]{thenInstructionArray.get(1).toString()};
            String memoryFlag = elementList.get(3).toString();
            MemoryInstruction memoryInstruction = parseMemoryInstruction(elementList.get(4));

            return new ConditionedInstruction(thenFrom, thenTo, memoryFlag, memoryInstruction, instructionCondition);
        }
        return null;

    }

    /**
     * Generate InstructionCondition from json of form ["comparator1", "comparator2", "comparing_operation"]
     * @param jsonArray ["comparator1", "comparator2", "comparing_operation"]
     * @return InstructionCondition
     */
    private InstructionCondition parseInstructionCondition(JsonArray jsonArray){

        String comparator1 = jsonArray.get(0).toString();
        String comparator2 = jsonArray.get(1).toString();
        String comparingOperation = jsonArray.get(2).toString();

        return new InstructionCondition(comparingOperation, comparator1, comparator2);
    }
}
