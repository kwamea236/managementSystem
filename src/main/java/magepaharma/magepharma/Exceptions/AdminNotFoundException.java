package magepaharma.magepharma.Exceptions;

public class AdminNotFoundException extends RuntimeException{
    public AdminNotFoundException(Long id){
        super("Admin not found" + id);
    }
}
