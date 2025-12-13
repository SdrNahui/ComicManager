package com.example.comicapp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ComicService {
    private ArrayList<Comic> listaComics = new ArrayList<>();
    private final String archivo = "comics.json";
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();


    public void agregarComic(Comic comic){
        if(listaComics.contains(comic)){
            throw new RuntimeException("El comic ya esta añadido");
        }
        listaComics.add(comic);
        guardar();
    }
    public void editarComic(Comic comic){

    }
    public void eliminarComic(Comic comic){
        listaComics.remove(comic);
        guardar();
    }
    public double calcularGastos(){
        double total = 0;
        for (Comic c : listaComics){
            if(c.getLoTengo()){
                total += c.getPrecio();
            }
        }
        return total;
    }
    public List<Comic> buscarInteligente(String texto){
        List<Comic> resultado = new ArrayList<>();
        if (texto == null || texto.isEmpty()){
            return resultado;
        }
        texto = texto.trim();

        //nom + num
        String[] partes = texto.split(" ");
        StringBuilder nomDec = new StringBuilder();
        Integer numDec = null;

        for (String p : partes){
            try{
                numDec = Integer.parseInt(p.replace("#",""));
            } catch (NumberFormatException e){
                if (!p.equalsIgnoreCase("n") && !p.equalsIgnoreCase("nº")){
                    nomDec.append(p).append(" ");
                }
            }
        }

        String nomFinal = nomDec.toString().trim().toLowerCase();

        // Nombre + número
        if (numDec != null){
            for (Comic c : listaComics){
                if (c.getTitulo().toLowerCase().equals(nomFinal) && c.getNumero() == numDec){
                    resultado.add(c);
                }
            }
            return resultado;
        }

        //Exacta
        boolean coincide = false;
        for (Comic c : listaComics){
            if(c.getTitulo().equalsIgnoreCase(nomFinal)){
                coincide = true;
                break;
            }
        }

        if (coincide){
            for(Comic c : listaComics){
                if (c.getTitulo().equalsIgnoreCase(nomFinal)){
                    resultado.add(c);
                }
            }
            return resultado;
        }

        //Parcial
        String t = texto.toLowerCase();
        for (Comic c : listaComics){
            if (c.getTitulo().toLowerCase().contains(t) || c.getEditorial().toLowerCase().contains(t)){
                resultado.add(c);
            }
        }

        return resultado;
    }
    public List<Comic> buscarEditorial(String editorial){
        if(editorial.isEmpty() || editorial == null){
            return listaComics;
        }
        String filtro= editorial.toLowerCase();
        return listaComics.stream().filter(c -> c.getEditorial().toLowerCase().contains(filtro)).toList();


    }
    public void msgError(String msg){
        System.out.println(msg);
    }
    public ArrayList<Comic> getListaComics() {
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


    public void setListaComics(ArrayList<Comic> listaComics) {
        this.listaComics = listaComics;
    }

    //gson
    private void guardar() {
        try (FileWriter writer = new FileWriter(archivo)) {
            gson.toJson(listaComics, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargar() {
        try (FileReader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<ArrayList<Comic>>() {}.getType();
            ArrayList<Comic> lista = gson.fromJson(reader, tipoLista);
            if (lista != null) {
                listaComics = lista;
                for (Comic c : listaComics){
                    if (c.getAgregado() == null){
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





}
