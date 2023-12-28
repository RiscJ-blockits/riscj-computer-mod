package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.data.IDataElement;

public interface IItemChangeNotifiedController {
    abstract void onItemInserted(IDataElement data);

    abstract void onItemRemoved();
}
