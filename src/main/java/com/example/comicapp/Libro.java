package com.example.comicapp;

public class Libro extends Comic {
    public Libro(String titulo, String editorial, double precio, boolean loTengo,
                 String notas){
        super(titulo, editorial, precio, loTengo, notas);
        this.tipo = TipoDeComic.LIBRO;
    }
    @Override
    public String getDescripcion() {
        return " • Libro" + " • " + getEditorial();
    }

}
