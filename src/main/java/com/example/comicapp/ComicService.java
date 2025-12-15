package com.example.comicapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class ComicService {
    private ArrayList<Comic> listaComics = new ArrayList<>();
    private final String archivo = "comics.json";
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Comic.class, new ComicAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

    public void agregarComic(Comic comic) {
        if (listaComics.contains(comic)) {
            throw new RuntimeException("El comic ya esta añadido");
        }
        listaComics.add(comic);
        guardar();
    }

    public void eliminarComic(Comic comic) {
        listaComics.remove(comic);
        guardar();
    }
    public void editarComic(Comic original, Comic nuevo) {
        int idx = listaComics.indexOf(original);
        if (idx != -1) {
            listaComics.set(idx, nuevo);
            guardar();
        }
    }


    public double calcularGastos() {
        double total = 0;
        for (Comic c : listaComics) {
            if (c.getLoTengo()) {
                total += c.getPrecio();
            }
        }
        return total;
    }

    public List<Comic> buscarInteligente(String texto) {
        List<Comic> resultado = new ArrayList<>();
        if (texto == null || texto.isBlank()) {
            return resultado;
        }

        texto = texto.trim().toLowerCase();

        // ---------- 1. Detectar tipo ----------
        TipoDeComic tipoBuscado = null;
        for (TipoDeComic t : TipoDeComic.values()) {
            if (t.coincideConTexto(texto)) {
                tipoBuscado = t;
                break;
            }
        }
        // ---------- 2. Detectar rango ----------
        String[] partes = texto.split(" ");
        StringBuilder textoLimpio = new StringBuilder();
        Integer numDesde = null;
        Integer numHasta = null;

        for (String p : partes) {
            try {
                String[] rango = p.replace("#", "").split("-");
                numDesde = Integer.parseInt(rango[0]);
                if (rango.length > 1) {
                    numHasta = Integer.parseInt(rango[1]);
                }
            } catch (NumberFormatException e) {
                textoLimpio.append(p).append(" ");
            }
        }

        texto = textoLimpio.toString().trim();

        // ---------- 3. Búsqueda por rango (tomos) ----------
        if (numDesde != null) {
            for (Comic c : listaComics) {
                if (c instanceof TomoRecopilatorio t) {
                    boolean rangoOk =
                            t.getDesde() <= numDesde &&
                                    (numHasta == null || t.getHasta() >= numHasta);

                    boolean tipoOk = tipoBuscado == null || c.getTipo() == tipoBuscado;

                    if (rangoOk && tipoOk) {
                        resultado.add(c);
                    }
                }
            }
            return resultado;
        }

        // ---------- 4. Búsqueda textual ----------
        for (Comic c : listaComics) {

            boolean textoOk =
                    texto.isEmpty() ||
                            c.getTitulo().toLowerCase().contains(texto) ||
                            c.getEditorial().toLowerCase().contains(texto);

            // Eventos: buscar también en "incluye"
            if (!textoOk && c instanceof Evento e) {
                textoOk = e.getIncluye() != null &&
                        e.getIncluye().toLowerCase().contains(texto);
            }

            boolean tipoOk = tipoBuscado == null || c.getTipo() == tipoBuscado;

            if (textoOk && tipoOk) {
                resultado.add(c);
            }
        }

        return resultado;
    }



    public List<Comic> getListaComics() {
        return listaComics;
    }

    public int contarEditoriales() {
        return (int) listaComics.stream()
                .map(Comic::getEditorial)
                .map(String::toLowerCase)
                .distinct()
                .count();
    }

    public int getNuevosDelMes() {
        YearMonth actual = YearMonth.now();
        return (int) listaComics.stream()
                .filter(c -> c.getAgregado() != null)
                .filter(c -> YearMonth.from(c.getAgregado()).equals(actual))
                .count();
    }

    public List<Comic> getUltimos(int cantidad) {
        return listaComics.stream()
                .sorted(Comparator.comparing(Comic::getAgregado).reversed())
                .limit(cantidad)
                .toList();
    }

    public void cargar() {
        try (FileReader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<ArrayList<Comic>>() {}.getType();
            ArrayList<Comic> lista = gson.fromJson(reader, tipoLista);
            if (lista != null) {
                listaComics = lista;
                for (Comic c : listaComics) {
                    if (c.getAgregado() == null) {
                        c.setAgregado(LocalDate.now());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado, se creará uno nuevo al guardar.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void guardar() {
        try (FileWriter writer = new FileWriter(archivo)) {
            gson.toJson(listaComics, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
