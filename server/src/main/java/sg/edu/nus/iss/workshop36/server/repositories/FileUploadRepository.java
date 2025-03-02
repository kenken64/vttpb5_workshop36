package sg.edu.nus.iss.workshop36.server.repositories;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.workshop36.server.models.Post;

@Repository
public class FileUploadRepository {
    private static final String INSERT_POST_SQL
            = "INSERT INTO posts( picture, comments, post_id) VALUES (?,?,?)";
    
    private static final String SQL_GET_POST_BY_POSTID
            = "select post_id , comments, picture from posts where post_id=?";
    
    @Autowired
    private DataSource dataSource;
    

    @Autowired
    private JdbcTemplate template;

    public String upload(MultipartFile file, String comments) throws SQLException, IOException{
        try(Connection con = dataSource.getConnection(); 
            PreparedStatement prstmt = con.prepareStatement(INSERT_POST_SQL)){
            String uid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            InputStream is = file.getInputStream();
            prstmt.setBinaryStream(1, is, file.getSize());
            prstmt.setString(2, comments);
            prstmt.setString(3, uid);
            prstmt.executeUpdate();
            return uid;
        }
    }

    public Optional<Post> getPostById(String postId){
        return template.query(
            SQL_GET_POST_BY_POSTID,
            (ResultSet rs)->{
                if(!rs.next())
                    return Optional.empty();
                final Post post = Post.populate(rs);
                return Optional.of(post);
            }
        , postId);
    }
}