package radarbook;

public class Oferta {
	String lugar;
	String url;
	String precio;
	String disponibilidad;
	//Añadido para ebay
	String imagen;
	
	//Constructor
	Oferta () {
		this.lugar = "";
		this.url = "";
		this.precio = "";
		this.disponibilidad = "";
	}
	
    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
    
    public String getDisponibilidad() {
    	return this.disponibilidad;
    }
    
    public void setDisponibilidad(String disponibilidad) {
    	this.disponibilidad = disponibilidad;
    }
    
    public String getImagen() {
    	return this.imagen;
    }
    
    public void setImagen(String imagen) {
    	this.imagen = imagen;
    }
    
    @Override
    public String toString() {
        return "Oferta{" +
                "lugar='" + lugar + '\'' +
                ", url='" + url + '\'' +
                ", precio='" + precio + '\'' +
                ", disponibilidad='" + disponibilidad + '\'' +
                '}';
    }
    
}
