package com.futing.diary.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.futing.diary.config.CloudinaryConfig;
import com.futing.diary.util.dto.CloudinaryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CloudinaryUtility {
  private final CloudinaryConfig cloudinaryConfig;

  public CloudinaryResponseDTO uploadOnCloudinary(MultipartFile multipartFile) throws IOException {
    File file = convertMultipartFileToFile(multipartFile);
    Cloudinary cloudinary =
      new Cloudinary("cloudinary://"
        + cloudinaryConfig.getApiKey() + ":"
        + cloudinaryConfig.getApiSecret() + "@"
        + cloudinaryConfig.getCloudName());

    Map<?, ?> result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
    file.delete();
    return CloudinaryResponseDTO.builder()
      .publicId((String) result.get("public_id"))
      .url((String) result.get("secure_url"))
      .build();
  }

  public void deleteImageFromCloudinary(String publicId) throws Exception {
    Cloudinary cloudinary =
      new Cloudinary("cloudinary://" + cloudinaryConfig.getApiKey() + ":"
        + cloudinaryConfig.getApiSecret() + "@"
        + cloudinaryConfig.getCloudName());
    cloudinary.api().deleteResources(new ArrayList<>(List.of(publicId)), ObjectUtils.emptyMap());
  }

  public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
    File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
    try {
      convFile.createNewFile();
      FileOutputStream fos = new FileOutputStream(convFile);
      fos.write(multipartFile.getBytes());
      fos.close();

    } catch (IOException e) {
      throw new IOException("Exception while converting MultipartFile to file.");
    }

    return convFile;
  }
}
