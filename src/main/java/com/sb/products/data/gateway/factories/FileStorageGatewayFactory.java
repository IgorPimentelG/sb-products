package com.sb.products.data.gateway.factories;

import com.sb.products.data.errors.FileStorageException;
import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.gateway.FileStorageGateway;
import com.sb.products.data.usecases.storage.LoadFileAsResourceUseCase;
import com.sb.products.data.usecases.storage.StoreFileUseCase;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public abstract class FileStorageGatewayFactory {

	public static FileStorageGateway create(FileStorageGateway gateway) {
		return new Output(
		  new StoreFileUseCase(gateway),
		  new LoadFileAsResourceUseCase(gateway)
		);
	}

	public record Output(
	  StoreFileUseCase storeFileUseCase,
	  LoadFileAsResourceUseCase loadFileAsResourceUseCase
	) implements FileStorageGateway {

		@Override
		public String storeFile(MultipartFile file) throws FileStorageException {
			return storeFileUseCase.execute(new StoreFileUseCase.Input(file))
			  .fileName();
		}

		@Override
		public Resource loadFileAsResource(String filename) throws NotFoundException {
			return loadFileAsResourceUseCase.execute(new LoadFileAsResourceUseCase.Input(filename))
			  .resource();
		}
	}
}
