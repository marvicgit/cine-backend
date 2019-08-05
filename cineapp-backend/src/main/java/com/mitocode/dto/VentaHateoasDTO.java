package com.mitocode.dto;

import org.springframework.hateoas.ResourceSupport;

import com.mitocode.model.Cliente;
import com.mitocode.model.Pelicula;

public class VentaHateoasDTO extends ResourceSupport {

	private int idVenta;
	private Pelicula pelicula;
	private Cliente cliente;

	public int getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(int idVenta) {
		this.idVenta = idVenta;
	}

	public Pelicula getPelicula() {
		return pelicula;
	}

	public void setPelicula(Pelicula pelicula) {
		this.pelicula = pelicula;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
