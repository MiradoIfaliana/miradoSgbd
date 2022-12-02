package exception;


public class RQTException extends Exception{
      String message;
      public RQTException(String msg){   
            message=msg;  
      }
      public String getMessage() {
            return message;
      }
      public void setMessage(String message) {
            this.message = message;
      }
      
      
}