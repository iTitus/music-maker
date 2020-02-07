package io.github.ititus.music.maker.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class TestReceiver implements Receiver {

    private final Receiver receiver;

    public TestReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (receiver != null) {
            // receiver.send(message, timeStamp);
            receiver.send(message, -1);
        }

        if (!(message instanceof ShortMessage)) {
            throw new IllegalArgumentException(String.valueOf(message));
        }
        ShortMessage msg = (ShortMessage) message;

        System.out.printf("[%d] channel=%d, cmd=%d, data1=%d, data2=%d%n", timeStamp, msg.getChannel(), msg.getCommand(), msg.getData1(), msg.getData2());
        /*if (msg.getCommand() == ShortMessage.NOTE_ON) {
            channel.noteOn(Tone.get(Note.C, Octave.MIDDLE).getMidiIndex(), 127);
        }*/
    }

    @Override
    public void close() {
        if (receiver != null) {
            receiver.close();
        }
    }
}
