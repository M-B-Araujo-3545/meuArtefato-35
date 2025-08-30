package com.aluno.meuArtefato.api;

import com.aluno.meuArtefato.model.Tarefa;
import com.aluno.meuArtefato.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaRestController {

    @Autowired
    private TarefaService service;

    @GetMapping
    public List<Tarefa> listarTodas() {
        return service.listarTodas();
    }


    @PostMapping
    public Tarefa salvar(@RequestBody Tarefa tarefa) {
        return service.salvar(tarefa);
    }

    @PutMapping("/{id}")
    public Tarefa atualizar(@PathVariable Long id, @RequestBody Tarefa tarefaAtualizada) {
        Optional<Tarefa> tarefaExistente = service.buscarPorId(id);
        if (tarefaExistente.isPresent()) {
            Tarefa tarefa = tarefaExistente.get();
            tarefa.setNome(tarefaAtualizada.getNome());
            tarefa.setDataEntrega(tarefaAtualizada.getDataEntrega());
            tarefa.setResponsavel(tarefaAtualizada.getResponsavel());
            return service.salvar(tarefa);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam(required = false) Long id,
                                    @RequestParam(required = false) String nome) {
        if (id != null) {
            Optional<Tarefa> tarefa = service.buscarPorId(id);
            return tarefa.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else if (nome != null && !nome.isBlank()) {
            List<Tarefa> tarefas = service.pesquisarPorNome(nome);
            return ResponseEntity.ok(tarefas);
        } else {
            return ResponseEntity.badRequest().body("Informe o ID ou o nome para buscar.");
        }
    }

    // Este método deve estar fora do método acima
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}