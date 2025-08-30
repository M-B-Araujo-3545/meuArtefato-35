package com.aluno.meuArtefato.repository;

import com.aluno.meuArtefato.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // Busca por nome ignorando maiúsculas/minúsculas
    List<Tarefa> findByNomeContainingIgnoreCase(String nome);

    // deleteById já existe no JpaRepository, não precisa declarar novamente
    // deleteAllById espera uma Collection<Long>, não um único Long
}