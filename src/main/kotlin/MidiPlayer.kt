import java.io.File
import javax.sound.midi.MidiMessage
import javax.sound.midi.MidiSystem
import javax.sound.midi.Receiver
import javax.sound.midi.ShortMessage

class MidiPlayer(val midFile: File) {

    private val receivers = mutableSetOf<Receiver>()
    val seq = MidiSystem.getSequence(midFile)
    val midip by lazy { MidiSystem.getSequencer(false) }

    fun start() {
        if (midip.isRunning) {
            return
        }
        midip.transmitter.receiver = object : Receiver {
            override fun close() {
                receivers.forEach {
                    it.close()
                }
            }

            override fun send(message: MidiMessage?, timeStamp: Long) {
                println("send ${message?.message?.contentToString()} to ${receivers.size}")
                receivers.forEach {
                    it.send(message, timeStamp)
                }
            }
        }
        midip.sequence = seq
        midip.open()
        midip.start()
    }

    fun stop() {
        if (midip.isRunning) midip.stop()
        if (midip.isOpen) midip.close()
    }

    fun addReceiver(receiver: Receiver) {
        receivers += receiver
    }

    fun addReceivers(receiver: Collection<Receiver>) {
        receivers.addAll(receiver)
    }

    fun removeReceiver(receiver: Receiver) {
        receiver.send(ShortMessage(0x90, 0, 0), 0)
        receivers -= receiver
    }

}