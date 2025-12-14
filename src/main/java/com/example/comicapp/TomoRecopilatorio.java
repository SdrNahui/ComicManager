package com.example.comicapp;

public class TomoRecopilatorio extends Comic{
    private int desde;
    private int hasta;
    public TomoRecopilatorio(String titulo, String editorial, double precio, boolean loTengo,
                             String notas, int desde, int hasta){


        super(titulo, editorial, precio, loTengo, notas);
        if(desde < 0 ) {
            throw new RuntimeException("no puede haber un numero menor a 0");
        }
        this.desde = desde;
        if(hasta < desde){
            throw new RuntimeException("No puede ser menor al primer numero");
        }
        this.hasta =hasta;
        this.tipo = TipoDeComic.TOMO;

    }

    @Override
    public String getDescripcion() {
        return " (" + desde + "–" + hasta + ")" + " • " + getEditorial();
    }

    public int getDesde() {
        return desde;
    }

    public int getHasta() {
        return hasta;
    }
}
