package com.sb.products.infra.controller.dtos;

public record UploadFileDto(
  String filename,
  String fileDownloadUri,
  String fileType,
  long size
) {}
