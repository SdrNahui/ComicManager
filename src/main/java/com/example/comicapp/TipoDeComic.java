package com.example.comicapp;

import java.util.List;

public enum TipoDeComic {
    LIBRO("libro", "one", "one-shot", "oneshot"),
    TOMO("tomo", "recopilatorio"),
    EVENTO("evento", "saga", "crossover");

    private final List<String> aliases;

    TipoDeComic(String... aliases) {
        this.aliases = List.of(aliases);
    }

    public boolean coincideConTexto(String texto) {
        return aliases.stream().anyMatch(a -> texto.contains(a));
    }
}

