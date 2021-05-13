package in.hangang.exception;


public class TimeTableException extends Exception  {
    private final String myMessage;

    public TimeTableException(String myMessage){
        this.myMessage = myMessage;
    }
    public String getMyMessage(){
        return myMessage;
    }
}
