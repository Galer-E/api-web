package com.galere.pictures.controllers;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
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

/**
 * <b>
 * 	Controller offrant différentes routes pour gérer les images en base de données et sur la machine.
 * </b>
 * 
 * @see Image
 * @see IImageService
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Controller
public class ImageController {
	
	/**
	 * <b> Implémentation du service IImageService. </b>
	 */
    @Autowired
    private IImageService imageService;
    
    /**
	 * <b> Implémentation du service ICategoryService. </b>
	 */
    @Autowired
    private ICategoryService categoryService;

    /**
   	 * <b> Instance d'un objet d'une librairie permettant d'effectuer une analyse d'image, via une api en ligne. </b>
   	 */
    private V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
    	    .withCallCredentials(new ClarifaiCallCredentials("59a0e133b42a4af78321abb08557bd84"));
    
    /**
     * <b> Lister ou rechercher des images (par mots clés). </b>
     * 
     * @param tags Mots clés
     * @param model Attributs destinés à la page web.
     * @return La liste récupérée, vers la page de listing admin.
     */
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
	
    /**
     * <b> Redirection vers la page de création d'une image. </b>
     * 
     * <p> On envoie sur cette route une image, on accède ensuite à la page de renseignement des informations de l'image. </p>
     * <p> Cette méthode effectuera une analyse en ligne de l'image fournit pour obtenir une première analyse du contenu de l'image. </p>
     * 
     * @param multipartImage Image à ajouter.
     * @param model Attributs destinés à la page web.
     * @return Une nouvelle image vierge.
     */
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
	
	/**
	 * <b> Récupérer une image dans une page web vierge. </b>
	 * 
	 * @param id Id de l'image.
	 * @param model Attributs destinés à la page web.
	 * @return L'image pour la page web.
	 * @throws Exception En cas d'erreur.
	 */
	@RequestMapping(value = "/shared/show/{id}", method = RequestMethod.GET)
    public String showImage(@PathVariable Long id, Model model) throws Exception {
        
		model.addAttribute("image", imageService.getRepository().findById(id).get());
		
		return "shared/ShowImage";
		
    }
	
	/**
	 * <b> Télécharger une image. </b>
	 * 
	 * @param id Id de l'image.
	 * @param width Largeur de l'image.
	 * @param height Hauteur de l'image.
	 * @return L'image en téléchargement.
	 * @throws Exception En cas d'erreur.
	 */
	@RequestMapping(value = "/shared/image/{id}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE }, method = RequestMethod.GET)
    public ResponseEntity getImage(@PathVariable Long id, @RequestParam(defaultValue = "500") Integer width, 
    							@RequestParam(defaultValue = "500") Integer height) throws Exception {
        
		FileSystemResource initial = imageService.findImage(id);
		FileSystemResource img = imageService.getResizedImage(initial, width, height);
		
		return ResponseEntity.ok()
				.contentType(initial.getFilename().contains(".png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + initial.getFilename() + "\"")
				.body(img);
		
    }
	
	/**
	 * <b> Télécharger un groupe image. </b>
	 * 
	 * @param id Id de l'image.
	 * @return L'image en téléchargement.
	 * @throws Exception En cas d'erreur.
	 */
	@RequestMapping(value = "/shared/image/group/{id}", produces="application/zip", method = RequestMethod.GET)
    public void zipImageGroupSize(HttpServletResponse response, @PathVariable Long id) throws Exception {
        
		response.setStatus(HttpServletResponse.SC_OK);
	    response.addHeader("Content-Disposition", "attachment; filename=\"" + imageService.getRepository().findById(id).get().getTitle() + ".zip\"");

	    ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        List<File> files = imageService.getGroupSizeOf(id);

        for (File file : files) {
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);

            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();
        }    

        zipOutputStream.close();
		
    }
	
	
	/**
     * <b> Redirection vers la page d'édition d'une image. </b>
     * 
     * @param id Id de l'image recherchée.
     * @param model Attributs destinés à la page web.
     * @return L'image existante.
     */
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
	
	/**
	 * <b> Redirection vers la page de suppression d'une image. </b>
	 * 
	 * <p> Cette page sert de validation à la suppression. </p>
	 * 
	 * @param id Id de l'image.
	 * @param model Attributs destinés à la page web.
	 * @return La page de validation de la suppression.
	 */
	@RequestMapping(value = { "/admin/images/{id}/delete" }, method = RequestMethod.GET)
    public String removeImage(@PathVariable Long id, Model model) {
		
		if (imageService.getRepository().existsById(id)) {
		
			model.addAttribute("image", imageService.getRepository().findById(id).get());
	        return "admin/image/ImageDeletion";
        
		}
		
		return "/index";
        
    }

	/**
	 * <b> Mise à jours des informations relatives à une image. </b>
	 * 
	 * @param img Image modifiée.
	 * @param id Id de l'image.
	 * @param model Attributs destinés à la page web.
	 * @return La mise à jours puis la redirection vers la page de listing.
	 */
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
	
	/**
	 * <b> Suppression d'une image en base de données et sur la machine. </b>
	 * 
	 * @param id Id de l'image.
	 * @param model Attributs destinés à la page web.
	 * @return La suppression, puis redirection vers la page de listing admin.
	 */
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
