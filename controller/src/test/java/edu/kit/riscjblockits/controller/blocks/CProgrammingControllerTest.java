package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import org.junit.jupiter.api.Test;

class CProgrammingControllerTest {

    @Test
    void assemble() throws AssemblyException {
        CProgrammingController cProgrammingController = new CProgrammingController();
        cProgrammingController.assemble("int main() {\n" +
            "    int x = 6;\n" +
            "   x = x + 5; \n" +
            "    return 5;\n" +
            "}\n", null);
        System.out.println("Test passed");
    }


}
