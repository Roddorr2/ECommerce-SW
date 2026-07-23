package com.ecommerce.app.sales.domain.enums;

public enum EstadoOrdenCodigo {
    PENDIENTE {
    	@Override public boolean isFinal() { return false; }
    	@Override public boolean isCancelled() { return false; }
    	@Override public boolean isPagado() { return false; }
    },
    PAGADO {
    	@Override public boolean isFinal() { return false; }
    	@Override public boolean isCancelled() { return false; }
    	@Override public boolean isPagado() { return true; }
    },
    ENVIADO {
    	@Override public boolean isFinal() { return false; }
    	@Override public boolean isCancelled() { return false; }
    	@Override public boolean isPagado() { return false; }
    },
    ENTREGADO {
    	@Override public boolean isFinal() { return true; }
    	@Override public boolean isCancelled() { return false; }
    	@Override public boolean isPagado() { return false; }
    },
    CANCELADO {
    	@Override public boolean isFinal() { return true; }
    	@Override public boolean isCancelled() { return true; }
    	@Override public boolean isPagado() { return false; }
    };
	
	public abstract boolean isFinal();
	public abstract boolean isCancelled();
	public abstract boolean isPagado();
	
	public boolean permiteModificacion() { return !isFinal(); } 
}