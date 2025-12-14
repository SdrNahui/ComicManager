package com.example.comicapp;

import com.google.gson.*;
import java.lang.reflect.Type;

public class ComicAdapter implements JsonSerializer<Comic>, JsonDeserializer<Comic> {

    @Override
    public JsonElement serialize(Comic src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = context.serialize(src).getAsJsonObject();
        json.addProperty("tipo", src.getTipo().name());
        return json;
    }

    @Override
    public Comic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        TipoDeComic tipo;

        // 🔹 COMPATIBILIDAD CON JSON VIEJOS
        if (obj.has("tipo") && !obj.get("tipo").isJsonNull()) {
            tipo = TipoDeComic.valueOf(obj.get("tipo").getAsString());
        } else {
            // Inferir por estructura
            if (obj.has("desde") || obj.has("hasta")) {
                tipo = TipoDeComic.TOMO;
            } else if (obj.has("incluye")) {
                tipo = TipoDeComic.EVENTO;
            } else {
                tipo = TipoDeComic.LIBRO;
            }
        }

        return switch (tipo) {
            case LIBRO ->
                    context.deserialize(obj, Libro.class);
            case TOMO ->
                    context.deserialize(obj, TomoRecopilatorio.class);
            case EVENTO ->
                    context.deserialize(obj, Evento.class);
        };
    }
}
