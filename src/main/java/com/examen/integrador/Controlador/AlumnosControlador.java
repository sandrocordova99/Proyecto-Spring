package com.examen.integrador.Controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examen.integrador.DTO.AlumnoDTO.RequestAlumnoDTO;
import com.examen.integrador.DTO.AlumnoDTO.ResponseAlumnoDTO;
import com.examen.integrador.Entidades.Alumnos;
import com.examen.integrador.Entidades.Usuarios;
import com.examen.integrador.Mapper.AlumnoMapper;
import com.examen.integrador.Servicios.AlumnoServicioImp;
import com.examen.integrador.Servicios.UserSerivicio;
import com.examen.integrador.Validacion.UserValidacion;

@RestController
@RequestMapping("/alu")
public class AlumnosControlador {

    private final AlumnoServicioImp alumnoServicioImp;
    private final UserValidacion userValidacion;

    @Autowired
    public AlumnosControlador(AlumnoServicioImp alumnoServicioImp, UserValidacion userValidacion) {
        this.alumnoServicioImp = alumnoServicioImp;
        this.userValidacion = userValidacion;
    }

    @GetMapping("/listar")
    public ResponseEntity<Map<String, Object>> listarAlumnos() {

        Map<String, Object> respuesta = new HashMap();

        List<ResponseAlumnoDTO> ListAlumnosDTO = alumnoServicioImp.ListAlumnos();

        respuesta.put("Alumnos", ListAlumnosDTO);

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);

    }

    @PostMapping("/crear")
    public ResponseEntity<Map<String, Object>> crearAlumnos(@RequestBody RequestAlumnoDTO dto) {

        Map<String, Object> respuesta = new HashMap();

        Map<String, Object> respuestaValidacion = userValidacion.validarUsuarios(dto);

        if (respuestaValidacion.containsKey("Confirmación")) {

            Alumnos alu = alumnoServicioImp.crearAlumno(dto);

            respuesta.put("validacion", respuestaValidacion.get("Confirmación"));
            respuesta.put("ID", alu.getId());

        } else {

            respuesta.put("respuesta", respuestaValidacion.get("Errores"));

        }

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);

    }

}
