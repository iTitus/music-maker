package io.github.ititus.music.maker.midi;

import io.github.ititus.music.maker.music.Chord;
import io.github.ititus.music.maker.music.Tone;

import javax.sound.midi.MidiChannel;

public class ChannelPlayer implements Player {

    private static final int DEFAULT_VELOCITY = 63;

    private final MidiChannel channel;

    public ChannelPlayer(MidiChannel channel) {
        this.channel = channel;
    }

    @Override
    public void play(Chord chord) {
        for (Tone t : chord) {
            channel.noteOn(t.getMidiIndex(), DEFAULT_VELOCITY);
        }
    }
}
