package controlador.gestion;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelo.dao.CuentaDAO;
import modelo.dao.DAOFactory;
import modelo.dao.EgresoDAO;
import modelo.dao.IngresoDAO;
import modelo.dao.MovimientoDAO;
import modelo.dao.TransferenciaDAO;
import modelo.entidades.Cuenta;
import modelo.entidades.Egreso;
import modelo.entidades.Ingreso;
import modelo.entidades.Movimiento;
import modelo.entidades.TipoCuenta;
import modelo.entidades.Transferencia;
import modelo.entidades.Usuario;
import modelo.jpa.JPACuentaDAO;


@WebServlet("/GestionCuentaController")
public class GestionCuentaController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GestionCuentaController() {
		super();
	
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.ruteador(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.ruteador(request, response);
	}

	private void ruteador(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ruta = (request.getParameter("ruta") == null) ? "inicio" : request.getParameter("ruta");

		switch (ruta) {
		case "verMovimientosPantalla":
			this.verMovimientosPantalla(request, response);
			break;
		case "verMovimientosCuentaPantalla":
			this.verMovimientosCuentaPantalla(request, response);
			break;
		case "verMovimientosPantallaPorMes":
			this.verMovimientosPantallaPorMes(request, response);
			break;
		case "crearCuentaPantalla":
			this.crearCuentaPantalla(request, response);
			break;
		case "transferirPantalla":
			this.transferirPantalla(request, response);
			break;
		case "ingresoPantalla":
			this.registrarIngresoPantalla(request, response);
			break;
		case "egresoPantalla":
			this.registrarEgresoPantalla(request, response);
			break;
		case "ajustarSaldoPantalla":
			this.ajustarSaldoPantalla(request, response);
			break;
		case "crearCuenta":
			this.crearCuenta(request, response);
			break;
		case "transferir":
			this.transferir(request, response);
			break;
		case "ingreso":
			this.registrarIngreso(request, response);
			break;
		case "egreso":
			this.registrarEgreso(request, response);
			break;
		case "ajustarSaldo":
			this.ajustarSaldo(request, response);
			break;
		case "eliminarMovimiento":
			this.eliminarMovimiento(request, response);
			break;
		case "error":
			break;
		default:
			break;
		}
	}
	
	private void eliminarMovimiento(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        // Obtener el ID del movimiento a eliminar desde el formulario
	        int movimientoId = Integer.parseInt(request.getParameter("movimientoId"));
	        
	        // Lógica para eliminar el movimiento
	        MovimientoDAO movimientoDAO = modelo.dao.DAOFactory.getFactory().getMovimientoDAO();
	        movimientoDAO.deleteById(movimientoId); // Ajusta esto a tu implementación
	        
	        // Redirigir a la página de movimientos actualizada
	        response.sendRedirect("GestionCuentaController?ruta=verMovimientosPantalla");
	    
	}
	
	
	private void verMovimientosCuentaPantalla(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
	
		 int cuentaId = Integer.parseInt(request.getParameter("cuentaId")); 

		    // 1. Obtener la cuenta por su ID utilizando el m�todo getById del JPAGenericDAO
		    Cuenta cuenta = modelo.dao.DAOFactory.getFactory().getCuentaDAO().getById(cuentaId);

		    // 2. Obtener los movimientos de la cuenta utilizando el m�todo obtenerMovimientosPorCuenta del JPAMovimientoDAO
		    List<Movimiento> listaMovimientos = modelo.dao.DAOFactory.getFactory().getMovimientoDAO().getMovimientosByCuenta(cuenta);

		    // 3. Enviar la cuenta y los movimientos a la vista
		    request.setAttribute("cuenta", cuenta);
		    request.setAttribute("listaMovimientos", listaMovimientos);
		    request.getRequestDispatcher("jsp/verMovimientosCuenta.jsp").forward(request, response);
	}

