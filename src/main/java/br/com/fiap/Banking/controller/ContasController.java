package br.com.fiap.Banking.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.Banking.model.Contas;
import br.com.fiap.Banking.model.Deposito;
import br.com.fiap.Banking.model.Pix;
import br.com.fiap.Banking.model.Saque;
import jakarta.validation.Valid;

@RestController
public class ContasController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private List<Contas> contas = new ArrayList<>();

    // GET da String com o Nome do Projeto e participantes.
    @GetMapping(path = "/")
    public String infoParticipantes() {
        return "Banking, Seu gestor de banco digital! \nParticipantes: \nCaetano Matos Penafiel \nVictor Egidio Lira";
    }

    // GETALL
    @GetMapping("/contas")
    public List<Contas> index() {
        log.info("Buscando todas as contas.");
        return contas;
    }

    // POST
    @PostMapping(path = "/contas")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Contas> create(@Valid @RequestBody Contas conta) {
        log.info("Criando uma nova conta: " + conta.getNumero());
        contas.add(conta);
        return ResponseEntity.status(201).body(conta);
    }

    // GETBYNUMERO
    @GetMapping(path = "/contas/numero/{numero}")
    public ResponseEntity<Contas> getContaByNumero(@PathVariable Long numero) {
        log.info("Buscando conta por número: " + numero);
        return ResponseEntity.ok(getContasByNumero(numero));
    }

    // GETBYCPF
    @GetMapping(path = "/contas/CPF/{CPF}")
    public ResponseEntity<Contas> getContaByCPF(@PathVariable String CPF) {
        log.info("Buscando conta por CPF: " + CPF);
        return ResponseEntity.ok(getContasByCPF(CPF));
    }

    // DELETE
    @DeleteMapping("/contas/encerrar/{numero}")
    public ResponseEntity<Object> encerrarConta(@PathVariable Long numero) {
        log.info("Encerrando conta por número: " + numero);
        Contas conta = getContasByNumero(numero);
        conta.setAtiva(false);
        return ResponseEntity.ok(conta);
    }

    // PUT
    @PutMapping("/contas/{numero}")
    public ResponseEntity<Contas> update(@Valid @PathVariable Long numero, @RequestBody Contas conta) {
        log.info("Atualizando conta por número: " + numero);
        var contaAtt = getContasByNumero(numero);
        contas.remove(contaAtt);
        conta.setNumero(numero);
        contas.add(conta);
        return ResponseEntity.ok(conta);
    }

    // Endpoint para realizar o saque
    @PostMapping("/contas/saque")
    public ResponseEntity<Contas> saque(@RequestBody Saque saqueRequest) {
        log.info("Realizando saque para a conta número: " + saqueRequest.getNumero());

        if (saqueRequest.getValor() <= 0) {
            return ResponseEntity.badRequest().body(null);
        }

        Contas conta = getContasByNumero(saqueRequest.getNumero());

        if (conta.getSaldo() < saqueRequest.getValor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        conta.setSaldo(conta.getSaldo() - saqueRequest.getValor());
        return ResponseEntity.ok(conta);
    }

    private Contas getContasByNumero(Long numero) {
        return contas.stream()
                .filter(c -> c.getNumero().equals(numero))
                .findFirst()
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    // Endpoint para realizar o PIX
    @PostMapping("/contas/pix")
    public ResponseEntity<Contas> pix(@RequestBody Pix pixRequest) {
        log.info("Realizando Pix: Transferindo de conta número " + pixRequest.getNumeroOrigem() + " para conta número "
                + pixRequest.getNumeroDestino());

        if (pixRequest.getValor() <= 0) {
            return ResponseEntity.badRequest().body(null);
        }

        Contas contaOrigem = getContasByNumero(pixRequest.getNumeroOrigem());
        Contas contaDestino = getContasByNumero(pixRequest.getNumeroDestino());

        if (contaOrigem.getSaldo() < pixRequest.getValor()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo() - pixRequest.getValor());
        contaDestino.setSaldo(contaDestino.getSaldo() + pixRequest.getValor());
        return ResponseEntity.ok(contaDestino);
    }

    // Endpoint para realizar o depósito
    @PostMapping("/contas/deposito")
    public ResponseEntity<Contas> deposito(@RequestBody Deposito depositoRequest) {
        log.info("Realizando depósito para a conta número: " + depositoRequest.getNumero());

        if (depositoRequest.getValor() <= 0) {
            return ResponseEntity.badRequest().body(null);
        }

        Contas conta = getContasByNumero(depositoRequest.getNumero());
        conta.setSaldo(conta.getSaldo() + depositoRequest.getValor());
        return ResponseEntity.ok(conta);
    }

    private Contas getContasByCPF(String CPF) {
        return contas.stream()

                .filter(c -> c.getCpfDoTitular().equals(CPF))
                .findFirst()
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }
}
