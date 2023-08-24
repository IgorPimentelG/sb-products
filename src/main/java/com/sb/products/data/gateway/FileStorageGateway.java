package com.sb.products.data.gateway;

import com.sb.products.data.errors.FileStorageException;
import com.sb.products.data.errors.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageGateway {
	String storeFile(MultipartFile multipartFile) throws FileStorageException;
	Resource loadFileAsResource(String filename) throws NotFoundException;
}
