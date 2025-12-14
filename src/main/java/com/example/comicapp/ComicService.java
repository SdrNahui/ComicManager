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
    public void editarComic(Comic comicEditado){
    //    int index = listaComics.indexOf(comicEditado);
    //    if(index == -1){
    //        throw new RuntimeException("El comic no existe");
    //    }
    //    listaComics.set(index,comicEditado);
        guardar();
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
        if (texto.contains("tomo")) {
            tipoBuscado = TipoDeComic.TOMO;
        } else if (texto.contains("evento")) {
            tipoBuscado = TipoDeComic.EVENTO;
        } else if (texto.contains("libro") || texto.contains("one")) {
            tipoBuscado = TipoDeComic.LIBRO;
        }

        // ---------- 2. Detectar rango de números ----------
        String[] partes = texto.split(" ");
        StringBuilder nombreDetectado = new StringBuilder();
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
                nombreDetectado.append(p).append(" ");
            }
        }

        String nombreFinal = nombreDetectado.toString().trim();

        // ---------- 3. Búsqueda por rango (solo tomos) ----------
        if (numDesde != null) {
            for (Comic c : listaComics) {
                if (c instanceof TomoRecopilatorio tomo) {
                    boolean dentroDelRango =
                            tomo.getDesde() <= numDesde &&
                                    (numHasta == null || tomo.getHasta() >= numHasta);

                    boolean coincideTipo = tipoBuscado == null || c.getTipo() == tipoBuscado;

                    if (dentroDelRango && coincideTipo) {
                        resultado.add(c);
                    }
                }
            }
            return resultado;
        }

        // ---------- 4. Búsqueda exacta por título ----------
        for (Comic c : listaComics) {
            if (c.getTitulo().equalsIgnoreCase(nombreFinal)) {
                if (tipoBuscado == null || c.getTipo() == tipoBuscado) {
                    resultado.add(c);
                }
            }
        }

        if (!resultado.isEmpty()) {
            return resultado;
        }

        // ---------- 5. Búsqueda parcial (título / editorial / tipo) ----------
        for (Comic c : listaComics) {
            boolean coincideTexto =
                    c.getTitulo().toLowerCase().contains(texto) ||
                            c.getEditorial().toLowerCase().contains(texto);

            boolean coincideTipo = tipoBuscado == null || c.getTipo() == tipoBuscado;

            if (coincideTexto && coincideTipo) {
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

    private void guardar() {
        try (FileWriter writer = new FileWriter(archivo)) {
            gson.toJson(listaComics, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
