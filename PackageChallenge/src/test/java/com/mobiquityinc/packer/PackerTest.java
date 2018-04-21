package com.mobiquityinc.packer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.info.PackageInfo;
import com.mobiquityinc.packer.parser.PackageParser;
import com.mobiquityinc.packer.processor.PackageProcessor;

import junit.framework.Assert;

public class PackerTest {

	@Mock
	private PackageParser packageParser;

	@Mock
	private PackageProcessor packageProcessor;

	@InjectMocks
	private Packer packer;

	@BeforeClass
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expectedExceptions = APIException.class)
	public void testPackAPIException() {
		Packer.pack(null);
	}

	@Test(expectedExceptions = APIException.class)
	public void testPackAPIExceptionFileNotExists() {
		File file = new File("src/test/resourcesWrong/packer.txt");
		Packer.pack(file.getAbsolutePath());
	}

	@Test
	public void testPackSuccess() throws Exception {
		File file = new File("src/test/resources/packer.txt");

		when(packageParser.parsePackage(Matchers.anyString()))
				.thenReturn(Optional.of(new PackageInfo(0, new ArrayList<>())));

		when(packageProcessor.processPackage(Matchers.<Optional<PackageInfo>>any())).thenReturn("1").thenReturn("2,7").thenReturn("3")
				.thenReturn("-");

		Method method = Packer.class.getDeclaredMethod("process", String.class);
		method.setAccessible(true);
		
		
		String response = (String) method.invoke(packer, file.getAbsolutePath());

		Assert.assertEquals(response, "1" + System.lineSeparator() + "2,7" + System.lineSeparator() + "3"
				+ System.lineSeparator() + "-" + System.lineSeparator());
		verify(packageParser, times(4)).parsePackage(Matchers.anyString());
		verify(packageProcessor, times(4)).processPackage(Matchers.<Optional<PackageInfo>>any());

	}
}
