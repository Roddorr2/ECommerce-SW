package com.ecommerce.app.shared.domain.model;

public enum TipoSolicitud {
    DUDA,
    PEDIDO,
    CUENTA,
    GARANTIA;

    public static TipoSolicitud fromCodigo(String codigo) {
        if (codigo == null) return DUDA;
        switch (codigo.toLowerCase().trim()) {
            case "pedido":
            case "inconveniente con pedido":
                return PEDIDO;
            case "cuenta":
            case "problema con cuenta / software":
                return CUENTA;
            case "garantia":
            case "garantía / falla de componente":
                return GARANTIA;
            case "duda":
            case "duda general":
            default:
                return DUDA;
        }
    }
}
