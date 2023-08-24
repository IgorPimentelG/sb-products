package com.sb.products.infra.controller;

import com.sb.products.data.errors.FileStorageException;
import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.gateway.FileStorageGateway;
import com.sb.products.data.gateway.factories.FileStorageGatewayFactory;
import com.sb.products.infra.controller.docs.file.DownloadDoc;
import com.sb.products.infra.controller.docs.file.UploadDoc;
import com.sb.products.infra.controller.docs.file.UploadMultiplesDoc;
import com.sb.products.infra.controller.dtos.UploadFileDto;
import com.sb.products.infra.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/file")
@Tag(name = "File", description = "Endpoints for managing files")
public class FileController {

	private final FileStorageGateway gateway;

	@Autowired
	public FileController(FileStorageService gateway) {
		this.gateway = FileStorageGatewayFactory.create(gateway);
	}

	@UploadDoc
	@PostMapping(value = "/v1")
	public ResponseEntity<UploadFileDto> uploadFile(@RequestParam("file") MultipartFile file)
	  throws FileStorageException {
		var fileName = gateway.storeFile(file);

		var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
		  .path("/api/file/v1/download/")
		  .path(fileName)
		  .toUriString();

		var body = new UploadFileDto(
		  fileName,
		  fileDownloadUri,
		  file.getContentType(),
		  file.getSize()
		);

		return ResponseEntity.ok(body);
	}

	@UploadMultiplesDoc
	@PostMapping(value = "/v1/upload-multiple")
	public ResponseEntity<List<UploadFileDto>> uploadMultipleFile(@RequestParam("files") MultipartFile[] files)
	  throws FileStorageException {

		var body = Arrays.stream(files).map(file -> {
			try {
				return uploadFile(file).getBody();
			} catch (FileStorageException e) {
				throw new RuntimeException(e);
			}
		}).toList();

		return ResponseEntity.ok(body);
	}

	@DownloadDoc
	@GetMapping(value = "/v1/download/{filename:.+}")
	public ResponseEntity<Resource> downloadFile(
	  @PathVariable String filename,
	  HttpServletRequest request
	) throws NotFoundException, IOException {
		var resource = gateway.loadFileAsResource(filename);
		var contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

		if (contentType.isBlank()) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
		  .contentType(MediaType.parseMediaType(contentType))
		  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
		  .body(resource);

	}
}
