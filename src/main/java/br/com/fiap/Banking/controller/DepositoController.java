package br.com.fiap.Banking.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.Banking.model.Contas;
import br.com.fiap.Banking.model.Deposito;

@RestController
public class DepositoController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ContasController contasController;

    public DepositoController(ContasController contasController) {
        this.contasController = contasController;
    }

    @PostMapping("/transacoes/deposito")
    public ResponseEntity<Contas> deposito(@RequestBody Deposito depositoRequest) {
        log.info("Realizando depósito para a conta número: " + depositoRequest.getNumero());

        if (depositoRequest.getValor() <= 0) {
            log.info("Não é possivel fazer um depósito de um valor negativo ou igual a zero.");
            return ResponseEntity.badRequest().body(null);
        }

        Contas conta = contasController.getContasByNumero(depositoRequest.getNumero());
        conta.setSaldo(conta.getSaldo() + depositoRequest.getValor());
        return ResponseEntity.ok(conta);
    }
}
