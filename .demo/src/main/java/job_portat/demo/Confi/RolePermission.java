package job_portat.demo.Confi;

import job_portat.demo.Entity.Permission;
import job_portat.demo.Entity.Role;
import job_portat.demo.Repository.PermissionRepository;
import job_portat.demo.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RolePermission {

    @Bean
    CommandLineRunner initializeDatabase(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ){
        return args -> {
            // 1️⃣ Step 1: Ensure all permissions exist in the database
            Permission read = getOrCreatePermission(permissionRepository, "USER_READ");
            Permission create = getOrCreatePermission(permissionRepository, "USER_CREATE");
            Permission update = getOrCreatePermission(permissionRepository, "USER_UPDATE");
            Permission delete = getOrCreatePermission(permissionRepository, "USER_DELETE");

            // 2️⃣ Step 2: Ensure Roles exist and map their Permissions

            // Setup USER role
            Role userRole = getOrCreateRole(roleRepository, "USER");
            userRole.getPermission().clear();
            userRole.getPermission().add(read);
            userRole.getPermission().add(update);
            roleRepository.save(userRole);

            // Setup ADMIN role
            Role adminRole = getOrCreateRole(roleRepository, "ADMIN");
            adminRole.getPermission().clear();
            adminRole.getPermission().add(read);
            adminRole.getPermission().add(create);
            adminRole.getPermission().add(update);
            adminRole.getPermission().add(delete);
            roleRepository.save(adminRole);
        };
    }

    // Helper to fetch permission, or create it if missing
    private Permission getOrCreatePermission(PermissionRepository repo, String name) {
        return repo.findByName(name).orElseGet(() -> repo.save(new Permission(name)));
    }

    // 🔑 Fixed: Correctly handles Optional.empty() using orElseGet
    private Role getOrCreateRole(RoleRepository repo, String name) {
        return repo.findByName(name).orElseGet(() -> repo.save(new Role(name)));
    }
}
