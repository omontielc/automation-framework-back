package api.models.request;

/**
 * POJO representing the body of a Create Post request.
 * @author Osiris Montiel Campos
 * @version 2025-07-06
 */
public class ATC01APIOneRequest {

    private String title;
    private String body;
    private int    userId;

    /**
     * Creates a new post request body.
     * @param title  title of the post
     * @param body   content of the post
     * @param userId ID of the user creating the post
     */
    public ATC01APIOneRequest(String title, String body, int userId) {
        this.title  = title;
        this.body   = body;
        this.userId = userId;
    }
    
    // ----------------------------------------------------------------
    // Getters & Setters 
    // ----------------------------------------------------------------
    
    public String getTitle(){ 
    	return title; 
    }
    public void setTitle(String title){ 
    	this.title = title;
    }

    public String getBody(){ 
    	return body; 
    }
    public void setBody(String body){ 
    	this.body = body; 
    }

    public int getUserId(){ 
    	return userId; 
    }
    public void setUserId(int userId){ 
    	this.userId = userId;
    }
}
