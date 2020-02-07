package io.github.ititus.music.maker.player;

import javax.sound.sampled.Mixer;
import java.nio.file.Path;

public final class MusicContext {

    private final Path musicListFile;
    private final Mixer m1, m2;

    public MusicContext(Path musicListFile, Mixer m1, Mixer m2) {
        this.musicListFile = musicListFile.toAbsolutePath().normalize();
        this.m1 = m1;
        this.m2 = m2;
    }

    public Path getMusicListDir() {
        return musicListFile.getParent();
    }

    public Path getMusicListFile() {
        return musicListFile;
    }

    public Mixer getM1() {
        return m1;
    }

    public Mixer getM2() {
        return m2;
    }
}
