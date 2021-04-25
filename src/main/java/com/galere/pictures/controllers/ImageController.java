package com.galere.pictures.controllers;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.Concept;
import com.clarifai.grpc.api.Data;
import com.clarifai.grpc.api.Input;
import com.clarifai.grpc.api.MultiOutputResponse;
import com.clarifai.grpc.api.PostModelOutputsRequest;
import com.clarifai.grpc.api.V2Grpc;
import com.clarifai.grpc.api.status.StatusCode;
import com.galere.pictures.entities.Image;
import com.galere.pictures.services.ICategoryService;
import com.galere.pictures.services.IImageService;
import com.google.protobuf.ByteString;

@Controller
public class ImageController {
	
    @Autowired
    private IImageService imageService;
    
    @Autowired
    private ICategoryService categoryService;

    private V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
    	    .withCallCredentials(new ClarifaiCallCredentials("59a0e133b42a4af78321abb08557bd84"));
    
    //	Lister ou rechercher des images (par mots clés)
	@RequestMapping(value = { "/admin/images" }, method = RequestMethod.GET)
    public String listImages(@RequestParam(value = "tags", required = false) String tags, 
    						Model model) {
		
		List<Image> images;
		
		if (tags != null && !tags.equals(""))
			images = imageService.searchByTags(tags);
		else
			images = imageService.getRepository().findAll();
		
		model.addAttribute("images", images);
        return "admin/image/ImageList";
        
    }
	
	//	Redirect to image creation page
	@RequestMapping(value = { "/admin/image/new" }, method = RequestMethod.POST)
    public String uploadImage(@RequestParam MultipartFile multipartImage, Model model) {
		
		Image img = new Image();
		img.setContent("");
		img.setDate(LocalDate.now());
		img.setDescription("");
		img.setTags("");
		img.setTitle("");

        try {
			img = imageService.saveImage(img, multipartImage.getBytes(), multipartImage.getOriginalFilename());
        
	        MultiOutputResponse response = stub.postModelOutputs(
	        	    PostModelOutputsRequest.newBuilder()
	        	        .setModelId("aaa03c23b3724a16a56b629203edc62c")
	        	        .addInputs(
	        	            Input.newBuilder().setData(
	        	                Data.newBuilder().setImage(
//	        	                	com.clarifai.grpc.api.Image.newBuilder().setUrl("localhost:8081/api/show/" + img.getId())//.parseFrom(multipartImage.getBytes())
	        	                	com.clarifai.grpc.api.Image.newBuilder()
	        	                        .setBase64(ByteString.copyFrom(Files.readAllBytes(
	        	                            new File(img.getUrl()).toPath()
        	                        )))
	        	                )
	        	            )
	        	        )
	        	        .build()
	        	);

        	if (response.getStatus().getCode() != StatusCode.SUCCESS) 
        		throw new RuntimeException("Request failed, status: " + response.getStatus());
        	else {
        		String imgContent = "- ";
        		for (Concept c : response.getOutputs(0).getData().getConceptsList()) {
        			if (c.getValue() > 0.95) imgContent += c.getName() + " - ";
        			if (c.getName().equalsIgnoreCase("person") ||
        					c.getName().equalsIgnoreCase("man") ||
        					c.getName().equalsIgnoreCase("women")) model.addAttribute("withPerson", "");
        		}
        		img.setContent(imgContent);
        	}
        	
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
		model.addAttribute("image", img);
		model.addAttribute("creation", "");
		model.addAttribute("categories", categoryService.getRepository().findAll());
        return "admin/image/ImageEdition";
        
    }
	
	//	Show image on browser
	@RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public String showImage(@PathVariable Long id, Model model) throws Exception {
        
		model.addAttribute("image", imageService.getRepository().findById(id).get());
		
		return "shared/ShowImage";
		
    }
	
	//	Download image
	@RequestMapping(value = "/shared/image/{id}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE }, method = RequestMethod.GET)
    public ResponseEntity getImage(@PathVariable Long id) throws Exception {
        
		FileSystemResource img = imageService.findImage(id);
		
		return ResponseEntity.ok()
				.contentType(img.getFilename().contains(".png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + img.getFilename() + "\"")
				.body(img);
    }
	
	
	//	Redirect to image edit page
	@RequestMapping(value = { "/admin/images/{id}/edit" }, method = RequestMethod.GET)
    public String editUser(@PathVariable Long id, Model model) {
		
		if (imageService.getRepository().existsById(id)) {
		
			model.addAttribute("image", imageService.getRepository().findById(id).get());
			model.addAttribute("update", "");
			model.addAttribute("categories", categoryService.getRepository().findAll());
	        return "admin/image/ImageEdition";
        
		}
		
		return "/index";
        
    }
	
	//	Redirect to image deletion page
	@RequestMapping(value = { "/admin/images/{id}/delete" }, method = RequestMethod.GET)
    public String removeImage(@PathVariable Long id, Model model) {
		
		if (imageService.getRepository().existsById(id)) {
		
			model.addAttribute("image", imageService.getRepository().findById(id).get());
	        return "admin/image/ImageDeletion";
        
		}
		
		return "/index";
        
    }

	//	Update image from data base
	@RequestMapping(value = { "/admin/images/{id}" }, method = RequestMethod.POST)
    public String updateImage(@Valid @ModelAttribute("image") Image img, @PathVariable Long id, Model model) {
		
		Image old = imageService.getRepository().findById(id).get();
		
		if (img.getCategories().isEmpty() || StringUtils.isEmpty(img.getTitle())
				|| StringUtils.isEmpty(img.getDescription()) || StringUtils.isEmpty(img.getTags())) {
			
			img.setId(id);
			model.addAttribute("image", img);
			model.addAttribute("update", "");
			model.addAttribute("categories", categoryService.getRepository().findAll());
			model.addAttribute("creationError", "");
	
	        return "admin/image/ImageEdition";
        
		} else {
			
			try {
				
				img.setDate(old.getDate());
				img.setId(old.getId());
				img.setUrl(old.getUrl());
				
				imageService.getRepository().save(img);
			
			} catch (Exception e) {e.printStackTrace();}

			
			model.addAttribute("updateOK", "");
			model.addAttribute("images", imageService.getRepository().findAll());
			
			return "/admin/image/ImageList";
			
		}
        
    }
	
	//	Delete image from data base
	@RequestMapping(value = { "/admin/images/{id}/delete" }, method = RequestMethod.POST)
    public String deleteImage(@PathVariable Long id, Model model) {
					
		if (imageService.getRepository().existsById(id)) {
			
			Image old = imageService.getRepository().findById(id).get();
			imageService.getRepository().deleteById(old.getId());
			
			model.addAttribute("deleteOK", "");
			model.addAttribute("images", imageService.getRepository().findAll());
			
			return "/admin/image/ImageList";
			
		}
		
		return "/index";
        
    }
	
}
