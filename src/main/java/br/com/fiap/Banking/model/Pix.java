package br.com.fiap.Banking.model;

public class Pix {

    private Long numeroOrigem, numeroDestino;
    private Double valor;

    public Long getNumeroOrigem() {
        return numeroOrigem;
    }

    public void setNumeroOrigem(Long numeroOrigem) {
        this.numeroOrigem = numeroOrigem;
    }

    public Long getNumeroDestino() {
        return numeroDestino;
    }

    public void setNumeroDestino(Long numeroDestino) {
        this.numeroDestino = numeroDestino;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}