package io.github.ititus.music.maker.player;

import io.github.ititus.commons.data.mutable.Mutable;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;

public class DoubleSound implements Closeable {

    private final Clip c1, c2;
    private final Mutable<SoundState> state = Mutable.of(SoundState.IDLE);

    public DoubleSound(Mixer m1, Mixer m2, InputStream soundStream) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            soundStream.transferTo(baos);

            try (
                    AudioInputStream ais1 =
                            AudioSystem.getAudioInputStream(new ByteArrayInputStream(baos.toByteArray()));
                    AudioInputStream ais2 =
                            AudioSystem.getAudioInputStream(new ByteArrayInputStream(baos.toByteArray()))
            ) {
                DataLine.Info info1 = new DataLine.Info(Clip.class, ais1.getFormat());
                DataLine.Info info2 = new DataLine.Info(Clip.class, ais2.getFormat());

                c1 = (Clip) m1.getLine(info1);
                c2 = (Clip) m2.getLine(info2);

                c1.open(ais1);
                c2.open(ais2);
            } catch (UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        prepare();
    }

    public DoubleSound(Mixer m1, Mixer m2, URL soundUrl) {
        try (
                AudioInputStream ais1 = AudioSystem.getAudioInputStream(soundUrl);
                AudioInputStream ais2 = AudioSystem.getAudioInputStream(soundUrl)
        ) {
            DataLine.Info info1 = new DataLine.Info(Clip.class, ais1.getFormat());
            DataLine.Info info2 = new DataLine.Info(Clip.class, ais2.getFormat());

            c1 = (Clip) m1.getLine(info1);
            c2 = (Clip) m2.getLine(info2);

            c1.open(ais1);
            c2.open(ais2);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        prepare();
    }

    public DoubleSound(Mixer m1, Mixer m2, File soundFile) {
        try (
                AudioInputStream ais1 = AudioSystem.getAudioInputStream(soundFile);
                AudioInputStream ais2 = AudioSystem.getAudioInputStream(soundFile)
        ) {
            DataLine.Info info1 = new DataLine.Info(Clip.class, ais1.getFormat());
            DataLine.Info info2 = new DataLine.Info(Clip.class, ais2.getFormat());

            c1 = (Clip) m1.getLine(info1);
            c2 = (Clip) m2.getLine(info2);

            c1.open(ais1);
            c2.open(ais2);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        prepare();
    }

    private void prepare() {
        c1.addLineListener(event -> {
            System.out.println(event.getType() + " @ pos " + event.getFramePosition());
            if (event.getType() == LineEvent.Type.START) {
                state.set(SoundState.PLAYING);
            } else if (event.getType() == LineEvent.Type.STOP) {
                int l = c1.getFrameLength();
                int f = c1.getFramePosition();
                state.set(f == 0 ? SoundState.IDLE : f < l ? SoundState.PAUSED : SoundState.DONE);
            }
        });
        c2.addLineListener(event -> {
            //System.out.println("c2: " + event.getType() + " @ pos " + event.getFramePosition());
            /*if (event.getType() == LineEvent.Type.STOP) {
                // ...
            }*/
        });
    }

    public void waitUntilDone() {
        do {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (c1.isRunning() || c2.isRunning());
    }

    public void play() {
        reset();
        resume();
    }

    @Override
    public void close() {
        c1.close();
        c2.close();
    }

    public void pause() {
        c1.stop();
        c2.stop();
    }

    public void resume() {
        c1.start();
        c2.start();
    }

    public void reset() {
        c1.setFramePosition(0);
        c2.setFramePosition(0);
    }

    public void stop() {
        pause();
        reset();
    }

    public SoundState getState() {
        return state.get();
    }

    /**
     * @return linear volume
     */
    public float getVolume() {
        FloatControl gainControl1 = (FloatControl) c1.getControl(FloatControl.Type.MASTER_GAIN);
        float db = gainControl1.getValue();
        return (float) Math.pow(10, db / 20);
    }

    public DoubleSound setVolume(float linearVolume) {
        if (linearVolume < 0 || linearVolume > 2) {
            throw new IllegalArgumentException("Volume not valid: " + linearVolume);
        }

        float gain = (float) (20 * Math.log10(linearVolume));

        FloatControl gainControl1 = (FloatControl) c1.getControl(FloatControl.Type.MASTER_GAIN);
        FloatControl gainControl2 = (FloatControl) c2.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl1.setValue(gain);
        gainControl2.setValue(gain);

        return this;
    }
}