	private void ajustarSaldoPantalla(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
	    String cuentaIdParam = request.getParameter("cuentaId");
	    int cuentaId = Integer.parseInt(cuentaIdParam);
	    
	    // Aqu� podr�as realizar la obtenci�n de la cuenta a partir del ID
	    // Esto es solo un ejemplo, reempl�zalo con tu l�gica real
	    DAOFactory daoFactory = modelo.dao.DAOFactory.getFactory();
	    CuentaDAO cuentaDAO = daoFactory.getCuentaDAO();
	    Cuenta cuenta = cuentaDAO.getById(cuentaId);

	    // Setear los atributos en la solicitud para que la p�gina JSP los utilice
	    request.setAttribute("cuenta", cuenta);

	    // Luego, redirige a la pantalla de ajuste de saldo
	    request.getRequestDispatcher("jsp/ajustarSaldo.jsp").forward(request, response);
	}
	
	
	
	private void verMovimientosPantalla(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//1. Get the authenticated user from the session
	    HttpSession session = request.getSession();
	    Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuarioLogeado");
	    int idUsuarioAutenticado = usuarioAutenticado.getId();
	    
	 // 2. Get the movements of the authenticated user using the getMovimientosByUsuario method
	    List<Movimiento> listaMovimientos = modelo.dao.DAOFactory.getFactory().getMovimientoDAO().getMovimientosByUsuario(usuarioAutenticado);

	 // 3. Send the moves to the view
	    request.setAttribute("listaMovimientos", listaMovimientos);
	    request.getRequestDispatcher("jsp/verMovimientos.jsp").forward(request, response);
	}
	
	
	private void verMovimientosPantallaPorMes(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//1. Get the authenticated user from the session
	    HttpSession session = request.getSession();
	    Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuarioLogeado");
	    int idUsuarioAutenticado = usuarioAutenticado.getId();
	    // get the selected month
	    String mesSeleccionadoStr = request.getParameter("mesSeleccionado");
	    int mesSeleccionado = Integer.parseInt(mesSeleccionadoStr);
	    
	    // 2. Get the movements of the authenticated user using the getMovimientosByUsuario method
	    List<Movimiento> listaMovimientos = modelo.dao.DAOFactory.getFactory().getMovimientoDAO().getMovimientosByUsuarioAndMonth(usuarioAutenticado, mesSeleccionado);

	 // 3. Send the moves to the view
	    request.setAttribute("listaMovimientos", listaMovimientos);
	    request.getRequestDispatcher("jsp/verMovimientos.jsp").forward(request, response);
	}
	
	
	
	private void crearCuentaPantalla(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		 // Obtener los tipos de cuenta desde la enumeraci�n TipoCuenta
	    TipoCuenta[] tiposCuenta = TipoCuenta.values();

	    // Establecer los tipos de cuenta como un atributo de la solicitud
	    request.setAttribute("tiposCuenta", tiposCuenta);

	    // Redirigir a la vista JSP
	    request.getRequestDispatcher("jsp/crearCuenta.jsp").forward(request, response);
	}
	
	private void transferirPantalla(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
	    Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuarioLogeado");

		  // 1.- Obtener los datos necesarios del modelo
	    DAOFactory daoFactory = modelo.dao.DAOFactory.getFactory();
	    CuentaDAO cuentaDAO = daoFactory.getCuentaDAO();
	    List<Cuenta> listaCuentas = cuentaDAO.getCuentasByPropietario(usuarioAutenticado); 

	    // 2.- Agregar los datos al �mbito de la solicitud
	    request.setAttribute("listaCuentas", listaCuentas);

	    // 3.- Redirigir al JSP de transferencia
	    request.getRequestDispatcher("jsp/transferencia.jsp").forward(request, response);
	}
	
