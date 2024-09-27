package radarbook;

import java.util.ArrayList;

public class Libro {
	
	String titulo;
	public String isbn;
	ArrayList<String> autores;

	String editora;	
	String fecha;
	String paginas;
	
	String url;
	String imagen;
	
	String valoracion;
	
	Oferta oferta;
	ArrayList<Reseña> reseñas;
	
	//Constructor
	Libro() {
		this.titulo = "";
		this.isbn = "";
		this.autores = new ArrayList<>();
		this.editora = "";
		this.fecha = "";
		this.paginas = "";
		this.url = "";
		this.imagen = "";
		this.oferta = null;
		this.valoracion = "";
		this.reseñas = new ArrayList<>();
	}
	
    // Constructor con parámetros para apiRain
    public Libro(String titulo, String isbn, ArrayList<String> autores, String editora, String fecha, String paginas,
                 String url, String imagen, String valoracion, Oferta oferta, ArrayList<Reseña> reseñas) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.autores = autores;
        this.editora = editora;
        this.fecha = fecha;
        this.paginas = paginas;
        this.url = url;
        this.imagen = imagen;
        this.valoracion = valoracion;
        this.oferta = oferta;
        this.reseñas = reseñas;
    }
    
    public Libro(String titulo, String autor) {
        this(titulo, "", crearListaAutores(autor), "", "", "", "", "", "", new Oferta(), new ArrayList<Reseña>());
    }

    private static ArrayList<String> crearListaAutores(String autor) {
        ArrayList<String> autores = new ArrayList<>();
        autores.add(autor);
        return autores;
    }
    
    public String getAutor(){
    	return this.autores.get(0);
    }
	
    //Constructor con parámetros para libreria sanPablo
    public Libro(String titulo, String isbn, ArrayList<String> autores, String editora, String fecha, String paginas, String url, String imagen, Oferta oferta) {
    	this.titulo = titulo;
	    this.isbn = isbn;
	    this.autores = autores;
	    this.editora = editora;
	    this.fecha = fecha;
	    this.paginas = paginas;
	    this.url = url;
	    this.imagen = imagen;
	    this.oferta = oferta;
    }
    
    //Constructor con parámetros para ebay
    public Libro(String titulo, String isbn, ArrayList<String> autores, String editora, String paginas, String url, Oferta oferta, String imagen) {
    	this.titulo = titulo;
	    this.isbn = isbn;
	    this.autores = autores;
	    this.editora = editora;
	    this.paginas = paginas;
	    this.url = url;
	    this.oferta = oferta;
	    this.imagen = imagen;
    }
	
	public String getTitulo() {
		return titulo;
	}
	
	public String getISBN() {
		return isbn;
	}
	
	public ArrayList<String> getAutores() {
		return autores;
	}
	
	public String getEditora() {
		return editora;
	}
	
	public String getFecha() {
		return fecha;
	}
	
	public String getPaginas() {
		return paginas;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public void setAutores(ArrayList<String> autores) {
		this.autores=autores;
	}
	
	public void setEditora(String editora) {
		this.editora = editora;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public void setPaginas(String paginas) {
		this.paginas = paginas;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
    public Oferta getOferta() {
        return oferta;
    }
    
    public void set(Oferta oferta) {
        this.oferta = oferta;
    }
    
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    public String getValoracion() {
    	return this.valoracion;
    }
    
    public void setValoracion(String valoracion) {
    	this.valoracion = valoracion;
    }
    
    public ArrayList<Reseña> getReseñas() {
    	return reseñas;
    }
    
    public void setReseñas (ArrayList<Reseña> reseñas) {
    	this.reseñas = reseñas;
    }
        
    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                ", isbn='" + isbn + '\'' +
                ", autores=" + autores +
                ", editora='" + editora + '\'' +
                ", fecha='" + fecha + '\'' +
                ", paginas='" + paginas + '\'' +
                ", url='" + url + '\''  +
                ", imagen='" + imagen + '\'' +
                ", valoracion='" + valoracion + '\'' +
                ", ofertas=" + oferta +
                ", reseñas=" + reseñas +
                '}';
    }
	
}