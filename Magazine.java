package model;
import java.io.Serializable;
import java.time.LocalDateTime;
public class Magazine implements Serializable {
    private String title;
    private String description;
    private String filePath;
    private LocalDateTime uploadDate;

    public Magazine(String title, String description, String filePath) {
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.uploadDate = LocalDateTime.now();
    }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getFilePath() { return filePath; }
    public LocalDateTime getUploadDate() { return uploadDate; }
    @Override
    public String toString() {
        return "Magazine{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", filePath='" + filePath + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
