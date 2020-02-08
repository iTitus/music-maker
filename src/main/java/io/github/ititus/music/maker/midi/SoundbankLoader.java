package io.github.ititus.music.maker.midi;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class SoundbankLoader {

    public static void load(Synthesizer synthesizer, boolean verbose) {
        // File soundbankFile = new File("C:/Program Files/VideoLAN/FluidR3_GM.sf2");
        File soundbankFile = new File("C:/Program Files/VideoLAN/SGM-V2.01.sf2");
        Soundbank soundbank;
        try {
            soundbank = MidiSystem.getSoundbank(soundbankFile);
        } catch (InvalidMidiDataException | IOException e) {
            throw new RuntimeException(e);
        }
        synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
        synthesizer.loadAllInstruments(soundbank);

        if (verbose) {
            System.out.printf("Soundbank: name=%s, desc=%s, vendor=%s, version=%s%n", soundbank.getName(), soundbank.getDescription(), soundbank.getVendor(), soundbank.getVersion());

            Instrument[] instruments = soundbank.getInstruments();
            System.out.println("Instruments:");
            for (int i = 0; i < instruments.length; i++) {
                Instrument ins = instruments[i];
                System.out.printf("Instrument: index=%d, name=%s, str=%s%n", i, ins.getName(), ins.toString());
            }

            SoundbankResource[] resources = soundbank.getResources();
            System.out.println("Resources:");
            for (int i = 0; i < resources.length; i++) {
                SoundbankResource res = resources[i];
                System.out.printf("SoundbankResource: index=%d, name=%s, dataClass=%s, str=%s%n", i, res.getName(), res.getDataClass(), res.toString());
            }
        }
    }
}
