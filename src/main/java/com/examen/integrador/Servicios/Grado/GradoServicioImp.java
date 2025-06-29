package com.examen.integrador.Servicios.Grado;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.examen.integrador.DTO.CursoDTO.AsignarGradoDTO;
import com.examen.integrador.DTO.GradoDTO.AsignarAlumnosDTO;
import com.examen.integrador.DTO.GradoDTO.GradoRequestDTO;
import com.examen.integrador.DTO.GradoDTO.GradoResponseDTO;
import com.examen.integrador.Entidades.Alumnos;
import com.examen.integrador.Entidades.Categorias;
import com.examen.integrador.Entidades.Cursos;
import com.examen.integrador.Entidades.Grados;
import com.examen.integrador.Mapper.GradoMapper;
import com.examen.integrador.Repositorio.AlumnosRepositorio;
import com.examen.integrador.Repositorio.CategoriasRepositorio;
import com.examen.integrador.Repositorio.CursosRepositorio;
import com.examen.integrador.Repositorio.GradoRepositorio;
import com.examen.integrador.Validacion.AutogenerarID;

import jakarta.transaction.Transactional;

@Service
public class GradoServicioImp implements GradoServicio {

    private final GradoRepositorio gradoRepositorio;
    private final AutogenerarID autogenerarID;
    private final CursosRepositorio cursosRepositorio;
    private final AlumnosRepositorio alumnosRepositorio;
    private final CategoriasRepositorio categoriasRepositorio;

    @Autowired
    public GradoServicioImp(GradoRepositorio gradoRepositorio, AutogenerarID autogenerarID,
            CursosRepositorio cursosRepositorio, AlumnosRepositorio alumnosRepositorio , CategoriasRepositorio categoriasRepositorio) {
        this.gradoRepositorio = gradoRepositorio;
        this.autogenerarID = autogenerarID;
        this.cursosRepositorio = cursosRepositorio;
        this.alumnosRepositorio = alumnosRepositorio;
        this.categoriasRepositorio = categoriasRepositorio;
    }

    // para este punto ya deberia esta validado los campos
    @Override
    @Transactional
    public GradoResponseDTO crearGrado(GradoRequestDTO dto) {

        try {
            Grados grados = GradoMapper.instancia.toGradoRequest(dto);

            grados.setId(autogenerarID.generarId("GRADOS"));

            return GradoMapper.instancia.toGradoReponse(gradoRepositorio.save(grados));

        } catch (Exception e) {

            throw new RuntimeException("Error al guardar el grado", e);

        }

    }

    @Override
    @Transactional
    public GradoResponseDTO asignarCursos(AsignarGradoDTO dto) {

        try {

            Optional<Grados> gradoOptional = gradoRepositorio.findById(dto.getGradoId());

            List<Cursos> cursosList = cursosRepositorio.findAllById(dto.getCursosId());

            Set<Cursos> listaCursos = new HashSet<>(cursosList);

            if (gradoOptional.isEmpty() || listaCursos.isEmpty()) {
                throw new UsernameNotFoundException("No se encontró grado o cursos con ese ID");
            }

            Grados grado = gradoOptional.get();

            Set<Categorias> categoriasList = new HashSet();

            for (Cursos curso : cursosList) {

                Categorias categoria = new Categorias();

                categoria.setId(autogenerarID.generarId("CATEGORIAS"));

                categoria.setNombre(curso.getNombre()+  " "+ grado.getNombre());

                categoria.setCursos(curso);

                categoria.setGrados(grado);

                categoriasRepositorio.save(categoria);

                categoriasList.add(categoria);
            }

            grado.getCategorias().addAll(categoriasList);

            gradoRepositorio.save(grado);

            cursosRepositorio.saveAll(cursosList);

            return GradoMapper.instancia.toGradoReponse(grado);

        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }

    }

    @Override
    public List<GradoResponseDTO> listGradoResponseDTO() {

        try {
            List<Grados> listaGrados = gradoRepositorio.findAll();

            return GradoMapper.instancia.listGradoResponseDTO(listaGrados);
        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }

    }

    @Override
    public GradoResponseDTO asignarAlumnos(AsignarAlumnosDTO dto) {

        Optional<Grados> gradosOptional = gradoRepositorio.findById(dto.getGradoId());

        List<Alumnos> alumnosList = alumnosRepositorio.findAllById(dto.getAlumnos());

        Set<Alumnos> alumnosSet = new HashSet<>(alumnosList);

        if (gradosOptional.isEmpty() || alumnosList.isEmpty()) {
            throw new UsernameNotFoundException("No se encontró grado o cursos con ese ID");
        }

        Grados grado = gradosOptional.get();

        for (Alumnos alumno : alumnosSet) {
            alumno.setGrado(grado);
            // alumno.setCursos(new ArrayList<>(grado.getCursos()));

            System.out.println("grados alumno : " + alumno.getGrado());
            System.out.println("Cursos alumno : " + alumno.getCursos());
        }

        alumnosRepositorio.saveAll(alumnosSet);

        grado.setAlumnos(new ArrayList<>(alumnosSet));
        gradoRepositorio.save(grado);

        return GradoMapper.instancia.toGradoReponse(grado);

    }

}
