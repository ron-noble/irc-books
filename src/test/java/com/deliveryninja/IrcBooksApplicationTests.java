package com.deliveryninja;

import org.junit.Test;

import java.io.File;

public class IrcBooksApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testUnrar(){
		final File destination = new File("C:\\local\\ebooks\\temp");

		if(destination.isDirectory()){
		    destination.delete();
		    destination.mkdir();
        };

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test.zip").getFile());

		ZipUtils.unzip(file.getPath(), destination.getPath());

        File[] files = destination.listFiles();

        System.out.println(files[0].getAbsoluteFile());
    }

    @Test
    public void testOutput(){
	    ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("SearchBot_results_for_ Alice in wonderland.txt").getFile());
        IrcBooksApplication booksApplication = new IrcBooksApplication();
	    booksApplication.readFile(file.getAbsolutePath());
    }
}
