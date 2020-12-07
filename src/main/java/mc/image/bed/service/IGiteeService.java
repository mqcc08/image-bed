package mc.image.bed.service;

import mc.image.bed.entity.AccordionEntity;
import mc.image.bed.entity.GiteeOssResponse;
import mc.image.bed.entity.HttpResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IGiteeService {

    public List<AccordionEntity> refresh(String dirPath);

    public HttpResponseEntity createFile(String dirPath, MultipartFile multipartFile);

    public HttpResponseEntity delete(String fileName, String dirPath, String sha);

    public GiteeOssResponse getFiles(String sha);

    public String getFileBlob(String sha);
}
