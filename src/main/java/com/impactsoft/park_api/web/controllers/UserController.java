package com.impactsoft.park_api.web.controllers;

import com.impactsoft.park_api.entities.User;
import com.impactsoft.park_api.services.UserService;
import com.impactsoft.park_api.web.dto.UserDTO;
import com.impactsoft.park_api.web.dto.UserPasswordDTO;
import com.impactsoft.park_api.web.dto.UserResponseDTO;
import com.impactsoft.park_api.web.dto.mapper.UserMapper;
import com.impactsoft.park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Has all the CRUD operations related to resources and registers")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user", description = "resource to create a new user",
            responses = {
            @ApiResponse(responseCode = "201", description = "resource successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),

            @ApiResponse(responseCode = "409", description = "User email already exists!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

            @ApiResponse(responseCode = "422", description = "Resource not processed, incorrect inputs", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.save(UserMapper.toUser(userDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDTO(user));
    }

    //api/v1/users/1
    @Operation(summary = "Recuperar um usuário pelo id", description = "Recuperar um usuário pelo id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('USER') AND #id == authentication.principal.id)")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @Operation(summary = "Atualizar senha", description = "Atualizar senha",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "400", description = "Senha não confere",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos invalidos ou mal formatados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER') AND (#id == authentication.principal.id)")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordDTO userDTO) {
        User user = userService.updatePassword(id, userDTO.getCurrentPassword(), userDTO.getNewPassword(), userDTO.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Listar todos os usuários", description = "Listar todos os usuários cadastrados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista com todos os usuários cadastrados",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))))
            })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(UserMapper.toListDTO(users));
    }

}
