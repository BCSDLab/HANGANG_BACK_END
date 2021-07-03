package in.hangang.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadFile {
    private Long id;
    private Long lecture_bank_id;
    @ApiModelProperty(hidden = true)
    private String url;
    private String fileName;
    private String ext;
    private Long size;
    private Long user_id;
    private String file_ext;
    @ApiModelProperty(hidden = true)
    private Timestamp created_at;
    @ApiModelProperty(hidden = true)
    private Timestamp updated_at;

    public UploadFile(){

    }


    public UploadFile(String url, String fileName, String file_ext, String ext, Long user_id, Long size){
        this.url = url;
        this.fileName = fileName;
        this.file_ext = file_ext;
        this.ext = ext;
        this.user_id = user_id;
        this.size = size;
    }

    public String getFile_ext() {
        return file_ext;
    }

    public void setFile_ext(String file_ext) {
        this.file_ext = file_ext;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLecture_bank_id() {
        return lecture_bank_id;
    }

    public void setLecture_bank_id(Long lecture_bank_id) {
        this.lecture_bank_id = lecture_bank_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
