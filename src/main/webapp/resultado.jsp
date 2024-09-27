<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="radarbook.Libro.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RadarBook</title>
    <link rel="icon" href="icono.ico" type="image/x-icon">
    <link rel="shortcut icon" href="icono.ico" type="image/x-icon">
    <link rel="stylesheet" href="styles.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Sedan+SC&display=swap" rel="stylesheet">
</head>
<body>
    <header>
        <img id="logo_principal" src="Imagenes/logoHeader.png"></img>
        <div id="titulo_header">
            <h1 id="titulo">RadarBook</h1>
            <h2 id="subtitulo">el mejor comparador de libros</h2>
        </div>
        <nav id="menu_header">
            <ul>
                <li class="opcion_menu"><a href="index.html">Inicio</a></li>
            </ul>
        </nav>
    </header>
    <main>
       <button id="toggleButton" aria-label="Minimizar/Maximizar formulario">-</button>
       <form id="searchForm" action="/ServletForm" method="GET">
        	<label>
        		Búsqueda por Nombre:
            	<input type="text" name="searchNameInput" id="searchNameInput" placeholder="Ingrese título" aria-label="Buscar por título">
            </label>
           <label>
            	Búsqueda por ISBN:
            	<input type="text" name="searchISBNInput" id="searchISBNInput" placeholder="Ingrese ISBN" aria-label="Buscar ISBN">
            </label>
            <button type="submit" value="enviarInfo">Buscar</button>
        </form>
        <h1 id=titulo_pag>RESULTADOS</h1>
        <div id="resultados">
			<c:if test="${libro1 != null}">
	  			 <div class="contenedor_libro">
	    		    <!-- Mostrar información del libro 1 -->
	    		    <h2>Resultado de la API de Amazon</h2> 
	        
			        <img src="${libro1.imagen}" height="400px" />
	        
			        <h4>Título: ${libro1.titulo}</h4>  
			        <p>Isbn: ${libro1.ISBN}</p> 
	        
			        <c:forEach var="autor" items="${libro1.autores}">
			            <c:out value="Autor/a: ${autor}"/> 
			        </c:forEach>
			
	    		    <p>Editorial: ${libro1.editora}</p>
	    		    <p>Páginas: ${libro1.paginas}</p>
	    		    <p>Fecha: ${libro1.fecha}</p>
	    		    <p>Valoracion: ${libro1.valoracion}</p>
		    	    <p>¡Compra Ya! <a href="${libro1.oferta.url}"><b>${libro1.oferta.disponibilidad}</b></a></p>
		    	    <p id="precio">Precio: ${libro1.oferta.precio}</p>
	    		</div>
			</c:if>
			
			<c:if test="${libro2 != null}">
	    		<div class="contenedor_libro">
	        		<h2>Resultado Librería San Pablo</h2>
	        
	        		<img src="${libro2.imagen}" />
	        
	      		  	<h4>Título: ${libro2.titulo}</h4>    
	      		  	<p>Isbn: ${libro2.ISBN}</p> 
		    
	        		<c:forEach var="autor" items="${libro2.autores}">
	        		    <c:out value="Autor/a: ${autor}"/>
	    	    	</c:forEach>
		
	    	    	<p>Editorial: ${libro2.editora}</p>
		        	<p>Páginas: ${libro2.paginas}</p> 
	        		<p>Fecha: ${libro2.fecha}</p>
	    	    	<p>¡Compra Ya! <a href="${libro2.oferta.url}"><b>${libro2.oferta.disponibilidad}</b></a></p>
	    	    	<p id="precio">Precio: ${libro2.oferta.precio}</p>
	    		</div>
			</c:if>
			
			<c:if test="${libro3 != null}">
			    <div class="contenedor_libro">
			        <!-- Mostrar información del libro 3 -->
			        <h2>Resultado de eBay</h2>
			        
			        <img src="${libro3.imagen}" />
			        
			        <h4>Título: ${libro3.titulo}</h4>
			        <p>Isbn: ${libro3.ISBN}</p>
			        
			        <c:forEach var="autor" items="${libro3.autores}">
			            <c:out value="Autor/a: ${autor}"/>
			        </c:forEach>
			
			        <p>Editorial: ${libro3.editora}</p>
			        <p>Páginas: ${libro3.paginas}</p>
			        <p>¡Compra Ya! <a href="${libro3.oferta.url}"><b>${libro3.oferta.disponibilidad}</b></a></p>
			        <p id="precio">Precio: ${libro3.oferta.precio}</p>
			    </div>
			</c:if>
		</div>
		
		<div id="reseñas">
			
			<h2>Reseñas proporcionadas por amazon</h2>
			
			<c:choose>
				<c:when test="${libro1 != null && !libro1.reseñas.isEmpty()}">
					<div class="contenedor_reseñas">
        				<c:forEach var="reseña" items="${libro1.reseñas}">
            				<div class="reseña">
                				<img src="${reseña.logo}" alt="Logo de ${reseña.who}" />
                				<h3>${reseña.tituloReseña}</h3>
                				<p><strong>${reseña.who}</strong></p>
                				<p>${reseña.texto}</p>
								<p>Estrellas: ${reseña.estrellas}</p>
            				</div>
        				</c:forEach>
    				</div>
				</c:when>
			</c:choose>
			
		</div>

		

    </main>
    <footer>
            <p>&copy; 2024 RadarBook SL</p>
            <p><a href="Documentacion.pdf">¿Cómo se hizo?</a></p>
            <p><a href="contacto.html">¿Quiénes somos?</a></p>
    </footer>
    <script src="toggleForm.js"></script>
</body>
</html>