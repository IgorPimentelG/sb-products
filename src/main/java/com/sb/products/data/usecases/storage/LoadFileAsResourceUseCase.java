package com.sb.products.data.usecases.storage;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.gateway.FileStorageGateway;
import org.springframework.core.io.Resource;

public class LoadFileAsResourceUseCase {

	private final FileStorageGateway fileStorageGateway;

	public LoadFileAsResourceUseCase(FileStorageGateway fileStorageGateway) {
		this.fileStorageGateway = fileStorageGateway;
	}

	public Output execute(Input input) throws NotFoundException {
		var result = fileStorageGateway.loadFileAsResource(input.filename());

		return new Output(result);
	}

	public record Input(String filename) {}

	public record Output(Resource resource) {}
}
