package br.com.fiap.Banking.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.Banking.model.Contas;
import br.com.fiap.Banking.model.Pix;

@RestController
@RequestMapping("/transacoes/pix")
public class PIXController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ContasController contasController;

    public PIXController(ContasController contasController) {
        this.contasController = contasController;
    }

    @PostMapping
    public ResponseEntity<Contas> pix(@RequestBody Pix pixRequest) {
        log.info("Realizando Pix: Transferindo de conta número " + pixRequest.getNumeroOrigem() + " para conta número " + pixRequest.getNumeroDestino());

        if (pixRequest.getValor() <= 0) {
            return ResponseEntity.badRequest().body(null);
        }

        Contas contaOrigem = contasController.getContasByNumero(pixRequest.getNumeroOrigem());
        Contas contaDestino = contasController.getContasByNumero(pixRequest.getNumeroDestino());

        if (contaOrigem.getSaldo() < pixRequest.getValor()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo() - pixRequest.getValor());
        contaDestino.setSaldo(contaDestino.getSaldo() + pixRequest.getValor());
        return ResponseEntity.ok(contaDestino);
    }
}
