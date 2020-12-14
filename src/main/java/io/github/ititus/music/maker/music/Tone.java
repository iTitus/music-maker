package io.github.ititus.music.maker.music;

public class Tone {

    private static final Tone[] TONES = new Tone[Note.MAX_MIDI_INDEX + 1];

    private final Note note;
    private final Octave octave;
    private final int midiIndex;

    private Tone(Note note, Octave octave, int midiIndex) {
        this.note = note;
        this.octave = octave;
        this.midiIndex = midiIndex;
    }

    public static Tone get(int midiIndex) {
        if (midiIndex < 0 || midiIndex > Note.MAX_MIDI_INDEX) {
            throw new IllegalArgumentException();
        }
        Tone tone = TONES[midiIndex];
        if (tone == null) {
            TONES[midiIndex] = tone = new Tone(Note.NOTES[midiIndex % Note.OCTAVE_NOTES],
                    Octave.OCTAVES[midiIndex / Note.OCTAVE_NOTES], midiIndex);
        }
        return tone;
    }

    public static Tone get(Note note, Octave octave) {
        int midiIndex = note.getBaseMidiIndex() + octave.getOffset() * Note.OCTAVE_NOTES;
        Tone tone = TONES[midiIndex];
        if (tone == null) {
            TONES[midiIndex] = tone = new Tone(note, octave, midiIndex);
        }
        return tone;
    }

    public Note getNote() {
        return note;
    }

    public Octave getOctave() {
        return octave;
    }

    public int getMidiIndex() {
        return midiIndex;
    }

    public Tone mod(PitchMod mod) {
        if (!note.isBaseNote()) {
            throw new RuntimeException();
        }

        return get(midiIndex + mod.getPitchMod());
    }

    public Tone modUp(Interval interval) {
        return get(midiIndex + interval.getPitchMod());
    }

    public Tone modDown(Interval interval) {
        return get(midiIndex - interval.getPitchMod());
    }

    @Override
    public String toString() {
        return note.getName() + octave.getOctaveString();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(midiIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tone)) {
            return false;
        }
        return midiIndex == ((Tone) o).midiIndex;
    }
}
