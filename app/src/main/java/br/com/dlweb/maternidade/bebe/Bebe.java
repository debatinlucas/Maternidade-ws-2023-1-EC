package br.com.dlweb.maternidade.bebe;

public class Bebe implements java.io.Serializable {

    private int id, id_mae, crm_medico, altura;
    private String nome, data_nascimento;
    private float peso;

    public Bebe() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_mae() {
        return id_mae;
    }

    public void setId_mae(int id_mae) {
        this.id_mae = id_mae;
    }

    public int getCrm_medico() {
        return crm_medico;
    }

    public void setCrm_medico(int crm_medico) {
        this.crm_medico = crm_medico;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }
}
