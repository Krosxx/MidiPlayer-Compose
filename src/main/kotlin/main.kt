import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import utils.DragFileHandler
import javax.sound.midi.MidiDevice
import javax.sound.midi.Receiver

val outputDevices = HashMap<String, Receiver>()
var player: MidiPlayer? = null


fun main() = Window(title = "JMidiPlayer-Compose", size = IntSize(300, 300)) {
    val midiDevices = remember { mutableStateListOf<MidiDevice>() }
    val devCount = remember { mutableStateOf(1) }
    val playFile = remember { mutableStateOf("无文件") }
    val playing = remember { mutableStateOf(false) }

    fun refreshMidiDevice() {
        midiDevices.forEach {
            if(it.isOpen) {
                it.close()
            }
        }
        midiDevices.clear()
        midiDevices.addAll(MidiDeviceManager.getOutDevice().also {
            println("devices: ${
                it.joinToString("\n") { it.deviceInfo.toString() }
            }")
        })
    }

    fun initDevice() {
        refreshMidiDevice()
        outputDevices["Soft"] = MidiDeviceManager.soft
        midiDevices.forEach {
            val rev = it.receiver
            it.open()
            outputDevices[it.deviceInfo.name] = rev
        }
    }
    remember { initDevice() }
    MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            Text(playFile.value, modifier = Modifier.align(Alignment.CenterHorizontally))
            Button(modifier = Modifier.align(Alignment.End),
                onClick = {
                    player ?: return@Button
                    if (!playing.value) {
                        player?.start()
                    } else {
                        player?.stop()
                    }
                    playing.value = !playing.value
                }) {
                Text(if (playing.value) "Stop" else "Start")
            }
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = ::refreshMidiDevice
            ) {
                Text("Refresh Device")
            }

            List(devCount.value) {
                newDeviceBox("Soft", null)

                for (d in midiDevices) {
                    newDeviceBox(d.deviceInfo.name, d)
                }
            }
        }
    }
    LocalAppWindow.current.window.dropTarget = DragFileHandler {
        println(it)
        player?.stop()
        playing.value = false
        playFile.value = it.name
        player = MidiPlayer(it)
        player?.addReceivers(outputDevices.values)
    }
}


@Composable
fun newDeviceBox(name: String, device: MidiDevice?, c: Boolean = true) {
    val a = remember { mutableStateOf(c) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(a.value, onCheckedChange = {
            a.value = it
            if (it) {
                if (device == null) {
                    kotlin.runCatching {
                        outputDevices[name] = MidiDeviceManager.soft
                        player?.addReceiver(MidiDeviceManager.soft)
                    }.onFailure { e ->
                        e.printStackTrace()
                    }
                } else {
                    runCatching {
                        device.open()
                        val rev = device.receiver
                        outputDevices[name] = rev
                        player?.addReceiver(rev)
                    }.onFailure { e ->
                        e.printStackTrace()
                    }
                }
            } else {
                if (device == null) {
                    player?.removeReceiver(MidiDeviceManager.soft)
                } else {
                    player?.removeReceiver(outputDevices[name]!!)
                    device.close()
                }
                outputDevices -= name
            }
        })
        Text(name)
    }
}
