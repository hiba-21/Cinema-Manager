package org.sid.cinema.dao;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.sid.cinema.entities.Projection;
import org.springframework.data.jpa.repository.JpaRepository;
@RepositoryRestResource
@CrossOrigin("*")
public interface ProjectionRpository extends JpaRepository<Projection,Long>{

}
