package io.github.ititus.music.maker.music;

import static io.github.ititus.music.maker.music.Interval.*;

public class Seventh extends Chord {

    private Seventh(Tone root, Tone third, Tone fifth, Tone seventh) {
        super(root, third, fifth, seventh);
    }

    public static Seventh get(Tone root, Seventh.Type type, Seventh.Inversion inversion) {
        Tone third = root.modUp(type.getLower());
        Tone fifth = third.modUp(type.getMiddle());
        Tone seventh = fifth.modUp(type.getUpper());
        return inversion.build(root, third, fifth, seventh);
    }

    public enum Type {
        // tertian
        MAJOR(MAJOR_THIRD, MINOR_THIRD, MAJOR_THIRD),
        MINOR(MINOR_THIRD, MAJOR_THIRD, MINOR_THIRD),
        DOMINANT(MAJOR_THIRD, MINOR_THIRD, MINOR_THIRD),
        DIMINISHED(MINOR_THIRD, MINOR_THIRD, MINOR_THIRD),
        HALF_DIMINISHED(MINOR_THIRD, MINOR_THIRD, MAJOR_THIRD),
        MINOR_MAJOR(MINOR_THIRD, MAJOR_THIRD, MAJOR_THIRD),
        AUGMENTED_MAJOR(MAJOR_THIRD, MAJOR_THIRD, MINOR_THIRD),
        AUGMENTED_AUGMENTED(MAJOR_THIRD, MAJOR_THIRD, MAJOR_THIRD),
        // non-tertian
        AUGMENTED(MAJOR_THIRD, MAJOR_THIRD, MAJOR_SECOND),
        DIMINISHED_MAJOR(MINOR_THIRD, MINOR_THIRD, PERFECT_FOURTH),
        DOMINANT_FLAT_FIVE(MAJOR_THIRD, MAJOR_SECOND, MAJOR_THIRD),
        MAJOR_FLAT_FIVE(MAJOR_THIRD, MAJOR_SECOND, PERFECT_FOURTH);


        private final Interval lower, middle, upper;

        Type(Interval lower, Interval middle, Interval upper) {
            this.lower = lower;
            this.middle = middle;
            this.upper = upper;
        }

        public Interval getLower() {
            return lower;
        }

        public Interval getMiddle() {
            return middle;
        }

        public Interval getUpper() {
            return upper;
        }
    }

    public enum Inversion {
        ROOT(Seventh::new),
        FIRST((root, third, fifth, seventh) -> new Seventh(third, fifth, seventh, root.modUp(PERFECT_OCTAVE))),
        SECOND((root, third, fifth, seventh) -> new Seventh(fifth, seventh, root.modUp(PERFECT_OCTAVE), third.modUp(PERFECT_OCTAVE))),
        THIRD((root, third, fifth, seventh) -> new Seventh(seventh, root.modUp(PERFECT_OCTAVE), third.modUp(PERFECT_OCTAVE), fifth.modUp(PERFECT_OCTAVE)));

        private final SeventhBuilder builder;

        Inversion(SeventhBuilder builder) {
            this.builder = builder;
        }

        public SeventhBuilder getBuilder() {
            return builder;
        }

        public Seventh build(Tone root, Tone third, Tone fifth, Tone seventh) {
            return builder.build(root, third, fifth, seventh);
        }
    }

    @FunctionalInterface
    private interface SeventhBuilder {

        Seventh build(Tone root, Tone third, Tone fifth, Tone seventh);

    }
}
