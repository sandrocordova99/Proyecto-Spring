package com.examen.integrador.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.examen.integrador.Entidades.Profesores;

@Repository
public interface ProfesoresRepositorio extends JpaRepository<Profesores, String> {

}
