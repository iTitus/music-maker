package io.github.ititus.music.maker.music;

import java.util.Arrays;
import java.util.Iterator;

public abstract class Chord implements Iterable<Tone> {

    private final Tone[] tones;

    protected Chord(Tone... tones) {
        this.tones = Arrays.copyOf(tones, tones.length);
    }

    public Tone[] getTones() {
        return tones;
    }

    public int getToneAmount() {
        return tones.length;
    }

    @Override
    public Iterator<Tone> iterator() {
        return Arrays.stream(tones).iterator();
    }
}
