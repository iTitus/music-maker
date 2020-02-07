package io.github.ititus.music.maker.midi;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class SoundbankLoader {

    public static void load(Synthesizer synthesizer) {
        File soundbankFile = new File("C:/Program Files/VideoLAN/FluidR3_GM.SF2");
        Soundbank soundbank;
        try {
            soundbank = MidiSystem.getSoundbank(soundbankFile);
        } catch (InvalidMidiDataException | IOException e) {
            throw new RuntimeException(e);
        }
        synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
        synthesizer.loadAllInstruments(soundbank);

        System.out.printf("Soundbank: name=%s, desc=%s, vendor=%s, version=%s%n", soundbank.getName(), soundbank.getDescription(), soundbank.getVendor(), soundbank.getVersion());
        Instrument[] instruments = soundbank.getInstruments();
        System.out.println("Instruments:");
        for (int i = 0; i < instruments.length; i++) {
            Instrument ins = instruments[i];
            System.out.printf("Instrument: index=%d, name=%s, str=%s%n", i, ins.getName(), ins.toString());
        }
    }
}
