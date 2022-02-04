package com.tms.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.activation.MimetypesFileTypeMap;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.tms.configuration.JwtUtils;
import com.tms.entity.AssignmentQuestionsBO;
import com.tms.entity.AssignmentSectionBO;
import com.tms.entity.HomeworkBO;
import com.tms.entity.HomeworkFilesBO;
import com.tms.repository.HomeworkFileRepository;
import com.tms.repository.HomeworkRepository;
import com.tms.repository.SectionRepository;
import com.tms.service.HomeworkUpload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HomeworkUploadImpl implements HomeworkUpload {

	@Autowired
	HomeworkRepository homeworkRepository;

	@Autowired
	HomeworkFileRepository homeworkFileRepository;

	@Autowired
	SectionRepository sectionRepository;

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	@Value("${aws.s3.region}")
	private String region;

	@Value("${cloudfront.file.url}")
	private String BASE_URL;

	@Autowired
	JwtUtils jwtUtils;

	@Override
	public String teacherUploadingHomework(Long homeworkId, MultipartFile[] file, String description) {
		HomeworkBO hw = homeworkRepository.findById(homeworkId).orElse(null);

		if (hw == null) {

			System.out.println("edit homework record");
			return "Homework creation not exit for this id";

		} else {

			hw.setDescription(description);
			hw.setUpdatedOn(new Date());
			hw.setStatus("ASSIGN-STUDENT");
			homeworkRepository.save(hw);
			for (MultipartFile multipartFile : file) {
				String homeworkUploadStr = null;
				HomeworkFilesBO hwf = new HomeworkFilesBO();
				File fileVal = convertMultiPartFileToFile(multipartFile);
				if (fileVal.isFile()) {
					MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
					String mimeType = fileTypeMap.getContentType(fileVal.getName());
					hwf.setFileSize(fileVal.length());
					hwf.setFileType(mimeType);
					homeworkUploadStr = uploadFile(multipartFile, hwf, false);
					hwf.setLocation(homeworkUploadStr);
					hwf.setName(multipartFile.getOriginalFilename());
					hwf.setHomeWorkId(hw);
					hwf.setCreatedOn(new Date());
					hwf.setUpdatedOn(new Date());
					hwf.setCreatedBy(jwtUtils.getUserId());
					hwf.setUpdatedBy(jwtUtils.getUserId());
					homeworkFileRepository.save(hwf);
				}

			}

		}

		return "Success";
	}

	@Async
	public String uploadFile(MultipartFile multipartFile, HomeworkFilesBO homeworkFile, boolean isReplace) {
		S3Object s3Object = null;
		final File file = convertMultiPartFileToFile(multipartFile);
		String homeworkUpload = null;
		try {
			if (isReplace) {
				deleteFile(homeworkFile);
			}
			s3Object = uploadFileToS3Bucket(bucketName, file);
			log.info("File upload is completed.");

			homeworkUpload = BASE_URL + s3Object.getBucketName().substring(
					s3Object.getBucketName().indexOf("b2blmsdev") + "b2blmsdev".length(),
					s3Object.getBucketName().length()) + "/" + s3Object.getKey();

		} catch (final AmazonServiceException ex) {
			log.info("File upload is failed.");
			log.error("Error= {} while uploading file.", ex.getMessage());
		} finally {
			file.delete(); // To remove the file locally created in the project folder.
			if (s3Object != null) {
				try {
					s3Object.close();
				} catch (IOException e) {
					log.error("Error closing s3 Object: " + e.getMessage());
				}
			}
		}
		return homeworkUpload;
	}

	public byte[] downloadFile(String fileName) {
		S3Object s3Object = amazonS3.getObject(bucketName, fileName);
		S3ObjectInputStream inputstream = s3Object.getObjectContent();
		try {
			byte[] content = IOUtils.toByteArray(inputstream);
			return content;
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Async
	public void deleteFile(HomeworkFilesBO hwfile) {

		final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName,
				hwfile.getLocation().substring(hwfile.getLocation().lastIndexOf("/") + 1));
		amazonS3.deleteObject(deleteObjectRequest);
		hwfile.setLocation(null);
		hwfile.setCreatedBy(jwtUtils.getUserId());
		hwfile.setUpdatedBy(jwtUtils.getUserId());
		homeworkFileRepository.save(hwfile);
	}

	protected File convertMultiPartFileToFile(final MultipartFile multipartFile) {
		final File file = new File(multipartFile.getOriginalFilename());
		try (final FileOutputStream outputStream = new FileOutputStream(file)) {
			outputStream.write(multipartFile.getBytes());
		} catch (final IOException ex) {
			log.error("Error converting the multi-part file to file= ", ex.getMessage());
		}
		return file;
	}

	private S3Object uploadFileToS3Bucket(final String bucketName, final File file) {
		final String uniqueFileName = System.currentTimeMillis() + "_" + file.getName();
		log.info("Uploading file with name= " + uniqueFileName);
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
		amazonS3.putObject(putObjectRequest);
		S3Object s3Object = amazonS3.getObject(bucketName, uniqueFileName);
		return s3Object;
	}

	@Override
	public String teacherDeleteHomework(Long homeworkFileId) {
		HomeworkFilesBO hwf = homeworkFileRepository.findById(homeworkFileId).orElse(null);

		if (hwf != null) {
			deleteFile(hwf);

		} else {

			return "homework file dosn't matching in system";
		}

		return "Success";
	}

	@Override
	public String teacherPublishHomework(Long homeworkId) {

		HomeworkBO hw = homeworkRepository.findById(homeworkId).orElse(null);

		if (hw != null) {
			hw.setActive(true);
			hw.setStatus("Publish");
			hw.setUpdatedOn(new Date());
			hw.setUpdatedBy(jwtUtils.getUserId());
			homeworkRepository.save(hw);
			Float pMark = 0f;
			if (hw.getType().equalsIgnoreCase("ONLINE")) {

				for (AssignmentSectionBO assignmentSection : hw.getAssignmentSections()) {

					if (assignmentSection.isActive()) {
						for (AssignmentQuestionsBO assignmentQuestions : assignmentSection.getAssignmentQuestions()) {
							if (assignmentQuestions.isActive()) {

								pMark += assignmentSection.getPositiveMarks();
							}

						}

						assignmentSection.setTotalMarks(pMark);
						assignmentSection.setUpdatedOn(new Date());
						assignmentSection.setUpdatedBy(jwtUtils.getUserId());
					}
					sectionRepository.save(assignmentSection);
				}

			}

		} else {
			return "homework  dosn't matching in system";
		}

		return "Success";

	}

}
