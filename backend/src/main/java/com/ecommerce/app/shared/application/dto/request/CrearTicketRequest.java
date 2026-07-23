package com.ecommerce.app.shared.application.dto.request;

public class CrearTicketRequest {
    private String nombre;
    private String email;
    private String tipoCaso;
    private String asunto;
    private String mensaje;

    public CrearTicketRequest() {
    }

    public CrearTicketRequest(String nombre, String email, String tipoCaso, String asunto, String mensaje) {
        this.nombre = nombre;
        this.email = email;
        this.tipoCaso = tipoCaso;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoCaso() {
        return tipoCaso;
    }

    public void setTipoCaso(String tipoCaso) {
        this.tipoCaso = tipoCaso;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
