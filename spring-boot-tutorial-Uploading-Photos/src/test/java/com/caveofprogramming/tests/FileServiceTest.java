package com.caveofprogramming.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
	
	@Test
	public void testIsImageExtension() throws Exception {
		
		Method method = FileService.class.getDeclaredMethod("isImageExtension", String.class);
		method.setAccessible(true);
		
		assertTrue("png should be valid", (Boolean)method.invoke(fileService, "png"));
		assertTrue("PNG should be valid", (Boolean)method.invoke(fileService, "PNG"));
		assertTrue("jpg should be valid", (Boolean)method.invoke(fileService, "jpg"));
		assertTrue("jpeg should be valid", (Boolean)method.invoke(fileService, "jpeg"));
		assertTrue("gif should be valid", (Boolean)method.invoke(fileService, "gif"));
		assertTrue("GIF should be valid", (Boolean)method.invoke(fileService, "GIF"));
		assertFalse("doc should be invalid", (Boolean)method.invoke(fileService, "doc"));
		assertFalse("jpg3 should be invalid", (Boolean)method.invoke(fileService, "jpg3"));
		assertFalse("gi should be invalid", (Boolean)method.invoke(fileService, "gi"));
		
	}

}






