	private void registrarIngresoPantalla(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		  // 1.- Obtener los datos necesarios del modelo
		HttpSession session = request.getSession();
	    Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuarioLogeado");
		
	    DAOFactory daoFactory = modelo.dao.DAOFactory.getFactory();
	    CuentaDAO cuentaDAO = daoFactory.getCuentaDAO();
	    List<Cuenta> listaCuentas = cuentaDAO.getCuentasByPropietario(usuarioAutenticado);

	    // 2.- Agregar los datos al �mbito de la solicitud
	    request.setAttribute("listaCuentas", listaCuentas);

	    // 3.- Redirigir al JSP de transferencia
	    request.getRequestDispatcher("jsp/ingreso.jsp").forward(request, response);
	}
	
	private void registrarEgresoPantalla(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
	    Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuarioLogeado");
	    
		  // 1.- Obtener los datos necesarios del modelo (por ejemplo, la lista de cuentas)
	    DAOFactory daoFactory = modelo.dao.DAOFactory.getFactory();
	    CuentaDAO cuentaDAO = daoFactory.getCuentaDAO();
	    List<Cuenta> listaCuentas = cuentaDAO.getCuentasByPropietario(usuarioAutenticado) ; 

	    // 2.- Agregar los datos al �mbito de la solicitud
	    request.setAttribute("listaCuentas", listaCuentas);

	    // 3.- Redirigir al JSP de transferencia
	    request.getRequestDispatcher("jsp/egreso.jsp").forward(request, response);
	}

	private void crearCuenta(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1. Obtener datos que me env�an en la solicitud
	    String nombreCuenta = request.getParameter("nombreCuenta");
	    float total = Float.parseFloat(request.getParameter("total"));
	    TipoCuenta tipoCuenta = TipoCuenta.valueOf(request.getParameter("tipoCuenta")); 

	    // 2. Obtener el ID del propietario desde la sesi�n
	    HttpSession session = request.getSession();
	    Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuarioLogeado"); 

	    // 3. Crear un objeto Cuenta con los datos obtenidos y el propietario obtenido de la sesion
	    Cuenta nuevaCuenta = new Cuenta();
	    nuevaCuenta.setNombreCuenta(nombreCuenta);
	    nuevaCuenta.setPropietario(usuarioAutenticado);
	    nuevaCuenta.setTotal(total);
	    nuevaCuenta.setTipoCuenta(tipoCuenta); // Establecer el tipo de cuenta

	    // 4. Obtener el CuentaDAO del DAO Factory
	    CuentaDAO cuentaDAO = DAOFactory.getFactory().getCuentaDAO();

	    // 5. Insertar la cuenta en la base de datos utilizando el m�todo createAndPersist del JPAGenericDAO
	    cuentaDAO.create(nuevaCuenta);

	    // 6. Redirigir a la vista deseada
	    response.sendRedirect("VisualizarDashboardController?ruta=visualizar");
	}


	private void transferir(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		  // 1.- Obtener datos que me env�an en la solicitud
	    String fechaStr = request.getParameter("fecha"); 
	    int idCuentaOrigen = Integer.parseInt(request.getParameter("cuentaOrigen"));
	    int idCuentaDestino = Integer.parseInt(request.getParameter("cuentaDestino"));
	    float monto = Float.parseFloat(request.getParameter("monto"));
	    String descripcion = request.getParameter("descripcion");

	    // Convertir la cadena de fecha a un objeto Date
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date fecha = null;
	    try {
	        fecha = new Date(dateFormat.parse(fechaStr).getTime());
	    } catch (ParseException e) {
	     
	    }

	    
	    // 2.- Llamo al modelo para obtener datos
	    DAOFactory daoFactory = modelo.dao.DAOFactory.getFactory();
	    CuentaDAO cuentaDAO = daoFactory.getCuentaDAO();
	    TransferenciaDAO transferenciaDAO = daoFactory.getTransferenciaDAO();

	    Cuenta cuentaOrigen = cuentaDAO.getById(idCuentaOrigen);
	    Cuenta cuentaDestino = cuentaDAO.getById(idCuentaDestino);

	    // 3.- Realizar la transferencia
	    Transferencia transferencia = transferenciaDAO.transferir(fecha, cuentaOrigen, cuentaDestino, monto, descripcion);

	    if (transferencia != null) {
	        System.out.println("Transferencia exitosa");
	    } else {
	        System.out.println("Error en la transferencia");
	    }

	    response.sendRedirect("VisualizarDashboardController?ruta=visualizar");
	}

