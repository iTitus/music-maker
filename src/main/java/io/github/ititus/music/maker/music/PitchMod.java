package io.github.ititus.music.maker.music;

public enum PitchMod {

    DOUBLE_FLAT(-2),
    FLAT(-1),
    NONE(0),
    SHARP(1),
    DOUBLE_SHARP(2);

    private final int pitchMod;

    PitchMod(int pitchMod) {
        this.pitchMod = pitchMod;
    }

    public int getPitchMod() {
        return pitchMod;
    }
}
