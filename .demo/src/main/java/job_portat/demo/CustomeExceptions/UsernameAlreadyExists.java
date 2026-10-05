package job_portat.demo.CustomeExceptions;

public class UsernameAlreadyExists extends RuntimeException{
    public UsernameAlreadyExists(String message){
        super(message);
        }
}
