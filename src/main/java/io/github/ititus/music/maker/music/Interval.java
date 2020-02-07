package io.github.ititus.music.maker.music;

public enum Interval {

    PERFECT_UNISON(0),
    MINOR_SECOND(1),
    MAJOR_SECOND(2),
    MINOR_THIRD(3),
    MAJOR_THIRD(4),
    PERFECT_FOURTH(5),
    TRITONE(6),
    PERFECT_FIFTH(7),
    MINOR_SIXTH(8),
    MAJOR_SIXTH(9),
    MINOR_SEVENTH(10),
    MAJOR_SEVENTH(11),
    PERFECT_OCTAVE(12);

    public static final Interval[] INTERVALS;

    static {
        Interval[] values = values();
        INTERVALS = new Interval[values.length];
        System.arraycopy(values, 0, INTERVALS, 0, values.length);
    }

    private final int pitchMod;

    Interval(int pitchMod) {
        this.pitchMod = pitchMod;
    }

    public static Interval get(int pitchMod) {
        if (pitchMod < 0 || pitchMod >= INTERVALS.length) {
            throw new IllegalArgumentException();
        }

        return INTERVALS[pitchMod];
    }

    public int getPitchMod() {
        return pitchMod;
    }
}
