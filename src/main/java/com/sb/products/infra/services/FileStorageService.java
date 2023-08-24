package com.sb.products.infra.services;

import com.sb.products.data.errors.FileStorageException;
import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.gateway.FileStorageGateway;
import com.sb.products.main.config.global.FileStorageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FileStorageService implements FileStorageGateway {

	private final Path fileStorageLocation;
	private final Logger logger = Logger.getLogger(FileStorageService.class.getName());

	@Autowired
	public FileStorageService(FileStorageConfig fileStorageConfig) throws FileStorageException {
		fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
		  .toAbsolutePath().normalize();

		try {
			Files.createDirectories(fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException();
		}
	}

	@Override
	public String storeFile(MultipartFile file) throws FileStorageException {

		String fileName = StringUtils.cleanPath(
		  Objects.requireNonNull(file.getOriginalFilename())
		);

		try {
			if (fileName.contains("..")) {
				logger.log(Level.WARNING, "[V1] File with incorrect filename");
				throw new FileStorageException();
			}

			Path targetLocation = fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			logger.log(Level.INFO, "[V1] File stored successfully");

			return fileName;
		} catch (Exception e) {
			logger.log(Level.WARNING, "[V1] cannot store file");
			throw new FileStorageException();
		}
	}

	public Resource loadFileAsResource(String filename) throws NotFoundException {
		try {
			Path filePath = fileStorageLocation.resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists()) {
				logger.log(Level.INFO, "[V1] File found successfully");
				return resource;
			}

			logger.log(Level.WARNING, "[V1] File not found");
			throw new NotFoundException(filename);
		} catch(Exception e) {
			throw new NotFoundException(filename);
		}
	}
}
