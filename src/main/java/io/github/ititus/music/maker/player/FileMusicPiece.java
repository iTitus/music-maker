package io.github.ititus.music.maker.player;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Path;

public class FileMusicPiece extends MusicPiece {

    private final Path file;

    public FileMusicPiece(String name, MusicPieceSettings settings, Path file) {
        super(name, settings);
        this.file = file;
    }

    public Path getFile() {
        return file;
    }

    @Override
    public void init(MusicContext context){
        close();
        sound = new DoubleSound(context.getM1(), context.getM2(), file.toFile());
        super.init(context);
    }

    @Override
    public void serialize(MusicContext ctx, JsonObject obj) {
        obj.addProperty("path", ctx.getMusicListDir().relativize(file).normalize().toString());
    }
}
