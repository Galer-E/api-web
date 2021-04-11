package com.galere.pictures.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.galere.pictures.entities.Image;
import com.galere.pictures.services.IImageService;

@Controller
public class ImageController {
	
    @Autowired
    private IImageService imageService;
	
	@RequestMapping(value = { "/images" }, method = RequestMethod.GET)
    public String listClient(Model model) {
		
		List<Image> images = imageService.getRepository().findAll();
		
		model.addAttribute("images", images);
        return "admin/image/ImageList";
        
    }
	
}
