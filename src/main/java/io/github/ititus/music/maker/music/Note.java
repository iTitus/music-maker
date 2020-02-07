package io.github.ititus.music.maker.music;

public enum Note {

    C(0, true, "C"),
    C_SHARP(1, false, "C#/Db"),
    D(2, true, "D"),
    D_SHARP(3, false, "D#/Eb"),
    E(4, true, "E"),
    F(5, true, "F"),
    F_SHARP(6, false, "F#/Gb"),
    G(7, true, "G"),
    G_SHARP(8, false, "G#/Ab"),
    A(9, true, "A"),
    A_SHARP(10, false, "A#/Bb"),
    B(11, true, "B");

    public static final Note D_FLAT = C_SHARP;
    public static final Note E_FLAT = D_SHARP;
    public static final Note G_FLAT = F_SHARP;
    public static final Note A_FLAT = G_SHARP;
    public static final Note B_FLAT = A_SHARP;

    public static final int MIN_MIDI_INDEX = 0;
    public static final int MAX_MIDI_INDEX = 127;
    public static final int OCTAVE_NOTES = 12;
    public static final int MIDDLE_C_INDEX = 60;
    public static final Note[] NOTES;

    static {
        Note[] values = values();
        NOTES = new Note[OCTAVE_NOTES];
        System.arraycopy(values, 0, NOTES, 0, values.length);
    }

    private final int baseMidiIndex;
    private final boolean baseNote;
    private final String name;

    Note(int baseMidiIndex, boolean baseNote, String name) {
        this.baseMidiIndex = baseMidiIndex;
        this.baseNote = baseNote;
        this.name = name;
    }

    public int getBaseMidiIndex() {
        return baseMidiIndex;
    }

    public boolean isBaseNote() {
        return baseNote;
    }

    public String getName() {
        return name;
    }
}