	private void registrarIngreso(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		 // 1.- Obtener datos que me env�an en la solicitud
	    String fechaStr = request.getParameter("fecha");
	    int idCuentaDestino = Integer.parseInt(request.getParameter("cuentaDestino"));
	    float monto = Float.parseFloat(request.getParameter("monto"));
	    String descripcion = request.getParameter("descripcion");
	    
		 // Convertir la cadena de fecha a un objeto Date
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    Date fecha = null;
		    try {
		        fecha = new Date(dateFormat.parse(fechaStr).getTime());
		    } catch (ParseException e) {
		        // Manejar la excepci�n apropiadamente
		    }
	    
		// 2.- Llamo al modelo para obtener datos
		    
		    DAOFactory daoFactory = modelo.dao.DAOFactory.getFactory();
		    CuentaDAO cuentaDAO = daoFactory.getCuentaDAO();
		    IngresoDAO ingresoDAO = daoFactory.getIngresoDAO();

		    Cuenta cuentaDestino = cuentaDAO.getById(idCuentaDestino);
		    
		 // 3.- Realizar el ingreso
		    
		    Ingreso ingreso = ingresoDAO.ingresar(fecha, cuentaDestino, monto, descripcion);

		    if (ingreso != null) {
		        System.out.println("Transferencia exitosa");
		    } else {
		        System.out.println("Error en la transferencia");
		    }

		    // Redirigir 
		    response.sendRedirect("VisualizarDashboardController?ruta=visualizar");
	}

	private void registrarEgreso(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		 // 1.- Obtener datos que me env�an en la solicitud
	    String fechaStr = request.getParameter("fecha"); 
	    int idCuentaDestino = Integer.parseInt(request.getParameter("cuentaDestino"));
	    float monto = Float.parseFloat(request.getParameter("monto"));
	    String descripcion = request.getParameter("descripcion");
	    
	    // Convertir la cadena de fecha a un objeto Date
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date fecha = null;
	    try {
	        fecha = new Date(dateFormat.parse(fechaStr).getTime());
	    } catch (ParseException e) {
	        // Manejar la excepci�n apropiadamente
	    }

		// 2.- Llamo al modelo para obtener datos
	    
	    DAOFactory daoFactory = modelo.dao.DAOFactory.getFactory();
	    CuentaDAO cuentaDAO = daoFactory.getCuentaDAO();
	    EgresoDAO egresoDAO = daoFactory.getEgresoDAO();

	    Cuenta cuentaDestino = cuentaDAO.getById(idCuentaDestino);
	    
	    // 3.- Realizar el ingreso
	    
	    Egreso egreso = egresoDAO.egresar(fecha, cuentaDestino, monto, descripcion);
	    
	    if (egreso != null) {
	        System.out.println("Transferencia exitosa");
	    } else {
	        System.out.println("Error en la transferencia");
	    }

		// 3.- Llamo a la vista
		response.sendRedirect("VisualizarDashboardController?ruta=visualizar");
	}
	
	private void ajustarSaldo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int cuentaId = Integer.parseInt(request.getParameter("cuentaId"));
	    float nuevoSaldo = Float.parseFloat(request.getParameter("monto"));

	    CuentaDAO cuentaDAO = new JPACuentaDAO();
	    Cuenta cuenta = cuentaDAO.getById(cuentaId);

	    if (cuenta != null) {
	        cuentaDAO.ajustarSaldo(cuenta, nuevoSaldo, new Date());

	        response.sendRedirect("VisualizarDashboardController?ruta=visualizar");
	    } else {
	        response.sendRedirect("jsp/error.jsp"); 
	    }
    }
	
	

		
}


