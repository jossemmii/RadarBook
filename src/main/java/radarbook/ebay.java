package radarbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ebay {

	static String lugar = "Ebay";
	Libro libro;
	static String disponibilidad = "Cómpralo ya!";
	
	//Método con el que devuelvo un libro
	public Libro devolverLibro(String aBuscar) {
		
		String direccion = "https://www.ebay.es/sch/i.html?_from=R40&_trksid=p2334524.m570.l1313&_nkw=" + aBuscar + "&_sacat=267&_fsrp=1&Idioma=Espa%25C3%25B1ol&rt=nc&LH_BIN=1&_dcat=171243&LH_PrefLoc=1&LH_ItemCondition=1000&LH_SellerType=2";
		
		Document doc = this.devolverParteDocumento(direccion);
		
		ArrayList<Oferta> ofertas = new ArrayList<>();
		ofertas = this.ofertasDisponibles(doc);
				
		//Obtengo la oferta más barata
		Oferta ofertaBarata = this.devolverOfertaBarata(ofertas, doc);
		
		//Si la ofertaBarata es null, se devuelve null y no se lleva a cabo la funcionalidad
		if (ofertaBarata == null) {
			return null;
		}
		
		Libro libro = this.construirLibro(ofertaBarata);
				
		return libro;
	}
	
	
	/***********************************METODOS INTERNOS A LA CLASE********************************************************/	
	
	//Método para establecer la conexióny devolver html
	private Document conexionResultado(String direccion) {
		
		OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(direccion).build();
        Document doc = null;
        
        try(Response response = client.newCall(request).execute()){
        	String html = response.body().string();
	        doc = Jsoup.parse(html);
        }  catch (IOException e) {
            e.printStackTrace();
        }
        
        return doc;
        
	}
	
	//Método con el que devuelvo la parte del html que me interesa (HASTA RESULTADOS CON MENOS PALABRAS COINCIDENTES)
	private Document devolverParteDocumento(String direccion) {
		
		Document doc = this.conexionResultado(direccion);
		Document docNuevo = null;
        
        try{
        	
	        // Buscamos el elemento límite hasta dónde quiero obtener el html
	        Element elementoLimite = doc.selectFirst("li.srp-river-answer.srp-river-answer--REWRITE_START");

	        if (elementoLimite != null) {
	        	// Creo el nuevo documento html vacío, pero con su estructura básica
		        docNuevo = Jsoup.parse("<html><body></body></html>");

		        // Obtenemos el hermano del elemento límite e iteramos hacia atrás hasta que no haya más hermanos
		        Element elementoActual = elementoLimite.previousElementSibling();
		        while (elementoActual != null) {
		            // Agregamos al documento nuevo el elemento actual, con su contenido html
		            docNuevo.body().prepend(elementoActual.outerHtml());
		            // Se mueve el elemento actual al anterior
		            elementoActual = elementoActual.previousElementSibling();
		        }
	        }
	        else
	        	docNuevo = doc;
	        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return docNuevo;
	}

	//Método para transformar String a Float
	private float conversor(String precio) {
		return Float.parseFloat(precio.replaceAll("[^\\d.]", "").replace(",", ".").replace("EUR", "").replace("de envío", "").replace("+", ""));
	}
	
	//Método con el que devuelvo el array de precios disponibles
	private ArrayList<String> preciosDisponibles (Document doc) {
		
		ArrayList<String> preciosBase = new ArrayList<>();
		
		
		Elements prices = doc.select("span.s-item__price");
		
		
		for (Element e : prices) {
			String precioBase = e.text();
			preciosBase.add(precioBase);
		}
		
		return preciosBase;
	}
	
	//Método con el que devuelvo el array de url
	private ArrayList<String> urlDisponibles (Document doc) {
		
		ArrayList <String> enlaces = new ArrayList<>();
		Elements enlacesE = doc.select("a.s-item__link");
		
		for (Element e: enlacesE) {
			enlaces.add(e.attr("href"));
		}
		
		return enlaces;
	}
	
	//Método con el que devuelvo un array de url de Imágenes
	private ArrayList<String> imagenesDisponibles (Document doc) {
		
		ArrayList<String> imagenes = new ArrayList<>();
		Elements imagenesE = doc.select("div.s-item__image-wrapper.image-treatment img");
		
		for (Element e: imagenesE) {
			imagenes.add(e.attr("src"));
		}
		
		return imagenes;
	}
	
	//Método para devolver arrayList de ofertas disponibles
	private ArrayList<Oferta> ofertasDisponibles (Document doc) {
		
		ArrayList<Oferta> ofertas = new ArrayList<>();
		ArrayList<String> precios = new ArrayList<>();
		ArrayList<String> url = new ArrayList<>();
		ArrayList<String> imagenes = new ArrayList<>();
		
		precios = this.preciosDisponibles(doc);
		url = this.urlDisponibles(doc);
		imagenes = this.imagenesDisponibles(doc);
		
		for (int i=0; i<url.size(); i++) {
			Oferta oferta = new Oferta();
			oferta.setDisponibilidad(disponibilidad);
			oferta.setLugar(lugar);
			oferta.setPrecio(precios.get(i).replace("EUR", "€"));
			oferta.setUrl(url.get(i));
			oferta.setImagen(imagenes.get(i));
			
			ofertas.add(oferta);
		}
		
		if (ofertas.size() == 0) {
			return null;
		}
		
		else {
			return ofertas;
		}
	}
	
	//Método para obtener la oferta más barata
	private Oferta devolverOfertaBarata (ArrayList <Oferta> ofertas, Document doc) {
		
		Oferta ofertaBarata = null;
		float precioBarato = Float.MAX_VALUE;
		
		ArrayList<Float> preciosAñadido = new ArrayList<>();
		
		Elements pricesEnvio = doc.select("span.s-item__shipping.s-item__logisticsCost, span.s-item__dynamic.s-item__freeXDays");
		
		for (Element e : pricesEnvio) {
			String precioAñadido = e.text();
			Float precioAñadidoFloat = 0f;
			
			if (precioAñadido.contains("gratis")) {
				precioAñadidoFloat = 0f;
			}
			else {
				precioAñadidoFloat = this.conversor(precioAñadido);
			}
			
			preciosAñadido.add(precioAñadidoFloat);
		}
		
		
		int index = 0;
		
		if (preciosAñadido.size() <= 0) {
			return null;
		}
		else {
			//Obtengo los precios de las ofertas en tipo Float
			for (Oferta oferta : ofertas) {
				
				if (oferta.getUrl().contains("www.ebay.es")) {
					
					if (index < preciosAñadido.size()) {
				        float precioAhora = this.conversor(oferta.getPrecio());
				        float precioAhoraTotal = precioAhora + preciosAñadido.get(index);
				        index += 1;
				        if (precioAhoraTotal <= precioBarato) {
				            ofertaBarata = oferta;
				            precioBarato = precioAhoraTotal;
				        }			
					}
				}
			}
			return ofertaBarata;
		}
	}

	//Método para obtener el html del libro elegido y construir el libro
	private Libro construirLibro(Oferta o) {
		
		Document doc = this.conexionResultado(o.getUrl());
		
		String isbn = this.obtenerIsbn(doc);
		String paginas = this.obtenerPaginas(doc);
		ArrayList<String> autores = this.obtenerAutores(doc);
		String editorial = this.obtenerEditorial(doc);
		String titulo = this.obtenerTitulo(doc);
		String url = o.getUrl();
		String imagen = o.getImagen();
		
		Libro libro = new Libro(titulo, isbn, autores, editorial, paginas, url, o, imagen);  
		
		return libro;
	}
	
	//Método para obtener el isbn
	private String obtenerIsbn (Document doc) {
		
		String isbn = "Isbn no proporcionado por Ebay";

	    // Patrón para buscar el ISBN
	    Pattern pattern = Pattern.compile("\\bISBN\\s(\\d{13})\\b");

	    // Busca elementos que contengan texto
	    Elements elements = doc.select("p, div");
	    for (Element e : elements) {
	        String text = e.text();
	        Matcher matcher = pattern.matcher(text);
	        if (matcher.find()) {
	            isbn = matcher.group();
	            String[] partesIsbn = isbn.split(" ");
	            if (partesIsbn.length > 1) {
	            	isbn = partesIsbn[1];
	            	return isbn;
	            }
	        }
	    }
	    
		return isbn;
	}
	
	//Método para obtener el número de páginas
	private String obtenerPaginas (Document doc) {
		
	    String paginas = "Número de páginas no proporcionado por Ebay";

	    // Patrón para buscar el número de páginas
	    Pattern pattern = Pattern.compile("Número de páginas (\\d+)");

	    // Busca elementos que contengan texto
	    Elements elements = doc.select("p, div");
   	    
	    for (Element e : elements) {
	        String text = e.text();
	        Matcher matcher = pattern.matcher(text);
	        if (matcher.find()) {
	            paginas = matcher.group(1);
	            return paginas;
	        }
	    }
	    
	    //Si no se ha devuelto en el anterior for, quiere decir que la información se va a encontrar en un iframe
	    Element iframeEl = doc.selectFirst("iframe#desc_ifr");
	    String iframeUrl = iframeEl.attr("src");
	    Document docIframe = this.conexionResultado(iframeUrl);
	    
	    //Selecciono las paginas en el iframe
	    Element paginasIframe = docIframe.selectFirst("p:containsOwn(Páginas / Pages:)");
	    if (paginasIframe != null) {
		    paginas = paginasIframe.text();
		    String [] partes = paginas.split(": ");
		    if (partes.length > 1) {
		    	paginas = partes[1];
		    	return paginas;
		    }
	    }
	    return paginas;
	}

	//Método para obtener los autores
	private ArrayList<String> obtenerAutores (Document doc) {
		
		ArrayList<String> noAutores = new ArrayList<>();
		String no = "Autor/Autores no proporcionado por Ebay";
		noAutores.add(no);
		ArrayList<String> autores = new ArrayList<>();
		
		//Patrón para buscar los autores
	    Pattern pattern = Pattern.compile("Autor (.+?) Número de páginas");
	    
	    //Busca elementos que contengan texto
	    Elements elements = doc.select("p, div");
   	    
	    for (Element e : elements) {
	        String text = e.text();
	        Matcher matcher = pattern.matcher(text);
	        if (matcher.find()) {
	        	String encontrado = matcher.group(1);
	        	//autores.add(encontrado);
	        	String[] partes = encontrado.split(";");
	        	for (String parte : partes) {
	        		parte = parte.trim();
	        		if (!parte.isEmpty()) {
	        			autores.add(parte);
	        		}
	        	}
	        	return autores;
	        }
	    }
	    
	    //Si no encuentra, buscará en el iframe
	    Element iframeEl = doc.selectFirst("iframe#desc_ifr");
	    String iframeUrl = iframeEl.attr("src");
	    Document docIframe = this.conexionResultado(iframeUrl);
	    
	    //Selecciono las paginas en el iframe
	    Element autoresIframe = docIframe.selectFirst("p:containsOwn(Autor(es) / Author(s):)");
	    if (autoresIframe != null) {
	    	String autor = autoresIframe.text(); 
		    String [] partes = autor.split(": ");
		    if (partes.length > 1) {
		    	autor = partes[1];
		    	autores.add(autor);
		    	return autores;
		    }
	    }
	    
	    return noAutores; 
	}
	
	//Método para obtener la editorial
	private String obtenerEditorial (Document doc) {
		
		String editorial = "Editorial no proporcionada por ebay";
		
		// Patrón para buscar el número de páginas
	    Pattern pattern = Pattern.compile("Editorial (.+?) Marca");
	    Pattern pattern2 = Pattern.compile("Editor (.+?) ISBN");

	    // Busca elementos que contengan texto 
	    Elements elements = doc.select("p, div");
   	    
	    for (Element e : elements) {
	        String text = e.text();
	        Matcher matcher = pattern.matcher(text);
	        Matcher matcher2 = pattern2.matcher(text);
	        if (matcher.find()) {
	            editorial = matcher.group(1);
	            return editorial;
	        }
	        if (matcher2.find()) {
	        	editorial = matcher2.group(1);
	        	return editorial;
	        }
	    }
	    
	    //Si no encuentra, buscará en el iframe
	    Element iframeEl = doc.selectFirst("iframe#desc_ifr");
	    String iframeUrl = iframeEl.attr("src");
	    Document docIframe = this.conexionResultado(iframeUrl);
	    
	    //Selecciono las paginas en el iframe
	    Element editorialIframe = docIframe.selectFirst("p:containsOwn(Editorial / Publisher:)");
	    if (editorialIframe != null) {
		    editorial = editorialIframe.text();
		    String [] partes = editorial.split(": ");
		    if (partes.length > 1) {
		    	editorial = partes[1];
		    	return editorial;
		    }
	    }
	    return editorial;
	}

	//Método para obtener el título
	private String obtenerTitulo (Document doc) {
		
		String titulo = "";
		Element tituloE = doc.selectFirst("h1.x-item-title__mainTitle");
		
		titulo = tituloE.text();
		
		return titulo;
	}
	
}