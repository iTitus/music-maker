package io.github.ititus.music.maker;

import io.github.ititus.music.maker.midi.ChannelPlayer;
import io.github.ititus.music.maker.midi.DeviceProvider;
import io.github.ititus.music.maker.midi.SoundbankLoader;
import io.github.ititus.music.maker.music.Chord;
import io.github.ititus.music.maker.music.Seventh;
import io.github.ititus.music.maker.music.Tone;
import io.github.ititus.music.maker.music.Triad;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import static io.github.ititus.music.maker.music.Note.*;
import static io.github.ititus.music.maker.music.Octave.MIDDLE;

public class Main {

    public static void main(String[] args) {
        DeviceProvider.MidiDevices devices = DeviceProvider.get();
        try (/*Transmitter transmitter = devices.transmitter;*/ Synthesizer synthesizer = devices.synthesizer/*; Sequencer sequencer = devices.sequencer; Receiver receiver = devices.receiver*/) {
            /*if (!sequencer.isOpen()) {
                try {
                    sequencer.open();
                } catch (MidiUnavailableException e) {
                    throw new RuntimeException(e);
                }
            }*/
            if (!synthesizer.isOpen()) {
                try {
                    synthesizer.open();
                } catch (MidiUnavailableException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.printf("latency=%d%n", synthesizer.getLatency());

            SoundbankLoader.load(synthesizer);

            int channelIndex = 0;
            int instrumentIndex = 0;

            MidiChannel[] channels = synthesizer.getChannels();
            MidiChannel channel = channels[channelIndex];

            channel.programChange(instrumentIndex);
            ChannelPlayer piano = new ChannelPlayer(channel);

            //try (Receiver synthReceiver = synthesizer.getReceiver(); TestReceiver testReceiver = new TestReceiver(synthReceiver)) {
                /*if (transmitter != null) {
                    transmitter.setReceiver(testReceiver);
                }*/

                /*Transmitter seqTransmitter;
                try {
                    seqTransmitter = sequencer.getTransmitter();
                } catch (MidiUnavailableException e) {
                    throw new RuntimeException(e);
                }

                File midFile = new File(System.getProperty("user.home") + "/Desktop/When_Mum_Isn't_Home.mid");
                Sequence seq;
                try {
                    seq = MidiSystem.getSequence(midFile);
                } catch (InvalidMidiDataException | IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    sequencer.setSequence(seq);
                } catch (InvalidMidiDataException e) {
                    throw new RuntimeException(e);
                }

                seqTransmitter.setReceiver(synthReceiver);
                // sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
                sequencer.start();
                while (sequencer.isRunning()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                sequencer.stop();*/

            Triad triadT = Triad.get(Tone.get(C, MIDDLE), Triad.Type.MAJOR, Triad.Inversion.ROOT);
            Triad triadS = Triad.get(Tone.get(F, MIDDLE.down()), Triad.Type.MAJOR, Triad.Inversion.SECOND);
            Triad triadSp = Triad.get(Tone.get(D, MIDDLE), Triad.Type.MINOR, Triad.Inversion.ROOT);
            Triad triadD = Triad.get(Tone.get(G, MIDDLE.down()), Triad.Type.MAJOR, Triad.Inversion.SECOND);
            Triad triadD7 = Triad.get(Tone.get(B, MIDDLE.down()), Triad.Type.DIMINISHED, Triad.Inversion.FIRST);
            Seventh seventhD7 = Seventh.get(Tone.get(G, MIDDLE.down()), Seventh.Type.DOMINANT, Seventh.Inversion.SECOND);
            Chord[] cadence = new Chord[] { triadT, triadS, triadD7, triadSp, triadD, triadT };

            System.out.println("start");
            for (Chord chord : cadence) {
                piano.play(chord);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("end");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("success");


                /*while (true) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }*/

            /*} catch (MidiUnavailableException e) {
                throw new RuntimeException(e);
            }*/
        }
    }
}
