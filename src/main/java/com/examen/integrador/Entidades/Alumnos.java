package com.examen.integrador.Entidades;

 
 
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Alumnos {

    @Id
    private String id;

    @MapsId   
    @OneToOne(cascade = CascadeType.ALL)   
    @JoinColumn(name = "usuario_id")
    private Usuarios usuarios;

    @Column(name = "apoderado")
    private String nombreDeApoderado;

}
