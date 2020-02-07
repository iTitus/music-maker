package io.github.ititus.music.maker.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MusicList implements Closeable {

    private final String name;
    private final List<MusicPiece> musicPieces = new ArrayList<>();

    private transient MusicContext context;

    private MusicList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<MusicPiece> getMusicPieces() {
        return musicPieces;
    }

    @Override
    public void close() throws IOException {
        for (MusicPiece p : musicPieces) {
            p.close();
        }
    }

    public static MusicList load(MusicContext context) {
        Gson gson = createGson(context);

        MusicList musicList;
        try (BufferedReader r = Files.newBufferedReader(context.getMusicListFile())) {
            musicList = gson.fromJson(r, MusicList.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        musicList.context = context;
        return musicList;
    }

    public void save() {
        Gson gson = createGson(context);

        try (BufferedWriter w = Files.newBufferedWriter(context.getMusicListFile())) {
            gson.toJson(this, w);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Gson createGson(MusicContext context) {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(MusicPiece.class, new MusicPiece.MusicPieceTypeAdapter(context))
                .create();
    }
}
