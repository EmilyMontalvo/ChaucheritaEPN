<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mis Movimientos</title>
    <link rel="shortcut icon"
	href="${pageContext.request.contextPath}/images/icon1.png"
	type="image/x-icon">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css" /> 
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/predeterminados.css" /> 
    
</head>
<body>
    
     <!-- BANNER -->
    <%@include file = '../templates/banner.jsp' %>
    
    <!-- VER MIS MOVIMIENTOS PANTALLA -->
    
        <br> <br>
    <p class="h3 ms-5">MIS MOVIMIENTOS</p>
    <br>
    <div class="container ">
		<form action="${pageContext.request.contextPath}/GestionCuentaController" method="GET" class="mb-4 d-flex align-items-center">
	    	<input type="hidden" name="ruta" value="verMovimientosPantallaPorMes">
	    	<label for="mesSeleccionado" class="form-label me-3">Selecciona un mes:</label>
	    	<select id="mesSeleccionado" name="mesSeleccionado" class="form-select">
	            <option value="1">Enero</option>
	            <option value="2">Febrero</option>
	            <option value="3">Marzo</option>
	            <option value="4">Abril</option>
	            <option value="5">Mayo</option>
	            <option value="6">Junio</option>
	            <option value="7">Julio</option>
	            <option value="8">Agosto</option>
	            <option value="9">Septiembre</option>
	            <option value="10">Octubre</option>
	            <option value="11">Noviembre</option>
	            <option value="12">Diciembre</option>
	        </select>
	        <br>
	        <button type="submit" class="btn text-white bg-1 ms-5 font-w700">Mostrar Movimientos</button>
	    </form>
	</div>  
    
    <br>
    <div class="container text-center mb-5" >
        <table class="table table-hover">
            <thead>
                <tr>
                    
                     <th scope="col">FECHA</th>
                    <th scope="col">ORIGEN </th>
                    <th scope="col">DESTINO</th>
                    <th scope="col">MOVIMIENTO </th>
                    <th scope="col">MONTO</th>
                    <th scope="col">DESCRIPCIÓN</th>
                    
                </tr>
            </thead>
            <tbody>
            
            <c:forEach items="${listaMovimientos}" var="movimiento">
            <tr>
                
                <td>${movimiento.fecha}</td>
               
                <td>
                    <c:choose>
                        <c:when test="${not empty movimiento.cuentaOrigen}">
                            ${movimiento.cuentaOrigen.nombreCuenta}
                        </c:when>
                        <c:otherwise>
                            null
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${movimiento.cuentaDestino.nombreCuenta}</td>
                 <td>${movimiento.getClass().simpleName}</td>
                <td>${movimiento.monto}</td>
                <td>${movimiento.descripcion}</td>
                
                <td>
                    <form action="${pageContext.request.contextPath}/GestionCuentaController" method="POST">
                        <input type="hidden" name="ruta" value="eliminarMovimiento" />
                        <input type="hidden" name="movimientoId" value="${movimiento.id}" />
                        <button type="submit" class="btn btn-danger">Eliminar</button>
                    </form>
                </td>
                
            </tr>
        </c:forEach>
            
        </tbody>
        
        </table>
        </div>

    
    
    <!-- BOOTSTRAP Y FONTAWESOME -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
        crossorigin="anonymous"></script>
    <script src="https://kit.fontawesome.com/87553d44ff.js" crossorigin="anonymous"></script>
    
    
    
</body>
</html>