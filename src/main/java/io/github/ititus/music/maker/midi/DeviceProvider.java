package io.github.ititus.music.maker.midi;

import javax.sound.midi.*;

public class DeviceProvider {

    public static MidiDevices get() {
        Synthesizer defSynthesizer;
        try {
            defSynthesizer = MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
        Sequencer defSequencer;
        try {
            defSequencer = MidiSystem.getSequencer(false);
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
        Receiver defReceiver;
        try {
            defReceiver = MidiSystem.getReceiver();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
        Transmitter defTransmitter;
        try {
            defTransmitter = MidiSystem.getTransmitter();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            defTransmitter = null;
        }

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
            String name = info.getName();
            String description = info.getDescription();
            String vendor = info.getVendor();
            String version = info.getVersion();

            MidiDevice device;
            try {
                device = MidiSystem.getMidiDevice(info);
            } catch (MidiUnavailableException e) {
                throw new RuntimeException(e);
            }

            int maxReceivers = device.getMaxReceivers();
            int maxTransmitters = device.getMaxTransmitters();
            long msPos = device.getMicrosecondPosition();
            boolean open = device.isOpen();

            System.out.printf("MidiDevice: name=%s, desc=%s, vendor=%s, version=%s, type=%s, isSynthesizer=%b, " +
                    "isSequencer=%b, maxReceivers=%d, maxTransmitters=%d, msPos=%d, open=%b%n", name, description,
                    vendor, version, device.getClass().getSimpleName(), device instanceof Synthesizer,
                    device instanceof Sequencer, maxReceivers, maxTransmitters, msPos, open);
        }

        return new MidiDevices(defTransmitter, defReceiver, defSynthesizer, defSequencer);
    }

    public static final class MidiDevices {
        public final Transmitter transmitter;
        public final Receiver receiver;
        public final Synthesizer synthesizer;
        public final Sequencer sequencer;

        private MidiDevices(Transmitter transmitter, Receiver receiver, Synthesizer synthesizer, Sequencer sequencer) {
            this.transmitter = transmitter;
            this.receiver = receiver;
            this.synthesizer = synthesizer;
            this.sequencer = sequencer;
        }
    }

}
