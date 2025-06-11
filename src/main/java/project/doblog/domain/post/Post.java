package project.doblog.domain.post;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "post_title")
    private String title;

    @Lob
    @Column(name = "post_content")
    private String content;

    @Builder
    private Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                .title(this.title)
                .content(this.content);
    }

    public void change(PostEditor editor) {
        this.title = editor.getTitle();
        this.content = editor.getContent();
    }
}
