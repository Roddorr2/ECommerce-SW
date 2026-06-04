package com.ecommerce.app.shared.domain.model;

import java.util.List;

public class Cargo {

	private Integer id;
	private String nombre;
	private List<Empleado> empleados;

	public Cargo() {}

	public Cargo(Integer id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Empleado> getEmpleados() {
		return empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}	
}
