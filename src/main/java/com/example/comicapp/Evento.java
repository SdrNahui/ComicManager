package com.example.comicapp;

public class Evento extends Comic {
    private String incluye;

    public Evento(String titulo, String editorial, double precio, boolean loTengo,
                  String notas, String incluye){
        super(titulo, editorial, precio, loTengo, notas);
        this.incluye = incluye;
        this.tipo = TipoDeComic.EVENTO;
    }
    public String getIncluye(){
        return incluye;
    }

    @Override
    public String getDescripcion() {
        return " • Evento" + " • " + getEditorial() + (incluye != null && !incluye.isBlank()
                ? "\nIncluye: " + incluye : "");
    }

    public void setIncluye(String incluye) {
        if(incluye == null || incluye.isBlank()){
            throw new RuntimeException("incluye invalido");
        }
        this.incluye = incluye;
    }
}
