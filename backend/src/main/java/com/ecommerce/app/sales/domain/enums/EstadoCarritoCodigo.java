package com.ecommerce.app.sales.domain.enums;

public enum EstadoCarritoCodigo {
    ACTIVO {
    	@Override public boolean permiteModificacion() { return true; }
    	@Override public boolean permiteConversion() { return true; }
    	@Override public boolean esFinal() { return false; }
    },
    CONVERTIDO {
    	@Override public boolean permiteModificacion() { return false; }
        @Override public boolean permiteConversion() { return false; }
        @Override public boolean esFinal() { return true; }
    },
    EXPIRADO {
    	@Override public boolean permiteModificacion() { return false; }
    	@Override public boolean permiteConversion() { return false; }
    	@Override public boolean esFinal() { return true; }
    },
    ABANDONADO {
    	@Override public boolean permiteModificacion() { return false; }
    	@Override public boolean permiteConversion() { return false; }
    	@Override public boolean esFinal() { return true; }
    };
	
	public abstract boolean permiteModificacion();
    public abstract boolean permiteConversion();
    public abstract boolean esFinal();
}
