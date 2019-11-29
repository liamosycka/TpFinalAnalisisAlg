/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EstructurasCatedra;

import java.util.Objects;

/**
 *
 * @author Nacho
 */
public class Ciudad {
    private String ciudad;
    private String pais;
    private double latitud;
    private double longitud;
    private boolean must;

    public boolean isMust() {
        return must;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.ciudad);
        hash = 97 * hash + Objects.hashCode(this.pais);
        return hash;
    }

    public int compareTo(Ciudad ciu){
        return this.getCiudad().compareToIgnoreCase(ciu.getCiudad());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ciudad other = (Ciudad) obj;
            if (!Objects.equals(this.ciudad, other.ciudad)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public Ciudad(String ciudad, String pais, double latitud, double longitud, boolean must) {
        this.ciudad = ciudad;
        this.pais = pais;
        this.latitud = latitud;
        this.longitud = longitud;
        this.must = must;
    }
    
    public Ciudad(String ciudad, String pais) {
        this.ciudad = ciudad;
        this.pais = pais;
    }
    
    public Ciudad(String ciudad, String pais, double latitud, double longitud) {
        this(ciudad, pais, latitud, longitud, false);
    }

    public String getCiudad() {
        return ciudad;
    }
            
}


