package com.sb.products.data.usecases.storage;

import com.sb.products.data.errors.FileStorageException;
import com.sb.products.data.gateway.FileStorageGateway;
import org.springframework.web.multipart.MultipartFile;

public class StoreFileUseCase {

	private final FileStorageGateway fileStorageGateway;

	public StoreFileUseCase(FileStorageGateway fileStorageGateway) {
		this.fileStorageGateway = fileStorageGateway;
	}

	public Output execute(Input input) throws FileStorageException {
		var result = fileStorageGateway.storeFile(input.file());

		return new Output(result);
	}

	public record Input(MultipartFile file) {}

	public record Output(String fileName) {}
}
