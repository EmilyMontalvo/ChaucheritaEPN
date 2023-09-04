package modelo.rest;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import modelo.dao.DAOFactory;
import modelo.dao.MovimientoDAO;
import modelo.dao.TransferenciaDAO;
import modelo.dao.UsuarioDAO;
import modelo.entidades.Movimiento;
import modelo.entidades.Transferencia;
import modelo.entidades.Usuario;
import modelo.entidades.Cuenta;
import modelo.entidades.Egreso;
import modelo.entidades.Ingreso;
import modelo.jpa.JPAMovimientoDAO;
import modelo.jpa.JPATransferenciaDAO;
import modelo.jpa.JPAUsuarioDAO;

@Path("/movimientos")
public class MovimientoRecurso {
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Movimiento> getMovimientos(@PathParam("id") int usuarioId){
		MovimientoDAO movimientoModelo = new JPAMovimientoDAO();
		UsuarioDAO usuarioModelo = new JPAUsuarioDAO();
		Usuario usuario = usuarioModelo.getById(usuarioId);
		//return DAOFactory.getFactory().getMovimientoDAO().getMovimientosByUsuario(usuario);
		return movimientoModelo.getMovimientosByUsuario(usuario);
	}
	
	@POST
	@Path("/egreso")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.APPLICATION_JSON)
	public void realizarEgreso(Egreso egreso){
		Cuenta cuentaDestino = egreso.getCuentaDestino();
		cuentaDestino.setTotal(cuentaDestino.getTotal() - egreso.getMonto());
		DAOFactory.getFactory().getCuentaDAO().update(cuentaDestino);
		DAOFactory.getFactory().getEgresoDAO().create(egreso);
	}
	
	@POST
	@Path("/ingreso")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.APPLICATION_JSON)
	public void realizarIngreso(Ingreso ingreso){
		Cuenta cuentaDestino = ingreso.getCuentaDestino();
		cuentaDestino.setTotal(cuentaDestino.getTotal() + ingreso.getMonto());
		DAOFactory.getFactory().getCuentaDAO().update(cuentaDestino);
		DAOFactory.getFactory().getIngresoDAO().create(ingreso);
	}
	
	
	@POST
	@Path("/transferencia")
	@Consumes(MediaType.APPLICATION_JSON)
	public void realizarTransferencia(Transferencia transferencia){
		Cuenta cuentaOrigen = transferencia.getCuentaOrigen();
		Cuenta cuentaDestino = transferencia.getCuentaDestino();
		cuentaOrigen.setTotal(cuentaOrigen.getTotal() - transferencia.getMonto());
        cuentaDestino.setTotal(cuentaDestino.getTotal() + transferencia.getMonto());
        DAOFactory.getFactory().getCuentaDAO().calcularYActualizarMontoMovido(cuentaDestino);
        DAOFactory.getFactory().getCuentaDAO().calcularYActualizarMontoMovido(cuentaOrigen);
        DAOFactory.getFactory().getCuentaDAO().update(cuentaOrigen);
        DAOFactory.getFactory().getCuentaDAO().update(cuentaDestino);
        DAOFactory.getFactory().getTransferenciaDAO().create(transferencia);
	}
	
}
