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

    public Contas getContasByNumero(Long numero) {
        return contas.stream()
                .filter(c -> c.getNumero().equals(numero))
                .findFirst()
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    private Contas getContasByCPF(String CPF) {
        return contas.stream()

                .filter(c -> c.getCpfDoTitular().equals(CPF))
                .findFirst()
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }
}
