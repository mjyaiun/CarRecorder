package com.mitu.carrecorder.net;


import com.guotiny.httputils.callback.Callback;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Response;

/**
 * 说明：
 * 2016/6/24 0024
 */
public abstract class CommandCallBack extends Callback<String> {

    @Override
    public String parseNetworkResponse(Response response, int i) throws Exception {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        ModeChagerHandler handler = new ModeChagerHandler();
        parser.parse(response.body().byteStream(), handler);
//			List<FileEntity> ad = handler.getLists();
        //String valueStr = handler.getLists();
        return handler.getLists();
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

}
