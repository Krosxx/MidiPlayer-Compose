@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
import com.sun.media.sound.SoftReceiver
import com.sun.media.sound.SoftSynthesizer
import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiSystem

object MidiDeviceManager {

    val soft by lazy {
        SoftReceiver(SoftSynthesizer().also {
            it.open()
        })
    }

    fun getDeviceList(): Array<MidiDevice.Info> {
        return MidiSystem.getMidiDeviceInfo()
    }

    fun getOutDevice(): List<MidiDevice> {
        return getDeviceList().map {
            MidiSystem.getMidiDevice(it)
        }.filter { device ->
            device.deviceInfo.name != "Unknown name" &&
                    device.toString().contains("MidiOutDevice")
        }.also{
            println(it.joinToString())
        }
    }

}