package io.github.ititus.music.maker.music;

import static io.github.ititus.music.maker.music.Interval.*;

public class Triad extends Chord {

    private Triad(Tone root, Tone third, Tone fifth) {
        super(root, third, fifth);
    }

    public static Triad get(Tone root, Type type, Inversion inversion) {
        Tone third = root.modUp(type.getLower());
        Tone fifth = third.modUp(type.getUpper());
        return inversion.build(root, third, fifth);
    }

    public enum Type {
        // tertian
        MAJOR(MAJOR_THIRD, MINOR_THIRD),
        MINOR(MINOR_THIRD, MAJOR_THIRD),
        DIMINISHED(MINOR_THIRD, MINOR_THIRD),
        AUGMENTED(MAJOR_THIRD, MAJOR_THIRD),
        // non-tertian
        SUS_4(PERFECT_FOURTH, MAJOR_SECOND),
        SUS_2(MAJOR_SECOND, PERFECT_FOURTH);

        private final Interval lower, upper;

        Type(Interval lower, Interval upper) {
            this.lower = lower;
            this.upper = upper;
        }

        public Interval getLower() {
            return lower;
        }

        public Interval getUpper() {
            return upper;
        }
    }

    public enum Inversion {
        ROOT(Triad::new),
        FIRST((root, third, fifth) -> new Triad(third, fifth, root.modUp(PERFECT_OCTAVE))),
        SECOND((root, third, fifth) -> new Triad(fifth, root.modUp(PERFECT_OCTAVE), third.modUp(PERFECT_OCTAVE)));

        private final TriadBuilder builder;

        Inversion(TriadBuilder builder) {
            this.builder = builder;
        }

        public TriadBuilder getBuilder() {
            return builder;
        }

        public Triad build(Tone root, Tone third, Tone fifth) {
            return builder.build(root, third, fifth);
        }
    }

    @FunctionalInterface
    private interface TriadBuilder {

        Triad build(Tone root, Tone third, Tone fifth);

    }
}
