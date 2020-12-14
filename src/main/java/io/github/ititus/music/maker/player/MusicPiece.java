package io.github.ititus.music.maker.player;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

public abstract class MusicPiece implements Closeable {

    private static final Map<String, MusicPieceFactory<? extends MusicPiece>> FACTORIES = Map.of(
            "file", (ctx, name, settings, obj) -> new FileMusicPiece(name, settings,
                    ctx.getMusicListDir().resolve(obj.get("path").getAsString()))
    );

    private static final Map<Class<? extends MusicPiece>, String> TYPES = Map.of(
            FileMusicPiece.class, "file"
    );

    protected final String name;
    protected final MusicPieceSettings settings;

    protected transient DoubleSound sound;

    protected MusicPiece(String name, MusicPieceSettings settings) {
        this.name = name;
        this.settings = settings;
    }

    public String getName() {
        return name;
    }

    public MusicPieceSettings getSettings() {
        return settings;
    }

    public DoubleSound getSound() {
        return sound;
    }

    @Override
    public void close() {
        if (sound != null) {
            sound.close();
            sound = null;
        }
    }

    public void init(MusicContext context) {
        sound.setVolume(settings.getVolume());
    }

    public abstract void serialize(MusicContext ctx, JsonObject obj);

    private interface MusicPieceFactory<T extends MusicPiece> {

        T create(MusicContext ctx, String name, MusicPieceSettings settings, JsonObject obj);

    }

    public static class MusicPieceTypeAdapter extends TypeAdapter<MusicPiece> {

        private static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(MusicPieceSettings.class,
                        MusicPieceSettings.MusicPieceSettingsTypeAdapter.INSTANCE)
                .create();

        private final MusicContext context;

        public MusicPieceTypeAdapter(MusicContext context) {
            this.context = context;
        }

        @Override
        public void write(JsonWriter out, MusicPiece value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            JsonObject obj = new JsonObject();
            obj.addProperty("name", value.getName());
            obj.add("settings", GSON.toJsonTree(value.getSettings()));
            obj.addProperty("type", TYPES.get(value.getClass()));
            value.serialize(context, obj);
            GSON.toJson(obj, out);
        }

        @Override
        public MusicPiece read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            JsonObject obj = GSON.fromJson(in, JsonObject.class);
            String type = obj.get("type").getAsString();
            String name = obj.get("name").getAsString();
            JsonElement settingsElement = obj.get("settings");
            MusicPieceSettings settings = settingsElement == null ? new MusicPieceSettings() :
                    GSON.fromJson(settingsElement, MusicPieceSettings.class);
            MusicPiece musicPiece = FACTORIES.get(type).create(context, name, settings, obj);
            musicPiece.init(context);
            return musicPiece;
        }
    }
}
