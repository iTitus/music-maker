package io.github.ititus.music.maker.player;

import io.github.ititus.music.maker.midi.SoundbankLoader;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MusicPlayer {

    public static void main(String[] args) {
        Path musicListFile = Paths.get(System.getProperty("user.home")).resolve("Desktop").resolve("music").resolve("music_list.json");

        System.out.println("Loading Sound System...");
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        Mixer m1 = AudioSystem.getMixer(null); // headphones/speaker
        Mixer m2 = AudioSystem.getMixer(Arrays.stream(mixerInfos).filter(i -> i.getName().startsWith("CABLE Input")).findAny().get()); // mixed with microphone
        MusicContext context = new MusicContext(musicListFile, m1, m2);

        Synthesizer synthesizer;
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
        SoundbankLoader.load(synthesizer, false);

        System.out.println("Loading Music...");

        MusicList musicList = MusicList.load(context);
        musicList.save();

        System.out.println();

        MusicPiece currentlyPlaying = null;
        while (true) {
            if (currentlyPlaying != null && currentlyPlaying.getSound().getState() == SoundState.DONE) {
                currentlyPlaying = null;
            }

            System.out.println("#".repeat(80));
            System.out.println("Music Player. Press 'q' to exit or 'p <number>' to play that song.");
            if (currentlyPlaying != null) {
                System.out.printf("Currently playing: %s%n", currentlyPlaying.getName());
                System.out.println("Press 'p' to pause/play or 's' to stop.");
            }
            System.out.printf("Current Music List: %s%n", musicList.getName());
            System.out.println("Song List:");

            List<MusicPiece> musicPieces = musicList.getMusicPieces();
            for (int i = 0; i < musicPieces.size(); i++) {
                MusicPiece p = musicPieces.get(i);
                System.out.printf("%d: %s%n", i, p.getName());
            }

            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            String in = parts[0];
            parts = Arrays.copyOfRange(parts, 1, parts.length);

            if (in.equalsIgnoreCase("q")) {
                break;
            } else if (in.equalsIgnoreCase("p")) {
                if (parts.length == 1) {
                    int i = -1;
                    try {
                        i = Integer.parseInt(parts[0]);
                    } catch (NumberFormatException ignored) {
                    }

                    if (i < 0 || i >= musicPieces.size()) {
                        System.out.printf("Music Piece does not exist: %d%n", i);
                        continue;
                    }

                    if (currentlyPlaying != null && (currentlyPlaying.getSound().getState() == SoundState.PLAYING || currentlyPlaying.getSound().getState() == SoundState.PAUSED)) {
                        currentlyPlaying.getSound().stop();
                    }

                    currentlyPlaying = musicPieces.get(i);
                    System.out.printf("Now playing: %s%n", currentlyPlaying.getName());
                    currentlyPlaying.getSound().play();
                    continue;
                } else if (parts.length == 0 && currentlyPlaying != null) {
                    SoundState state = currentlyPlaying.getSound().getState();
                    if (state == SoundState.PLAYING) {
                        currentlyPlaying.getSound().pause();
                    } else if (state == SoundState.PAUSED) {
                        currentlyPlaying.getSound().resume();
                    }
                    continue;
                }
            } else if (in.equalsIgnoreCase("s")) {
                if (currentlyPlaying != null && (currentlyPlaying.getSound().getState() == SoundState.PLAYING || currentlyPlaying.getSound().getState() == SoundState.PAUSED)) {
                    currentlyPlaying.getSound().stop();
                    currentlyPlaying = null;
                } else {
                    System.out.println("Nothing to stop.");
                }
                continue;
            }

            System.out.printf("Expected player control, but got: %s%n", in);
        }

        System.out.println("Shutting Down");
        try {
            musicList.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        synthesizer.close();
        System.out.println("Bye Bye");
    }
}
