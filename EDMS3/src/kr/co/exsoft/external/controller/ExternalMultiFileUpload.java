package kr.co.exsoft.external.controller;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ExternalMultiFileUpload {
	
	private List<MultipartFile> crunchifyFiles;
	 
    public List<MultipartFile> getFiles() {
        return crunchifyFiles;
    }
 
    public void setFiles(List<MultipartFile> files) {
        this.crunchifyFiles = files;
    }

}
