package eu.bitwalker.useragentutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;


public class BenchmarkTest {
	
	/**
	 * 倍率测试
	 */
	@Test
	public void doublingTest() {
		File inputFile = new File("/home/zxchaos/tempFiles/0830_ua");
		if (!inputFile.exists()) {
			System.out.println("File not exists!");
		}
		int init = 1000;
		ArrayList<String> uaList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile));) {
			String line = null;
			int count = 0;
			while ((line = br.readLine()) != null) {
				if (count < init) {
					uaList.add(line);
					count++;
				} else {
					testElapse(uaList, count);
					count = 0;
					init *= 2;
					uaList = new ArrayList<>(init);
				}
			}
			System.out.print("文件读取完毕.");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}

	}

	/**
	 * 运行时间测试
	 * @param uaList
	 * @param count
	 */
	private void testElapse(ArrayList<String> uaList, int count) {
		long start = System.currentTimeMillis();
		for (String l : uaList) {
			TestK k = new TestK();
			UserAgent ua = new UserAgent(l);
			Browser b = ua.getBrowser();
			Browser group = ua.getBrowser().getGroup();
			k.setBrowser(group.name());
			if (Browser.IE.equals(group)) {
				Version v = b.getVersion(l);
				if (v != null) {
					k.setBrowserVersion(v.getMajorVersion());
				}
			}
			k.setClientOs(ua.getOperatingSystem().getGroup().name());// TODO clientOsVersion
			k.setDeviceType(ua.getOperatingSystem().getDeviceType().name());
		}
		System.out.println("ua数据量:" + count + ", 运行时间:" + (System.currentTimeMillis() - start));
	}
	
	
	private class TestK{
		private String browser;
		private String browserVersion;
		private String clientOs;
		private String deviceType;
		public String getBrowser() {
			return browser;
		}
		public void setBrowser(String browser) {
			this.browser = browser;
		}
		
		public String getBrowserVersion() {
			return browserVersion;
		}
		public void setBrowserVersion(String browserVersion) {
			this.browserVersion = browserVersion;
		}
		public String getClientOs() {
			return clientOs;
		}
		public void setClientOs(String clientOs) {
			this.clientOs = clientOs;
		}
		public String getDeviceType() {
			return deviceType;
		}
		public void setDeviceType(String deviceType) {
			this.deviceType = deviceType;
		}
	}
}
