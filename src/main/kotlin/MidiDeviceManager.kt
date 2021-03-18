@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
import com.sun.media.sound.SoftReceiver
import com.sun.media.sound.SoftSynthesizer
import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiSystem

object MidiDeviceManager {

    val soft by lazy {
        MidiSystem.getReceiver().also {
            (it as SoftReceiver).midiDevice.open()
        }
//        SoftReceiver(SoftSynthesizer().also {
////            val sf2File = File("/Users/liben/Downloads/FluidR3_GM.sf2")
////            if(sf2File.exists()) {
////                val dsbf = SoftSynthesizer::class.java.getDeclaredField("defaultSoundBank")
////                dsbf.isAccessible = true
////                dsbf.set(null, SF2Soundbank())
////            }
//            it.open()
//        })
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