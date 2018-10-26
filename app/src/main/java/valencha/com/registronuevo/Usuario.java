package valencha.com.registronuevo;

public class Usuario {

    private String uid;
    private String nombre;
    private String corre;
    private String pass;

    public Usuario(String nombre, String corre, String pass) {
        this.nombre = nombre;
        this.corre = corre;
        this.pass = pass;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorre() {
        return corre;
    }

    public void setCorre(String corre) {
        this.corre = corre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

