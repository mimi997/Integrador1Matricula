package pe.edu.utp.matricula.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pe.edu.utp.matricula.entity.RolUsuario;
import pe.edu.utp.matricula.entity.Usuario;
import pe.edu.utp.matricula.repository.UsuarioRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioDetailsService usuarioDetailsService;

    @Test
    void loadUserByUsername_UserExistsAndActive_ReturnsUserDetails() {
        Usuario user = new Usuario("test@utp.edu.pe", "hash", RolUsuario.ESTUDIANTE, true);
        when(usuarioRepository.findByEmailAndActivoTrue("test@utp.edu.pe")).thenReturn(Optional.of(user));

        UserDetails userDetails = usuarioDetailsService.loadUserByUsername("test@utp.edu.pe");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("test@utp.edu.pe");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ESTUDIANTE");
    }

    @Test
    void loadUserByUsername_UserDoesNotExistOrInactive_ThrowsException() {
        when(usuarioRepository.findByEmailAndActivoTrue("noexiste@utp.edu.pe")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            usuarioDetailsService.loadUserByUsername("noexiste@utp.edu.pe");
        });
    }
}
