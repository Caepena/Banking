package br.com.fiap.Banking.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.Random;


public class Contas {

    private Long numero;

    @NotBlank(message = "Nome do titular é obrigatório.")
    private String nomeDoTitular;

    @NotBlank(message = "CPF do titular é obrigatório.")
    private String cpfDoTitular;

    @NotNull(message = "Agência é obrigatória.")
    private int agencia;

    @NotNull(message = "Data de abertura é obrigatória.")
    @PastOrPresent(message = "A data de abertura não pode ser no futuro.")
    private LocalDate dataDeAbertura;

    @NotNull
    @Min(value = 0, message = "O saldo não pode ser negativo.")
    private Double saldo;

    @NotNull(message = "Status da conta (ativa/inativa) é obrigatório.")
    private Boolean ativa;

    @NotBlank(message = "Tipo da conta é obrigatório.")
    @Pattern(regexp = "^(corrente|poupanca|salario)$", message = "Tipo inválido. Use 'corrente', 'poupanca' ou 'salario'.")
    private String tipo;

    public Contas(Long numero, String nomeDoTitular, String cpfDoTitular, int agencia, LocalDate dataDeAbertura,
                  Double saldo, Boolean ativa, String tipo) {
        this.numero = Math.abs(new Random().nextLong());
        this.nomeDoTitular = nomeDoTitular;
        this.cpfDoTitular = cpfDoTitular;
        this.agencia = agencia;
        this.dataDeAbertura = dataDeAbertura;
        this.saldo = saldo;
        this.ativa = ativa;
        this.tipo = tipo;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getNomeDoTitular() {
        return nomeDoTitular;
    }

    public void setNomeDoTitular(String nomeDoTitular) {
        this.nomeDoTitular = nomeDoTitular;
    }

    public String getCpfDoTitular() {
        return cpfDoTitular;
    }

    public void setCpfDoTitular(String cpfDoTitular) {
        this.cpfDoTitular = cpfDoTitular;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public LocalDate getDataDeAbertura() {
        return dataDeAbertura;
    }

    public void setDataDeAbertura(LocalDate dataDeAbertura) {
        this.dataDeAbertura = dataDeAbertura;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
