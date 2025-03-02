package sg.edu.nus.iss.workshop36.server.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.workshop36.server.models.Post;
import sg.edu.nus.iss.workshop36.server.repositories.FileUploadRepository;

@Service
public class FileUploadService {
    @Autowired
    private FileUploadRepository repo;

    public String upload(MultipartFile file, String comments) 
    throws SQLException, IOException{
        return this.repo.upload(file, comments);
    }

    public Optional<Post> getPostById(String postId){
        return this.repo.getPostById(postId);
    }
}