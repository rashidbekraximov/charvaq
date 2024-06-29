package uz.cluster.services.references_service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.cluster.constants.FileNames;
import uz.cluster.entity.references.model.FileEntity;
import uz.cluster.repository.references.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public FileEntity saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = FileNames.FILE_FOLDER + fileName;
        long fileSize = file.getSize();

        // Save the file to the specified upload directory
        byte[] fileData = file.getBytes();
        Path uploadPath = Paths.get(FileNames.FILE_FOLDER);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path destination = Paths.get(FileNames.FILE_FOLDER + fileName);
        Files.write(destination, fileData);

        // Create and save file entity
        FileEntity fileEntity = new FileEntity(fileName, filePath, fileSize);
        return fileRepository.save(fileEntity);
    }

    public FileEntity getFile(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    public void deleteFile(Long id) {
        try{
            fileRepository.deleteById(id);
            log.info("File muvaffaqiyatli o'chirildi !");
        }catch (Exception e){
            log.error("File o'chirishda xatolik yuzaga keldi !");
        }
    }
}


