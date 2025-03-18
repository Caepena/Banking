package br.com.fiap.Banking.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.Banking.model.Contas;
import br.com.fiap.Banking.model.Saque;

@RestController
public class SaqueController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ContasController contasController;

    public SaqueController(ContasController contasController) {
        this.contasController = contasController;
    }

    @PostMapping("/transacoes/saque")
    public ResponseEntity<Contas> saque(@RequestBody Saque saqueRequest) {
        log.info("Realizando saque para a conta número: " + saqueRequest.getNumero());

        if (saqueRequest.getValor() <= 0) {
            return ResponseEntity.badRequest().body(null);
        }

        Contas conta = contasController.getContasByNumero(saqueRequest.getNumero());

        if (conta.getSaldo() < saqueRequest.getValor()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        conta.setSaldo(conta.getSaldo() - saqueRequest.getValor());
        return ResponseEntity.ok(conta);
    }
}
