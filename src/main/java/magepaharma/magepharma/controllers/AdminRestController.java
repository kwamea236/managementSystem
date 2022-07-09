package magepaharma.magepharma.controllers;

import magepaharma.magepharma.Exceptions.AdminNotFoundException;
import magepaharma.magepharma.assemblers.AdminModelAssembler;
import magepaharma.magepharma.dataRepository.AdminRepository;
import magepaharma.magepharma.models.Admin;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/admin")
public class AdminRestController {
    private final AdminRepository adminRepository;
    private final AdminModelAssembler assembler;

    public AdminRestController(AdminRepository adminRepository, AdminModelAssembler assembler){
        this.adminRepository = adminRepository;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Admin>> all() throws AdminNotFoundException {
        List<EntityModel<Admin>> admins = adminRepository.findAll().stream()
                .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(admins,
                linkTo(methodOn(AdminRestController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Admin> one(@PathVariable Long id){
        Admin admin = adminRepository.findById(id).orElseThrow(()->new AdminNotFoundException(id));
        return assembler.toModel(admin);
    }

    @PostMapping
    ResponseEntity<?> newAdmin(@RequestBody Admin admin){
        EntityModel<Admin> entityModel = assembler.toModel(adminRepository.save(admin));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceEmployee(@PathVariable Long id, @RequestBody Admin admin){
        Admin updateAdmin = adminRepository.findById(id)
                .map(nAdmin ->{
                    nAdmin.setUsername(admin.getUsername());
                    nAdmin.setPassword(admin.getPassword());
                    return adminRepository.save(admin);
                }).orElseGet(()->{
                    admin.setId(id);
                    return adminRepository.save(admin);
                });
        EntityModel<Admin> entityModel = assembler.toModel(updateAdmin);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteAdmin(@PathVariable Long id){
        adminRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
