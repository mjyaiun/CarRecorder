package com.mitu.carrecorder.net;

import com.guotiny.httputils.callback.Callback;
import com.mitu.carrecorder.entiy.FileEntity;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Response;

/**
 * 说明：
 * 2016/6/24 0024
 */
public abstract class FileListCallBack extends Callback<ArrayList<FileEntity>> {


    @Override
    public ArrayList<FileEntity> parseNetworkResponse(Response response, int i) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        FileListHandler handler = new FileListHandler();
        parser.parse(response.body().byteStream(), handler);
        //List<FileEntity> ad = handler.getLists();
        return handler.getLists();
    }

    /**
     * 解析XML
     * @author Administrator
     *
     */
    class FileListHandler extends DefaultHandler {

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
