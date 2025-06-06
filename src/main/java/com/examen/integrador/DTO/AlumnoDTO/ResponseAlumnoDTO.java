package com.examen.integrador.DTO.AlumnoDTO;

import java.time.LocalDate;

import com.examen.integrador.Entidades.RolesEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseAlumnoDTO {

    // Usuarios
    // Usuarios

    private String nombre;

    private String apellido;

    private String username;

    private String email;

    private LocalDate nacimiento;

    private RolesEnum roles;

    private String nombreDeApoderado;

    private AlumnoGradoResponseDTO grado;

}