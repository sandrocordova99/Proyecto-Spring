package com.examen.integrador.Servicios.Grado;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.examen.integrador.DTO.GradoDTO.AsignarGradoDTO;
import com.examen.integrador.DTO.GradoDTO.GradoRequestDTO;
import com.examen.integrador.DTO.GradoDTO.GradoResponseDTO;
import com.examen.integrador.Entidades.Cursos;
import com.examen.integrador.Entidades.Grados;
import com.examen.integrador.Mapper.GradoMapper;
import com.examen.integrador.Repositorio.CursosRepositorio;
import com.examen.integrador.Repositorio.GradoRepositorio;
import com.examen.integrador.Validacion.AutogenerarID;

@Service
public class GradoServicioImp implements GradoServicio {

    private final GradoRepositorio gradoRepositorio;
    private final AutogenerarID autogenerarID;
    private final CursosRepositorio cursosRepositorio;

    @Autowired
    public GradoServicioImp(GradoRepositorio gradoRepositorio, AutogenerarID autogenerarID,
            CursosRepositorio cursosRepositorio) {
        this.gradoRepositorio = gradoRepositorio;
        this.autogenerarID = autogenerarID;
        this.cursosRepositorio = cursosRepositorio;
    }

    // para este punto ya deberia esta validado los campos
    @Override
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
    public GradoResponseDTO asignarCursos(AsignarGradoDTO dto) {

        try {

            Optional<Grados> gradosOptional = gradoRepositorio.findById(dto.getGradoId());

            List<Cursos> cursosList = cursosRepositorio.findAllById(dto.getCursosId());

            Set<Cursos> listaCursos = new HashSet<>(cursosList);

            if (gradosOptional.isEmpty() || listaCursos.isEmpty()) {
                throw new UsernameNotFoundException("No se encontró grado o cursos con ese ID");
            }

            Grados grados = gradosOptional.get();

            grados.setCursos(listaCursos);

            gradoRepositorio.save(grados);

            return GradoMapper.instancia.toGradoReponse(grados);

        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }

    }

}
