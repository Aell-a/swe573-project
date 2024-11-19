package IDentify.Controller;

import IDentify.DTO.Media.MediaRequest;
import IDentify.DTO.Post.MiniPostDTO;
import IDentify.DTO.Post.PostDTO;
import IDentify.DTO.Post.PostRequest;
import IDentify.Entity.Post;
import IDentify.Mapper.PostMapper;
import IDentify.Service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private PostMapper postMapper;

    @GetMapping("/main")
    public ResponseEntity<List<MiniPostDTO> > getMainPagePosts(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size) {
        if (postService.getMiniPosts(page, size) != null) {
            return ResponseEntity.ok(postService.getMiniPosts(page, size));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MiniPostDTO>> getPostsByUserId(@PathVariable Long userId) {
        if (postService.getAllMiniPostsByUserId(userId) != null) {
            return ResponseEntity.ok(postService.getAllMiniPostsByUserId(userId));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<MiniPostDTO>> getPostsByTagId(@PathVariable Long tagId) {
        if (postService.getAllMiniPostsByTagId(tagId) != null) {
            return ResponseEntity.ok(postService.getAllMiniPostsByTagId(tagId));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId) {
        if (postService.getPostById(postId) != null) {
            return ResponseEntity.ok(postService.getPostById(postId));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createPost(@RequestPart("postRequest") String postRequestJSON, @RequestPart("files") List<MultipartFile> files) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PostRequest postRequest = mapper.readValue(postRequestJSON, PostRequest.class);

            List<MediaRequest> mediaRequests = new ArrayList<>();
            for (MultipartFile file : files) {
                MediaRequest mediaRequest = new MediaRequest();
                mediaRequest.setFile(file);
                mediaRequests.add(mediaRequest);
            }
            postRequest.setMediaRequests(mediaRequests);

            Post newPost = postService.createPost(postRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPost.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
