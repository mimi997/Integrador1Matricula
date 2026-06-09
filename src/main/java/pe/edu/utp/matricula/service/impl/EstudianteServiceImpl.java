package pe.edu.utp.matricula.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.matricula.dto.EstudianteDTO;
import pe.edu.utp.matricula.entity.Estudiante;
import pe.edu.utp.matricula.entity.RolUsuario;
import pe.edu.utp.matricula.entity.Usuario;
import pe.edu.utp.matricula.repository.EstudianteRepository;
import pe.edu.utp.matricula.repository.UsuarioRepository;
import pe.edu.utp.matricula.service.EstudianteService;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public EstudianteServiceImpl(EstudianteRepository estudianteRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.estudianteRepository = estudianteRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Estudiante> findAll() {
        return estudianteRepository.findAll();
    }

    @Override
    public Optional<Estudiante> findById(Long id) {
        return estudianteRepository.findById(id);
    }

    @Override
    @Transactional
    public Estudiante guardarEstudiante(EstudianteDTO dto) {
        Usuario usuario;
        Estudiante estudiante;

        // Validar y normalizar entradas
        pe.edu.utp.matricula.util.Validaciones.requireValidEmail(dto.getEmail());
        String carreraNormalizada = pe.edu.utp.matricula.util.Validaciones.normalizar(dto.getCarrera());
        String codigoNormalizado = pe.edu.utp.matricula.util.Validaciones.normalizar(dto.getCodigoEstudiante());

        if (dto.getId() != null) {
            // Edit existing
            estudiante = estudianteRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
            usuario = estudiante.getUsuario();
            usuario.setEmail(dto.getEmail());
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            }
        } else {
            // Create new
            usuario = new Usuario();
            usuario.setEmail(dto.getEmail());
            usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            usuario.setRol(RolUsuario.ESTUDIANTE);
            usuario.setActivo(true);
            
            estudiante = new Estudiante();
            estudiante.setUsuario(usuario);
        }

        usuarioRepository.save(usuario);

        estudiante.setCodigoEstudiante(codigoNormalizado);
        estudiante.setCarrera(carreraNormalizada);
        estudiante.setCiclo(dto.getCiclo());
        estudiante.setCreditos(0); // Defaults to 0 or could be preserved on update
        if (dto.getId() != null) {
            estudiante.setCreditos(estudianteRepository.findById(dto.getId()).get().getCreditos());
        }

        return estudianteRepository.save(estudiante);
    }

    @Override
    @Transactional
    public void eliminarEstudiante(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
        estudiante.getUsuario().setActivo(false);
        usuarioRepository.save(estudiante.getUsuario());
    }
}
