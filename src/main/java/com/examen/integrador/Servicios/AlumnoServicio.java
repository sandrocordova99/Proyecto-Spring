package com.examen.integrador.Servicios;

import java.util.List;

import com.examen.integrador.DTO.AlumnoDTO.RequestAlumnoDTO;
import com.examen.integrador.DTO.AlumnoDTO.ResponseAlumnoDTO;
import com.examen.integrador.Entidades.Alumnos;

public interface AlumnoServicio {

    List<ResponseAlumnoDTO> ListAlumnos();

    Alumnos crearAlumno(RequestAlumnoDTO dto);

    

}
