package radarbook;

import java.io.IOException;
import java.util.ArrayList;

//Import de todo lo necesario de jsoup
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class sanPablo {

	static String lugar = "Librería San Pablo";
	Libro libro;
			
    //Método con el que devolver un libro junto a su oferta
	public Libro devolverLibro(String aBuscar) {
		
		String direccion = "https://libreria.sanpablo.es/busqueda/listaLibros.php?tipoBus=full&palabrasBusqueda=" + aBuscar
				+ "&pagSel=1&tipoArticulo=L";
		
		
		//Creo el documento que se va a generar
		Document doc = null;
		Document docProfundo = null;
		
		//Creo la oferta que incluirá el libro
		//Como puede haber varias ofertas a distintos precios, habrá que guardarlas en un array
		ArrayList<Oferta> ofertas = new ArrayList<>();
		
		//Conectarse a la web destino
		try {
			
			//Establecer conexión
			doc = Jsoup.connect(direccion).timeout(100000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36").get();
			
			//Selecciono los precios
			Elements precio = doc.body().select("p.precio strong");
			
			//Añado los precios a un arrayList de texto
			ArrayList<String> precios = new ArrayList<>();
			for (Element pre : precio) {
				precios.add(pre.text());
			}
						
			//Selecciono el enlace al libro
			Elements enlace = doc.body().select("dd.title a");
			
			//Puesto que el enlace (o enlaces) está dentro de un href, debemos sacarlo con el método attr, y añadirlos a su array de texto
			ArrayList<String> links = new ArrayList<>();
			
			for (Element link : enlace) {
				String url = link.attr("href");
				String enlaceCompleto = "https://libreria.sanpablo.es" + url;
				links.add(enlaceCompleto);
			}
						
			//Selecciono la disponibilidad del libro
			Elements disponibilidad = doc.body().select("dd.disponibilidad");
			
			//Añado la disponibilidad a un array de texto
			ArrayList<String> disponibilidades = new ArrayList<>();
			
			for (Element disp : disponibilidad) {
				String textoDisponib = disp.text();
				textoDisponib = textoDisponib.replace(" - Consulta disponibilidad aquí" , "");
				disponibilidades.add(textoDisponib);
			}
			
			//Creo las ofertas y las añado al Array de Ofertas
			for (int i=0; i<links.size(); i++) {
				Oferta oferta = new Oferta();
				
				oferta.setLugar(lugar);
				oferta.setPrecio(precios.get(i));
				oferta.setUrl(links.get(i));
				oferta.setDisponibilidad(disponibilidades.get(i));
				
				ofertas.add(oferta);
			}
			
			//Creo la oferta más barata comparando todas las ofertas
			Oferta ofertaBarata = this.comparar(ofertas);
			
			if (ofertaBarata == null) {
				return null;
			}
			
			//Nos adentramos en la url de la oferta mas barata para conseguir más información
			docProfundo = Jsoup.connect(ofertaBarata.getUrl()).timeout(100000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36").get();
			
			//Selecciono el ISBN
			Element isbn = docProfundo.body().select("dt:contains(ISBN)").first().nextElementSibling();
			String isbnT = isbn.text();
			isbnT = isbnT.replace("-", "").trim();
			
			//Selecciono la editorial
			Element editorial = docProfundo.body().selectFirst("dd.editorial a");
			
			//Selecciono la fecha
			Element fecha = docProfundo.body().selectFirst("dt:contains(Año de edición)").nextElementSibling();
					
			//Selecciono el título
			Element titulo = docProfundo.body().selectFirst("h1#titulo");
			
			//Selecciono el autor o autores
			Elements authors = docProfundo.body().select("p#autor");
			ArrayList<String> autores = new ArrayList<>();
			for (Element e: authors) {
				String autor = e.text();
				autores.add(autor);
			}
						
			//Selecciono las páginas
			Element paginas = docProfundo.body().selectFirst("dt:contains(Páginas)").nextElementSibling();
			
			//Selecciono la url de la imagen
			Element imagen = docProfundo.body().selectFirst("#detportada");
							
			//Creo el libro
			libro = new Libro (titulo.text(), isbnT, autores, editorial.text(), fecha.text(), paginas.text(), ofertaBarata.getUrl(), imagen.attr("src"), ofertaBarata);
						

		} catch (IOException e) {
			//Si el connect falla, se lanza un IOException
			e.printStackTrace();
		}
		
		//Devuelvo el libro
		return libro;
	
	}
	
	
/***********************************METODOS INTERNOS A LA CLASE********************************************************/	
	
	//Metodo para convertir el formato de precio a Float
	private float conversor(String precio) {
		return Float.parseFloat(precio.replaceAll("[^\\d.]", "").replace(",", "."));
	}
	
	//Método para comparar los precios
	private Oferta comparar(ArrayList<Oferta> ofertas) {
		
		//Selección de la oferta más barata 
		Oferta ofertaBarata = null;
		//Selección del precio más barato (empezamos por el primero para comparar mása adelante)
		float precioBarato = Float.MAX_VALUE;
		
		//Comparamos la primera oferta del array de Ofertas con el resto de ofertas, pero además el libro tiene que estar disponible
		for (Oferta o: ofertas) {
			if(!o.getDisponibilidad().contains("Sin stock") && !o.getDisponibilidad().contains("No disponible") && !o.getDisponibilidad().contains("Reimpresión") && !o.getDisponibilidad().contains("Agotado")) {
				float precioAhora = this.conversor(o.getPrecio());
				
				if(precioAhora <= precioBarato) {
					ofertaBarata = o;
					precioBarato = precioAhora;
				}
			}
		}
		
		return ofertaBarata;
	}
		
}
