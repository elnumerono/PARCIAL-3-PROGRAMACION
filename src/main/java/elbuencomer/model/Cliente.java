package elbuencomer.model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String documento;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String direccion;
    private List<Pedido> listClientePedidos;

    public Cliente(String documento, String nombreCompleto, String telefono,
                   String correo, String direccion) {
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.listClientePedidos = new ArrayList<>();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Pedido> getListClientePedidos() {
        return listClientePedidos;
    }

    public void setListClientePedidos(List<Pedido> listClientePedidos) {
        this.listClientePedidos = listClientePedidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "documento='" + documento + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}
