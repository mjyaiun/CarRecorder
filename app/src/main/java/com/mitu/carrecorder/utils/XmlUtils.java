package com.mitu.carrecorder.utils;

import com.mitu.carrecorder.entiy.FileEntity;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class XmlUtils {

	
	private static final String HEADER = "http://192.168.1.254/?custom=1";
	
	
	public String getState(String cmd,String par)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			//拼接url请求地址
			String urls = HEADER +"&cmd="+cmd+"&par="+par;
			SAXParser parser = factory.newSAXParser();
			ModeChagerHandler handler = new ModeChagerHandler();
			parser.parse(getInputStream(urls), handler);
//			List<FileEntity> ad = handler.getLists();
			String valueStr = handler.getLists();
			return handler.getLists();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解析XML
	 * @author Administrator
	 *
	 */
	class ModeChagerHandler extends DefaultHandler {

		private String stateString;

		private StringBuilder builder;

		private String getLists() {
			return stateString;
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			stateString = "";
			builder = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			/**
			 * 获取根节点
			 */
			if (localName.equals("Function")) {
				stateString ="";
			}
			builder.setLength(0);
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			builder.append(ch, start, length); // 将读取的字符数组追加到builder中
		}

		/**
		 * 遍历xml节点数据
		 */
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equals("Status")) {
				stateString = builder.toString();
			}
		}
	}
	/**
	 * 获取列表 type值传cmd的值
	 * @param type
	 * @return
	 */
	public ArrayList<FileEntity> getListFiles(String type)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			//拼接url请求地址
			String urls = HEADER +"&cmd="+type;
			SAXParser parser = factory.newSAXParser();
			HomeVideoHandler handler = new HomeVideoHandler();
			parser.parse(getInputStream(urls), handler);
			//List<FileEntity> ad = handler.getLists();
			return handler.getLists();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public InputStream getInputStream(String ur) {
		try {
			URL url = new URL(ur);
			// 打开连接,。会返回HttpsURLConnection 对象
			HttpURLConnection httpsURLConnection = (HttpURLConnection) url
					.openConnection();
			// 现在是使用HTTP协议请求服务器的数据
			// 所以要设置请求方式
			httpsURLConnection.setRequestMethod("GET");
			// 设置 超时时间 为5秒
			httpsURLConnection.setConnectTimeout(5 * 1000);
			// 通过输入流获取网络xml内容
			// 取得输入流
			return httpsURLConnection.getInputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 解析XML
	 * @author Administrator
	 *
	 */
	class HomeVideoHandler extends DefaultHandler {

		private ArrayList<FileEntity> lists;

		private FileEntity ad;

		private StringBuilder builder;

		private ArrayList<FileEntity> getLists() {
			return lists;
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			lists = new ArrayList<FileEntity>();
			builder = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			/**
			 * 获取根节点
			 */
			if (localName.equals("File")) {
				ad = new FileEntity();
				System.out.println("File");
			}
			builder.setLength(0);
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			builder.append(ch, start, length); // 将读取的字符数组追加到builder中
		}

		/**
		 * 遍历xml节点数据
		 */
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equals("NAME")) {
				ad.setName(builder.toString());
				System.out.println("NAME:"+builder.toString());
			}else if (localName.equals("FPATH")) {
				ad.setFPath(builder.toString());
				System.out.println("FPATH:"+builder.toString());
			} else if (localName.equals("SIZE")) {
				ad.setSize(builder.toString());
			} else if (localName.equals("TIMECODE")) {
				ad.setTimeCode(builder.toString());
			} else if (localName.equals("TIME")) {
				ad.setTime(builder.toString());
			} else if (localName.equals("ATTR")) {
				ad.setAttr(builder.toString());
				System.out.println("ATTR:"+builder.toString());
			} 
			else if (localName.equals("File")) {
				//遍历完成一个file节点保存一组数据
				lists.add(ad);
			}
		}
	}
}
