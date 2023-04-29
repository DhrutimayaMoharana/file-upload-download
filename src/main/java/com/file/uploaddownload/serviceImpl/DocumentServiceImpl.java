package com.file.uploaddownload.serviceImpl;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.file.uploaddownload.config.AwsAdapter;
import com.file.uploaddownload.config.FileUploadProperties;
import com.file.uploaddownload.dto.DocumentRequestDto;
import com.file.uploaddownload.dto.Response;
import com.file.uploaddownload.model.Document;
import com.file.uploaddownload.repository.DocumentRepository;
import com.file.uploaddownload.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	private DocumentRepository documentRepository;

	private Path dirLocation;

//	@Value("${aws.s3.bucket}")
//	private String awsPath;

	@Autowired
	private AwsAdapter awsAdapter;

	@Autowired
	public DocumentServiceImpl(FileUploadProperties fileUploadProperties) {
		this.dirLocation = Paths.get(fileUploadProperties.getLocation()).toAbsolutePath().normalize();
	}

	@PostConstruct
	public void init() throws Throwable {
		try {
			Files.createDirectories(this.dirLocation);
		} catch (Exception ex) {
			throw new Throwable("Could not create upload dir!");
		}

	}

	@Override
	public Response<?> uploadDocumentAndStoreInDBV1(DocumentRequestDto documentRequestDto) throws Exception {

		byte[] fileContent = documentRequestDto.getDocument().getBytes();
		String encodedString = Base64.getEncoder().encodeToString(fileContent);

		Document document = documentRepository.save(new Document(null, documentRequestDto.getFileName(),
				documentRequestDto.getFileType(), encodedString, false));

		return new Response<>(HttpStatus.OK.value(), "Document save in DB successfully!!!",
				document.convertToDocumentDto());
	}

	@Override
	public Response<?> storeFileInLocalDirectory(MultipartFile file, Long currentDate) {
		String fileName = StringUtils.cleanPath(currentDate + file.getOriginalFilename());

		try {
			Path targetLocation = this.dirLocation.resolve(fileName);

//			Path awsTargetLocation = Paths.get(awsPath).resolve(fileName);

			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//			Files.copy(file.getInputStream(), awsTargetLocation, StandardCopyOption.REPLACE_EXISTING);
			String fileUploadPath = dirLocation + "/" + fileName;
			return new Response<>(HttpStatus.OK.value(), "File successfull uploaded", fileUploadPath);
		} catch (IOException ex) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "File fail to uploaded", null);
		}
	}

	@Override
	public Response<?> storeFileInLocalDirectoryResponseIsDownloadUrl(MultipartFile file, Long currentDate) {
		String fileName = StringUtils.cleanPath(currentDate + file.getOriginalFilename());

		try {
			Path targetLocation = this.dirLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/document/download/")
					.path(fileName).toUriString();
			return new Response<>(HttpStatus.OK.value(), "File successfull uploaded", fileDownloadUri);
		} catch (IOException ex) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "File fail to uploaded", null);
		}
	}

	@Override
	public Resource downloadDocument(String fileName) {

		try {

			Path file = this.dirLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not find file");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Could not download file");
		}

	}

	@Override
	public Response<?> compressTheFile(MultipartFile file, Long currentDate, Float compressQuality) throws Exception {
		String fileName = StringUtils.cleanPath(currentDate + file.getOriginalFilename());

		RenderedImage image = ImageIO.read(file.getInputStream());
		ImageWriter jpegWriter = ImageIO.getImageWritersByFormatName("jpg").next();
		ImageWriteParam jpegWiterParam = jpegWriter.getDefaultWriteParam();
		jpegWiterParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpegWiterParam.setCompressionQuality(compressQuality.floatValue());
		try (ImageOutputStream outputStream = ImageIO
				.createImageOutputStream(new java.io.File(dirLocation + "/" + fileName))) {
			jpegWriter.setOutput(outputStream);
			IIOImage outputImage = new IIOImage(image, null, null);
			jpegWriter.write(null, outputImage, jpegWiterParam);
			return new Response<>(HttpStatus.OK.value(), "File successfull uploaded", dirLocation + "/" + fileName);
		}

		catch (IOException ex) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "File fail to uploaded", null);
		}
	}

	@Override
	public Response<?> storeInAwsS3(MultipartFile file, Long currentDate) throws Exception {
		String fileName = StringUtils.cleanPath(currentDate + file.getOriginalFilename());

		URL url = awsAdapter.storeObjectInS3(file, fileName, file.getContentType());

		Document document = documentRepository
				.save(new Document(null, fileName, file.getContentType().toString(), url.toString(), true));

		return new Response<>(HttpStatus.OK.value(), "File successfull uploaded", document);

	}

	@Override
	public HttpServletResponse downloadDocumentFromAwsS3Bucket(String fileName, HttpServletResponse response) {

		S3Object s3Obj = awsAdapter.fetchObject(fileName);

		InputStream stream = s3Obj.getObjectContent();
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		try {
			IOUtils.copy(stream, response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}

}
