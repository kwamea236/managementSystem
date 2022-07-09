package magepaharma.magepharma.dataRepository;

import magepaharma.magepharma.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
