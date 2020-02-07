package io.github.ititus.music.maker.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class MusicPieceSettings {

    private float volume = 1.0f;

    public float getVolume() {
        return volume;
    }

    public static class MusicPieceSettingsTypeAdapter extends TypeAdapter<MusicPieceSettings> {

        public static final MusicPieceSettingsTypeAdapter INSTANCE = new MusicPieceSettingsTypeAdapter();

        private static final Gson GSON = new GsonBuilder().create();

        private MusicPieceSettingsTypeAdapter() {
        }

        @Override
        public void write(JsonWriter out, MusicPieceSettings value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            GSON.toJson(value, MusicPieceSettings.class, out);
        }

        @Override
        public MusicPieceSettings read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return new MusicPieceSettings();
            }

            return GSON.fromJson(in, MusicPieceSettings.class);
        }
    }
}
