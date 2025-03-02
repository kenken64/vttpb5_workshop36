package sg.edu.nus.iss.workshop36.server.models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Post implements Serializable{
    private String postId;
    private String comments;
    private byte[] image;
    
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public String getPostId() {
        return postId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
    }

    public static Post populate(ResultSet rs) throws SQLException{
        final Post p = new Post();
        p.setPostId(rs.getString("post_id"));
        p.setComments(rs.getString("comments"));
        p.setImage(rs.getBytes("picture"));
        return p;
    }
    

}