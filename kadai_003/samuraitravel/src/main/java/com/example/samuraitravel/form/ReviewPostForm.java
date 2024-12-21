package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewPostForm {
	
    @NotNull
    private Integer star;  
    
    @NotBlank
    private String comment;    
   
}
	

