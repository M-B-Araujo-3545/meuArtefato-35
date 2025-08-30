package com.aluno.meuArtefato.controller;

import com.aluno.meuArtefato.model.Tarefa;
import com.aluno.meuArtefato.service.TarefaService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService service;

    @GetMapping
    public String listar(Model model) {
        // Correct: Adds a list of tasks for the table and an empty task for the form.
        model.addAttribute("tarefas", service.listarTodas());
        model.addAttribute("tarefa", new Tarefa());
        return "index";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Tarefa tarefa) {
        service.salvar(tarefa);
        // Correct: Redirects to the list view to show the updated data.
        return "redirect:/tarefas";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(@RequestParam String nome, Model model) {
        model.addAttribute("tarefas", service.pesquisarPorNome(nome));
        model.addAttribute("tarefa", new Tarefa());
        return "index";

    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        // Correct: Adds the task to be edited to fill the form.
        Tarefa tarefa = service.buscarPorId(id).orElse(new Tarefa());
        model.addAttribute("tarefa", tarefa);

        // Correct: Adds ALL tasks to populate the table on the same page.
        model.addAttribute("tarefas", service.listarTodas());

        return "index";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/tarefas";
    }
    @Configuration
    public class SpringDocConfig {
        @Bean
        public OpenAPI customOpenAPI() {
            return new OpenAPI()
                    .info(new Info().title("Minha API").version("v1"));
        }
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



}