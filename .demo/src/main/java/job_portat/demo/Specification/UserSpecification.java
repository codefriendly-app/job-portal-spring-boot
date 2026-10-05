package job_portat.demo.Specification;

import job_portat.demo.Entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasUsername(String username){

        return (root , query, criterialBuilder)-> {
            if(username ==null){
                return criterialBuilder.conjunction();
            }

            return criterialBuilder.equal(
                    root.get("username"), username
            );

        };
    }

    public static Specification<User> hasEmail(String email){

        return (root,query,criterialBuilder)-> {
            if(email==null){
                return  criterialBuilder.conjunction();
            }
           return  criterialBuilder.equal(
                    root.get("email"), email
            );
        };
    }
}
