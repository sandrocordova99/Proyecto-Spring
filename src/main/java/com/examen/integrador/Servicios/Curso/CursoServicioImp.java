package com.examen.integrador.Servicios.Curso;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.examen.integrador.DTO.CursoDTO.CursoEditDTO;
import com.examen.integrador.DTO.CursoDTO.CursoRequestDTO;
import com.examen.integrador.DTO.CursoDTO.CursoResponseDTO;
import com.examen.integrador.Entidades.Alumnos;
import com.examen.integrador.Entidades.Categorias;
import com.examen.integrador.Entidades.Cursos;
import com.examen.integrador.Entidades.Grados;
import com.examen.integrador.Entidades.Profesor;
import com.examen.integrador.Mapper.CursoMapper;
import com.examen.integrador.Repositorio.CategoriasRepositorio;
import com.examen.integrador.Repositorio.CursosRepositorio;
import com.examen.integrador.Repositorio.GradoRepositorio;
import com.examen.integrador.Validacion.AutogenerarID;

import jakarta.transaction.Transactional;

@Service
public class CursoServicioImp implements CursoServicio {

    private final CursosRepositorio cursosRepositorio;
    private final AutogenerarID autogenerarID;
    private final GradoRepositorio gradoRepositorio;

    public CursoServicioImp(CursosRepositorio cursosRepositorio, AutogenerarID autogenerarID,
            GradoRepositorio gradoRepositorio) {
        this.cursosRepositorio = cursosRepositorio;
        this.autogenerarID = autogenerarID;
        this.gradoRepositorio = gradoRepositorio;
    }

    @Override
    @Transactional
    public CursoResponseDTO crearCurso(CursoRequestDTO dto) {
        try {

            Cursos cursos = CursoMapper.instancia.toCursoRequest(dto);

            cursos.setId(autogenerarID.generarId("CURSOS"));

            Cursos CursoSave = cursosRepositorio.save(cursos);

            // despues creo las categorias

            List<Grados> listaGrados = gradoRepositorio.findAll();

            return CursoMapper.instancia.toCursoReponse(CursoSave);

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el curso", e);
        }

    }

    @Override
    public List<CursoResponseDTO> listarCursosDTO() {

        try {
            List<Cursos> cursosList = cursosRepositorio.findAll();

            List<CursoResponseDTO> cursosListDTO = CursoMapper.instancia.listarCursosDTO(cursosList);

            return cursosListDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error al listar cursos", e);
        }

    }

    @Override
    public String eliminarCurso(String id) {

        try {

            // limpio las entidades vinculadas --> ESTO SOLO APLICA CUANDO NO PUEDO USAR EL
            // CASCADE.TIPY ALL
            Cursos cursoDelete = cursosRepositorio.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("No se encontro el curso con ese ID"));

            for (Alumnos alumno : cursoDelete.getAlumnos()) {
                alumno.getCursos().remove(cursoDelete); // Importante: remover el curso del alumno
            }

            for (Profesor profesor : cursoDelete.getProfesores()) {
                profesor.setCurso(null);
            }

            // limpio mi entidad
            cursoDelete.getAlumnos().clear();

            cursoDelete.getProfesores().clear();

            cursosRepositorio.save(cursoDelete);

            cursosRepositorio.delete(cursoDelete);

            return cursoDelete.getNombre();

        } catch (Exception e) {
            throw new RuntimeException("Error al borrar curso: ", e);
        }

    }

    @Override
    @Transactional
    public CursoResponseDTO editarCurso(CursoEditDTO dto) {

        try {

            Cursos curso = cursosRepositorio.findById(dto.getId()).orElseThrow(
                    () -> new UsernameNotFoundException("Curso no encontrado con ese id"));

            curso.setNombre(dto.getNombre());

            // Grados grados = gradoRepositorio.findById(dto.getGrado()).orElseThrow(
            // () -> new UsernameNotFoundException("Grado no encontrado con ese id"));

            // curso.setGrado(grados);

            for (Categorias categoria : curso.getCategorias()) {
                String nuevoNombre = dto.getNombre() + " - " + categoria.getGrados().getNombre();
                categoria.setNombre(nuevoNombre);
            }
            return CursoMapper.instancia.toCursoReponse(cursosRepositorio.save(curso));

        } catch (Exception e) {
            throw new RuntimeException("Error al borrar curso: ", e);
        }

    }

}
