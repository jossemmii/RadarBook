package radarbook;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet (
		name = "ServletForm",
		urlPatterns = {"/ServletForm"}
)

public class ServletForm extends HttpServlet {
	
	//Para evitar problemas de compatibilidad de versiones
	private static final long serialVersionUID = 1L;
	
	apiRain api= new apiRain();
	sanPablo scrapper_san_pablo = new sanPablo();
	ebay scrapper_ebay = new ebay();
	String busqueda_nombre="";
	String busqueda_ISBN="";
	Libro libro1 = new Libro();
	Libro libro2 = new Libro();
	Libro libro3 = new Libro();
	
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Búsqueda de libros basado en una entrada del usuario
        busqueda_nombre = request.getParameter("searchNameInput");
        busqueda_ISBN = request.getParameter("searchISBNInput");
        
        if (busqueda_nombre != null && !busqueda_nombre.isEmpty()) {
            //Obtener libros con nombre
            try {
            	libro1 = api.devolverLibro(busqueda_nombre);
                libro2 = scrapper_san_pablo.devolverLibro(busqueda_nombre);
                libro3 = scrapper_ebay.devolverLibro(busqueda_nombre);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (busqueda_ISBN != null && !busqueda_ISBN.isEmpty()) {
            //Obtener libros con ISBN
            try {
            	libro1 = api.devolverLibro(busqueda_ISBN);
                libro2 = scrapper_san_pablo.devolverLibro(busqueda_ISBN);
                libro3 = scrapper_ebay.devolverLibro(busqueda_ISBN);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Manejo de caso donde no hay entrada
            request.setAttribute("error", "No se proporcionó un nombre o ISBN válido.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
       
        // Agregar libros al request si están disponibles
        if (libro1 != null) {
        	request.setAttribute("libro1", libro1);
        }
        if (libro2 != null) {
            request.setAttribute("libro2", libro2);
        }
        if (libro3 != null) {
            request.setAttribute("libro3", libro3);
        } 
        if(libro1 == null && libro2 ==null && libro3==null) {
        	request.setAttribute("error", "No se encontró el libro especificado.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        response.setContentType("text/plain:charset=UTF-8");
        
        // Se manda la solicitud al JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/resultado.jsp");
        
        try {
        	dispatcher.forward(request, response);
        } catch(ServletException e){
        	e.printStackTrace();
        }
        
        libro1 = null;
        libro2 = null;
        libro3 = null;
        busqueda_nombre=null;
    	busqueda_ISBN=null;
    	api = new apiRain();
     }
}