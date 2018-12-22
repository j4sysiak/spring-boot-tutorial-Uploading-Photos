package com.caveofprogramming.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.caveofprogramming.service.FileService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:test.properties")
//@Transactional
public class FileServiceTest {
	
	@Autowired
	FileService fileService;
	
	@Test
	public void testGetExtension() throws Exception {
		
		//accessing private method from other class (FileService) this class FileServiceTest
		Method method = FileService.class.getDeclaredMethod("getFileExtensions", String.class);
		
		method.setAccessible(true);
		
		assertEquals("should be png", "png", (String)method.invoke(fileService, "test.png"));
		assertEquals("should be doc", "doc", (String)method.invoke(fileService, "s.doc"));
		assertEquals("should be jpeg", "jpeg", (String)method.invoke(fileService, "file.jpeg"));
		assertNull("should be null", (String)method.invoke(fileService, "xyz"));
	}

}






































