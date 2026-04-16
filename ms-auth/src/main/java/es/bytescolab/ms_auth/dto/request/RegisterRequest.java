package es.bytescolab.ms_auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "El nombre de usuario es necesario")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]{3,20}$",
                message = "Formato de username invalido. Solo se admiten letras, numeros y guión bajo."
        )
        @Size(min = 3, max = 20, message = "El username debe tener entre 3 y 20 caracteres")
        String username,


        @NotBlank(message = "El email es necesario")
        @Email(message = "Formato de email invalido")
        String email,

        @NotBlank(message = "La contraseña es necesaria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
                message = "La contraseña debe contener al menos una mayúscula y un número"
        )
        String password
) {
}
