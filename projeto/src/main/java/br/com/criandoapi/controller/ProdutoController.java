package br.com.criandoapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.criandoapi.projeto.model.Produtos;
import br.com.criandoapi.projeto.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "http://localhost:3000")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public ResponseEntity<List<Produtos>> listarProdutos() {
        List<Produtos> produtos = produtoRepository.findAll();
        return ResponseEntity.ok(produtos);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> adicionarProduto(@RequestParam("nome") String nome,
                                              @RequestParam("preco") BigDecimal preco,
                                              @RequestParam("descricao") String descricao,
                                              @RequestParam(value = "imagem", required = false) MultipartFile imagem) {
        try {
            Produtos produto = new Produtos();
            produto.setNome(nome);
            produto.setPreco(preco);
            produto.setDescricao(descricao);

            if (imagem != null && !imagem.isEmpty()) {
                produto.setImagemPath(imagem.getBytes());
            }

            Produtos produtoSalvo = produtoRepository.save(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a imagem: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterProduto(@PathVariable Long id) {
        Optional<Produtos> produtoOptional = produtoRepository.findById(id);
        if (produtoOptional.isPresent()) {
            Produtos produto = produtoOptional.get();
            return ResponseEntity.ok(produto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletarProduto(@PathVariable Long id) {
        Optional<Produtos> produtoOptional = produtoRepository.findById(id);
        if (produtoOptional.isPresent()) {
            produtoRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
    }
}
