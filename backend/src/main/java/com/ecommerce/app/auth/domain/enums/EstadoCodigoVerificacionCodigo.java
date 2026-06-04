package com.ecommerce.app.auth.domain.enums;

public enum EstadoCodigoVerificacionCodigo {
	PENDIENTE {
        @Override public boolean isFinal()     { return false; }
        @Override public boolean isBloqueado() { return false; }
        @Override public boolean isUsado()     { return false; }
        @Override public boolean isExpirado()  { return false; }
    },
    USADO {
        @Override public boolean isFinal()     { return true; }
        @Override public boolean isBloqueado() { return false; }
        @Override public boolean isUsado()     { return true; }
        @Override public boolean isExpirado()  { return false; }
    },
    EXPIRADO {
        @Override public boolean isFinal()     { return true; }
        @Override public boolean isBloqueado() { return false; }
        @Override public boolean isUsado()     { return false; }
        @Override public boolean isExpirado()  { return true; }
    },
    BLOQUEADO {
        @Override public boolean isFinal()     { return true; }
        @Override public boolean isBloqueado() { return true; }
        @Override public boolean isUsado()     { return false; }
        @Override public boolean isExpirado()  { return false; }
    };

    public abstract boolean isFinal();
    public abstract boolean isBloqueado();
    public abstract boolean isUsado();
    public abstract boolean isExpirado();

    public boolean isPendiente() { return this == PENDIENTE; }
}
