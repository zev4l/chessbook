package domain;

public class MoveError {
    private String message;

    public MoveError(){};

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
