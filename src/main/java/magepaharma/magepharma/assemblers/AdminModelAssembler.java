package magepaharma.magepharma.assemblers;

import magepaharma.magepharma.controllers.AdminRestController;
import magepaharma.magepharma.dataRepository.AdminRepository;
import magepaharma.magepharma.models.Admin;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AdminModelAssembler implements RepresentationModelAssembler<Admin, EntityModel<Admin>> {
    @Override
    public EntityModel<Admin> toModel(Admin admin){
        return EntityModel.of(admin,
                linkTo(methodOn(AdminRestController.class).one(admin.getId())).withSelfRel(),
                linkTo(methodOn(AdminRestController.class).all()).withRel("administrators"));
    }
}
