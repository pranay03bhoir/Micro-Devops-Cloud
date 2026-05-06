package com.pranay.easybuy.products.services.serviceImpl;

import com.pranay.easybuy.products.exceptions.InvalidRequestException;
import com.pranay.easybuy.products.services.ImageStorageService;
import io.imagekit.client.ImageKitClient;
import io.imagekit.client.okhttp.ImageKitOkHttpClient;
import io.imagekit.models.files.FileUploadParams;
import io.imagekit.models.files.FileUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {

	private final String privateKey;
	private final String publicKey;
	private final String folder;
	private final String endPoint;

	public ImageStorageServiceImpl(@Value("${IMAGEKIT_PRIVATE_KEY}") String privateKey,
			@Value("${IMAGEKIT_PUBLIC_KEY}") String publicKey, @Value("${IMAGEKIT_FOLDER}") String folder,
			@Value("${IMAGEKIT_URL_ENDPOINT}") String endPoint) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
		this.folder = folder;
		this.endPoint = endPoint;
	}

	@Override
	public String uploadImage(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new InvalidRequestException("Image file cannot be empty");
		}
		validateConfig();
		try {
			ImageKitClient client = ImageKitOkHttpClient.builder().privateKey(privateKey).build();
			FileUploadParams params = FileUploadParams.builder().file(file.getBytes()).fileName(resolveFileName(file))
					.folder(folder).build();
			FileUploadResponse response = client.files().upload(params);
			return response.url().orElseThrow(() -> new IllegalStateException("ImageKit did not return a public URL"));
		} catch (IOException e) {
			throw new IllegalStateException("Failed to upload product image to ImageKit.", e);
		}
	}

	private void validateConfig() {
		if (privateKey.isBlank()) {
			throw new InvalidRequestException("Imagekit credentials are not configured");
		}
	}

	private String resolveFileName(MultipartFile file) {
		String originalFileName = file.getOriginalFilename();
		if (originalFileName == null || originalFileName.isBlank()) {
			return UUID.randomUUID() + ".jpg";
		}
		return originalFileName;
	}
}
