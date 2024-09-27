package radarbook;

public class Reseña {
	String logo;
	String who;
	String tituloReseña;
	String texto;
	String estrellas;
	
	Reseña (String logo, String who, String tituloReseña, String texto, String estrellas){
		this.logo = logo;
		this.who = who;
		this.tituloReseña = tituloReseña;
		this.texto = texto;
		this.estrellas = estrellas;
	}
	
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getTituloReseña() {
        return tituloReseña;
    }

    public void setTituloReseña(String tituloReseña) {
        this.tituloReseña = tituloReseña;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(String estrellas) {
        this.estrellas = estrellas;
    }
    
    @Override
    public String toString() {
        return "Reseña{" +
                "logo='" + logo + '\'' +
                ", who='" + who + '\'' +
                ", tituloReseña='" + tituloReseña + '\'' +
                ", texto='" + texto + '\'' +
                ", estrellas='" + estrellas + '\'' +
                '}';
    }
}

