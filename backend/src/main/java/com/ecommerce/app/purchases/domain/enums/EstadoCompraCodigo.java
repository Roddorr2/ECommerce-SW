package com.ecommerce.app.purchases.domain.enums;

public enum EstadoCompraCodigo {
	PENDIENTE {
		@Override public boolean isFinal() { return false; }
		@Override public boolean isRecibida() { return false; }
		@Override public boolean isCancelada() { return false; }
	},
	RECIBIDA {
		@Override public boolean isFinal() { return true; }
		@Override public boolean isRecibida() { return true; }
		@Override public boolean isCancelada() { return false; }
	},
	CANCELADA {
		@Override public boolean isFinal() { return true; }
		@Override public boolean isRecibida() { return false; }
		@Override public boolean isCancelada() { return true; }
	};

	public abstract boolean isFinal();
	public abstract boolean isRecibida();
	public abstract boolean isCancelada();

	public boolean isPendiente() { return this == PENDIENTE; }
	public boolean permiteModificacion() { return this == PENDIENTE; }
	public boolean puedeRecibirse() { return this == PENDIENTE; }
	public boolean puedeCancelarse() { return this == PENDIENTE; }
}
