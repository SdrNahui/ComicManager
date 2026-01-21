package com.example.comicapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Comic {
    private String titulo;
    //    private int numero;
    private String editorial;
    private double precio;
    private boolean loTengo;
    private String notas;
    private LocalDate agregado;
    protected TipoDeComic tipo;

    public Comic(String titulo, String editorial, double precio, boolean loTengo, String notas) {
        if ((titulo == null) || titulo.isEmpty()) {
            throw new RuntimeException("nombre invalido");
        }
        this.titulo = titulo;
        if ((editorial == null) || editorial.isEmpty()) {
            throw new RuntimeException("Editorial no puede ser vacia");
        }
        this.editorial = editorial;
        if (precio < 0.0) {
            throw new RuntimeException("El precio no puede ser menor a 0.0");
        }
        this.precio = precio;
        this.loTengo = loTengo;
        this.notas = notas;
        this.agregado = LocalDate.now();
    }

    public String getTitulo() {
        return titulo;
    }

    public String getEditorial() {
        return editorial;
    }

    public TipoDeComic getTipo() {
        return tipo;
    }
    /*    public int getNumero() {
        return numero;
    } */

    public double getPrecio() {
        return precio;
    }

    public boolean getLoTengo() {
        return loTengo;
    }

    public String getNotas() {
        return notas;
    }

    public void setLoTengo(boolean loTengo) {
        this.loTengo = loTengo;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comic comic = (Comic) o;
        // nombre
        if (titulo == null && comic.titulo != null) return false;
        if (titulo != null && !titulo.equalsIgnoreCase(comic.titulo)) return false;
        // editorial
        if (editorial == null && comic.editorial != null) return false;
        if (editorial != null && !editorial.equalsIgnoreCase(comic.editorial)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (titulo != null ? titulo.toLowerCase().hashCode() : 0);
        result = 31 * result + (editorial != null ? editorial.toLowerCase().hashCode() : 0);
        return result;
    }


    public LocalDate getAgregado() {
        return agregado;
    }

    public void setAgregado(LocalDate agregado) {
        this.agregado = agregado;
    }

    public abstract String getDescripcion();

    public void setTitulo(String titulo) {
        if(titulo == null || titulo.isBlank()){
            throw new RuntimeException("Titulo invalido");
        }
        this.titulo = titulo;
    }

    public void setEditorial(String editorial) {
        if(editorial == null || editorial.isBlank()){
            throw new RuntimeException("Editorial invalida");
        }
        this.editorial = editorial;
    }

    public void setPrecio(double precio) {
        if(precio < 0){
            throw new RuntimeException("Precio invalido");
        }
        this.precio = precio;
    }
}
