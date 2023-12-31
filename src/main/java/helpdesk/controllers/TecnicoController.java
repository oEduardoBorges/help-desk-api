package helpdesk.controllers;

import helpdesk.models.Tecnico;
import helpdesk.models.dtos.TecnicoDTO;
import helpdesk.services.TecnicoService;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/tecnicos")
public record TecnicoController(TecnicoService tecnicoService) {

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TecnicoDTO> criarTecnico(@Valid @RequestBody TecnicoDTO tecnicoDTO) {
        Tecnico novoTecnico = tecnicoService.criarTecnico(tecnicoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novoTecnico.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(uri).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TecnicoDTO> buscarTecnicoPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(new TecnicoDTO(tecnicoService.buscarTecnicoPorId(id)));
    }

    @GetMapping
    public ResponseEntity<Page<TecnicoDTO>> buscarTodosTecnicos(Pageable pageable) {
        Page<Tecnico> page = tecnicoService.buscarTodosTecnicos(pageable);
        Page<TecnicoDTO> pageDTO = page.map(TecnicoDTO::new);

        return ResponseEntity.status(HttpStatus.OK).body(pageDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<TecnicoDTO> atualizarTecnico(@PathVariable Long id, @Valid @RequestBody TecnicoDTO tecnicoDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(new TecnicoDTO(tecnicoService.atualizarTecnico(id, tecnicoDTO)));
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> excluirTecnico(@PathVariable Long id) {
        tecnicoService.excluirTecnico(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
