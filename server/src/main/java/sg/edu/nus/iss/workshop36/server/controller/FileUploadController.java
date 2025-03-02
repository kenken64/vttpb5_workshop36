package sg.edu.nus.iss.workshop36.server.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.workshop36.server.models.Post;
import sg.edu.nus.iss.workshop36.server.services.FileUploadService;

@Controller
public class FileUploadController {

    @Autowired
    private FileUploadService ffSvc;

    private static final String BASE64_PREFIX = "data:image/png;base64,";

    @PostMapping(path="/api/upload",
            consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upload(
        @RequestPart MultipartFile file,
        @RequestPart String comments
    ){
        String postId= "";
        try{
            System.out.println("comments" + comments);
            System.out.println("file" + file);
            postId = this.ffSvc.upload(file, comments);
            System.out.println(postId);
        }catch(IOException | SQLException e){
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
        }
        JsonObject payload = Json.createObjectBuilder()
            .add("postId", postId)
            .build();
            
        return ResponseEntity.ok(payload.toString());
    }

    @GetMapping(path="/api/get-image/{postId}")
    public ResponseEntity<String> retrieveImage(@PathVariable String postId){
        Optional<Post> r = this.ffSvc.getPostById(postId);
        Post p = r.get();
        String encodedString  = Base64.getEncoder().encodeToString(p.getImage());
        JsonObject payload = Json.createObjectBuilder()
                                .add("image", BASE64_PREFIX + encodedString)
                                .build();
        return ResponseEntity.ok(payload.toString());
    }
}
