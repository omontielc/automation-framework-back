package api.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO representing a Post returned by the API.
 * @author Osiris Montiel Campos
 * @version 2025-10-08
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class APIOneResponse {

    private int    userId;
    private int    id;
    private String title;
    private String body;

    // ----------------------------------------------------------------
    // Getters & Setters
    // ----------------------------------------------------------------

    public int getUserId(){
    	return userId;
    }
    public void setUserId(int userId){
    	this.userId = userId;
    }
    public int getId(){
    	return id;
    }
    public void setId(int id){
    	this.id = id;
    }
    public String getTitle(){
    	return title;
    }
    public void setTitle(String title)
    {
    	this.title = title;
    }
    public String getBody(){
    	return body;
    }
    public void setBody(String body){
    	this.body = body;
    }
    @Override
    public String toString() {
        return "APIResponse{userId=" + userId + ", id=" + id + ", title='" + title + "', body='" + body + "'}";
    }
}
