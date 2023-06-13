package org.sid.cinema.dao;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.sid.cinema.entities.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
@RepositoryRestResource
@CrossOrigin("*")
public interface VilleRpository extends JpaRepository<Ville,Long>{

}
