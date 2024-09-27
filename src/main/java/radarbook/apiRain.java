package radarbook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class apiRain {
	
	String lugar = "Amazon";
	Libro libroA = new Libro();
	
	public Libro devolverLibro(String aBuscar) {
		
		String urlAPI = "https://api.rainforestapi.com/request?api_key=7504C5C8D4294783A94A0AB7855D9E5D&type=search&amazon_domain=amazon.es&search_term=" 
				+ aBuscar.replaceAll("\\s+","+") + "&category_id=599365031&currency=eur";
		int codigoRespuesta = 0;
		Oferta o = new Oferta();
		
		try {
						
			//Inicio de la conexion
			URL url = new URL(urlAPI);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			
			//Solicitar la conexión a través de GET y realizar conexion
			conn.setRequestMethod("GET");
			conn.connect();
			
			//Comprobamos que la conexión haya sido válida si el código de respuesta es el 200
			codigoRespuesta = conn.getResponseCode();
			
			
			//Si el código de respuesta es 200, continuamos con el proceso
			if (codigoRespuesta == 200) {
				
				//Es necesario leer la respuesta de la solicitud HTTP y almacenarla en una cadena de texto
				//Scaner para poder leer la respuesta
				BufferedReader scaner = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				StringBuilder respuesta = this.respuesta(scaner);
				
	            //Construimos el objeto json
	            JSONObject objetoJSON = new JSONObject(respuesta.toString());
				
				// En el json devuelto los libros se encuentran en search_results, asique accedemos a ese array
				JSONArray libros = objetoJSON.getJSONArray("search_results");
				
				//Ahora para el primer libro del array vamos a crear su Objeto oferta, y su objeto Libro
				JSONObject primerResultado = libros.getJSONObject(0);
	
				//Creamos la oferta
				o = this.construirOferta(primerResultado);
				
				if (o == null) {
					return null;
				}

				//Construyo el libro
				libroA = this.construirLibro(primerResultado, o);
				
			}
			
		} catch (Exception e) {
			//Si el connect falla, se lanza un IOException
			e.printStackTrace();
		}
		
		//Devuelvo el libro
		return libroA;
		
	}
	
/***********************************METODOS INTERNOS A LA CLASE********************************************************/
		
	//Método para generar y leer respuesta
	private StringBuilder respuesta(BufferedReader scaner) {
		
		StringBuilder respuesta = new StringBuilder();
		
		try {
			
			//Cadena de texto que almacena cada línea de la respuesta
	        String linea;
	        
	        //Por cada línea que se lea y no esté vacía, se almacena en el response
	        while ((linea = scaner.readLine()) != null) {
	            respuesta.append(linea);
	        }
	        
	        //Se cierra el scaner al terminar
	        scaner.close();	
	        
		} catch (Exception e) {
			//Si el connect falla, se lanza un IOException
			e.printStackTrace();
		}
		
		return respuesta;
	}
	
	//Método para construir la oferta
	private Oferta construirOferta(JSONObject primerResultado) {
		
		//Completamos oferta
		Oferta o = new Oferta();
		
		//Obtenemos price
		if (!primerResultado.has("price") || !primerResultado.has("link")) {
			return null;
		}
		JSONObject price = primerResultado.getJSONObject("price");
		double priceValue = price.getDouble("value");
		String priceString = Double.toString(priceValue);
		String priceSymbol = price.getString("symbol");
		
		//Obtenemos link
		String link = primerResultado.getString("link");
		
		//Añadimos atributos de la oferta
		o.setUrl(link);
		o.setPrecio(priceString + priceSymbol);
		o.setLugar(lugar);
		//Los resultados de esta api siempre van a estar disponibles, sino, no se muestra nada
		o.setDisponibilidad("Disponible");
		
		//Devolvemos la oferta
		return o;
	}
	
	//Método para establecer la conexión y scrapear dentro del libro
	private Document scrapeoLibro(String url) {
				
	        try {
	            String urlOficial = "https://api.scraperapi.com/?api_key=d27c5b38aacccacf1e3759648f61861e&url=" + url;
	            URL urlForGetRequest = new URL(urlOficial);
	            String readLine = null;
	            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
	            conection.setRequestMethod("GET");
	            int responseCode = conection.getResponseCode();
	            Document doc = null;
	            if (responseCode == HttpURLConnection.HTTP_OK) {
	              BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	              StringBuffer response = new StringBuffer();
	              while ((readLine = in.readLine()) != null) {
	                response.append(readLine);
	              }
	              String respuesta = response.toString();
	              doc = Jsoup.parse(respuesta);
	              in.close();

	              return doc;
	              
	          }
	        }catch (Exception ex) {
	            ex.printStackTrace();
	          }
	        
	          return null;
}
	
	//Método para obtener las reseñas
	private ArrayList<Reseña> obtenerReseñas(Document doc) {
		
		Elements reviews = doc.select("[data-hook=top-customer-reviews-widget] [data-hook=review]");
			
		ArrayList<Reseña> reseñas = new ArrayList<>();
		
		for(Element review : reviews) {
			
			Element logosE = review.selectFirst(".a-profile .a-profile-avatar img");
			Element autoresE = review.selectFirst(".a-profile-name");
			Elements titles = review.select("a[data-hook=review-title]");
			Element texts = review.selectFirst("span[data-hook=review-body]"); 
			Element stars = review.selectFirst("a[data-hook=review-title] span.a-icon-alt");
			
			String src = logosE.attr("src");
			
			String aut = autoresE.text();
			
			String textoUltimoSpan = "";
			
			for (Element tit : titles) {
				Element ultimoSpan = tit.select("span").last();
				if (ultimoSpan != null) {
					textoUltimoSpan = ultimoSpan.text();
				}
			}
			
			String textoReview = texts.text(); 
			textoReview = textoReview.replace("Leer más", "");
			
			String textoStars = stars.text();
			
			Reseña r1 = new Reseña(src, aut, textoUltimoSpan, textoReview, textoStars);
			reseñas.add(r1);
			
			
		}
		
		return reseñas;
	}
	
	//Método para obtener la fecha del libro
	private String obtenerFecha(Document doc) {
        Element date = doc.selectFirst("span#productSubtitle");
        if (date != null) {
            String fecha = date.text();
            String[] partesFecha = fecha.split("–");

            if (partesFecha.length > 1) {
                fecha = partesFecha[1].trim();         
            } 
    		
    		return fecha;
        }
        
        return null;

	}
	
	//Método para obtener los autores
	private ArrayList<String> obtenerAutores(Document doc) {
		Elements authors = doc.select("span.author.notFaded");
		ArrayList<String> autores = new ArrayList<>();
		for (Element au : authors) {
			String autor = au.text().trim();
			autor = autor.replace(" (Autor)", "");
			autor = autor.replace(",", "");
			autores.add(autor);
		}
		
		System.out.println(autores);
		return autores;
		
	}
	
	//Método para obtener la editorial ,el número de páginas y el isbn
	private ArrayList<String> obtenerEditorialPaginasIsbn (Document doc) {
	
		        	ArrayList<String> aDevolver = new ArrayList<>();
					
			        Elements listaElementos = doc.select("#detailBullets_feature_div #detailBulletsWrapper_feature_div ul li");
			        String textoCompleto = "";
			        String editorial = "";
			        String paginas = "";
			        String isbn = "";

			        for (Element e : listaElementos) {
			        	textoCompleto = e.selectFirst("span:not(.a-text-bold)").text();
			        	if (textoCompleto.contains("Editorial")) {
			        		editorial = textoCompleto;
			        		String[] partes = editorial.split(":");
			        		if (partes.length > 1) {
			        			editorial = partes[1].trim().replaceFirst("\\s", "");
			        			String[] partesInternas = editorial.split(";");
			        			if (partesInternas.length > 1) {
			        				editorial = partesInternas[0].trim();
			        			}
			        		}
			        	}
			        	
			        	if (textoCompleto.contains("páginas")) {
			        		paginas = textoCompleto;
			        		String[] partes = paginas.split(":");
				        		if (partes.length > 1) {
				        			paginas = partes[1].trim().replaceFirst("\\s", "");
				        		}
			        	}
			        	
			        	if (textoCompleto.contains("ISBN-13")) {
			        		isbn = textoCompleto;
			        		String[] partes = isbn.split(":");
				        		if (partes.length > 1) {
				        			isbn = partes[1].trim().replaceFirst("\\s", "");
				        			if (isbn.contains("-")) {
				        				isbn = isbn.replace("-", "");
				        			}
				        		}
			        	}
			             
			        }
			        
			        aDevolver.add(editorial);
			        aDevolver.add(paginas);
			        aDevolver.add(isbn);
			        return aDevolver;
			        		        
	}
	
	
		
	//Método para construir el libro y obtener algunos atributos
	private Libro construirLibro (JSONObject primerResultado, Oferta o) {
				
		//Obtengo el titulo
		String titulo = "";
		String imagen = "";
		String valoracion = "";
		
		if (primerResultado.has("title")) {
			titulo = primerResultado.getString("title");
		}
		else
			titulo = "No proporcionada por apiRain";
		
		//Obtengo la imagen
		if (primerResultado.has("image")) {
			imagen = primerResultado.getString("image");
		}
		else
			imagen = "No proporcionada por apiRain";
		
		
		//Obtengo la valoracion
		if (primerResultado.has("rating")) {
			double rating = primerResultado.getDouble("rating");
			valoracion = String.valueOf(rating) + " estrellas";
		}
		else
			valoracion = "No proporcionada por apiRain";
		
		//Inicio scrapeo
		String url = o.getUrl();
		Document doc = this.scrapeoLibro(url);
		
		//Obtengo las reseñas
		ArrayList<Reseña> reseñas = this.obtenerReseñas(doc);
		
	    //Obtengo los autores
		ArrayList<String> autores = this.obtenerAutores(doc);

				
		//Obtengo las páginas, la editorial y el isbn, que se almacenan en un array, posicion 0 editorial, posición 1 páginas, posicion 2 isbn

		ArrayList<String> datos = this.obtenerEditorialPaginasIsbn(doc);
		String editorial = datos.get(0);
		String paginas = datos.get(1);
		String isbn = datos.get(2);
				
		//Obtengo la fecha
        String fecha = this.obtenerFecha(doc);
        
        if (fecha == null) {
        	return null;
        }
	
		//Completamos libro
		Libro l2 = new Libro(titulo, isbn, autores, editorial, fecha, paginas, o.getUrl(), imagen, valoracion, o, reseñas);
		
		//Devuelvo el libro
		return l2;
		
	}
	
}
