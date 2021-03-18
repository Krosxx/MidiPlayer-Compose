package utils

import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.io.File

class DragFileHandler(val onFile: (File) -> Unit) : DropTarget() {
    @Suppress("UNCHECKED_CAST")
    override fun drop(evt: DropTargetDropEvent) {
        kotlin.runCatching {
            evt.acceptDrop(DnDConstants.ACTION_REFERENCE)

            val dropFiles = (evt.transferable.getTransferData(DataFlavor.javaFileListFlavor) as? List<File>)?.filter {
                it.isFile
            } ?: return

            if (dropFiles.isNotEmpty()) {
                onFile(dropFiles[0])
            }
        }.onFailure {
            it.printStackTrace()
        }

    }
}