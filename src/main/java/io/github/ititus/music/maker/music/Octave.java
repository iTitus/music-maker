package io.github.ititus.music.maker.music;

public enum Octave {

    OCT_NEG_1(0),
    OCT_0(1),
    OCT_1(2),
    OCT_2(3),
    OCT_3(4),
    OCT_4(5),
    OCT_5(6),
    OCT_6(7),
    OCT_7(8),
    OCT_8(9),
    OCT_9(10);

    public static final Octave MIDDLE = OCT_4;
    public static final Octave[] OCTAVES;

    static {
        Octave[] values = values();
        OCTAVES = new Octave[values.length];
        for (int i = 0; i < values.length; i++) {
            OCTAVES[i] = values[i];
        }
    }

    private final int offset;

    Octave(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public Octave up() {
        return OCTAVES[offset + 1];
    }

    public Octave down() {
        return OCTAVES[offset - 1];
    }

    public String getOctaveString() {
        return Integer.toString(offset);
    }
}
