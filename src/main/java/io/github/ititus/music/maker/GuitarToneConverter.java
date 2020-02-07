package io.github.ititus.music.maker;

import io.github.ititus.music.maker.music.Interval;
import io.github.ititus.music.maker.music.Tone;

import java.util.Arrays;

import static io.github.ititus.music.maker.music.Note.*;
import static io.github.ititus.music.maker.music.Octave.*;

public class GuitarToneConverter {

    public static final GuitarToneConverter DEFAULT_TUNING = new GuitarToneConverter(
            Tone.get(E, OCT_4),
            Tone.get(B, OCT_3),
            Tone.get(G, OCT_3),
            Tone.get(D, OCT_3),
            Tone.get(A, OCT_2),
            Tone.get(E, OCT_2)
    );
    public static final GuitarToneConverter DROP_D = new GuitarToneConverter(
            Tone.get(D, OCT_4),
            Tone.get(B, OCT_3),
            Tone.get(G, OCT_3),
            Tone.get(D, OCT_3),
            Tone.get(A, OCT_2),
            Tone.get(E, OCT_2)
    );

    private final Tone[] strings;

    private GuitarToneConverter(Tone... strings) {
        this.strings = strings;
    }

    public static void main(String[] args) {
        GuitarToneConverter c = DEFAULT_TUNING.capo(6);
        printFretboardDiagram(c, 5);
    }

    private static void printFretboardDiagram(GuitarToneConverter c, int maxFret) {
        for (int s = 1; s <= c.getStringCount(); s++) {
            StringBuilder b = new StringBuilder();
            for (int f = 0; f <= maxFret; f++) {
                String t = c.get(s, f).toString();
                b.append(f == maxFret ? t : String.format("%-8s", t));
            }
            System.out.println(b.toString());
        }
    }

    public GuitarToneConverter capo(int fret) {
        if (fret < 0) {
            throw new IllegalArgumentException();
        } else if (fret == 0) {
            return this;
        }

        Interval i = Interval.get(fret);
        return new GuitarToneConverter(Arrays.stream(strings).map(t -> t.modUp(i)).toArray(Tone[]::new));
    }

    public int getStringCount() {
        return strings.length;
    }

    public Tone get(int string, int fret) {
        if (string < 1 || string > strings.length || fret < 0) {
            throw new IllegalArgumentException();
        }

        return strings[string - 1].modUp(Interval.get(fret));
    }
}
