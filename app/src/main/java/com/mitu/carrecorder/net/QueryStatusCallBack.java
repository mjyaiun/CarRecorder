package com.mitu.carrecorder.net;

import com.guotiny.httputils.callback.Callback;
import com.mitu.carrecorder.entiy.Command;

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
public abstract class QueryStatusCallBack extends Callback<ArrayList<Command>> {


    @Override
    public ArrayList<Command> parseNetworkResponse(Response response, int i) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        CurrentStatusHandler handler = new CurrentStatusHandler();
        parser.parse(response.body().byteStream(), handler);
        //List<FileEntity> ad = handler.getLists();
        return handler.getLists();
    }

    /**
     * 解析XML
     * @author Administrator
     *
     */
    class CurrentStatusHandler extends DefaultHandler {

        private ArrayList<Command> lists;

        private Command ad;

        private StringBuilder builder;

        private ArrayList<Command> getLists() {
            return lists;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            lists = new ArrayList<Command>();
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
                System.out.println("Function");
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
            if (localName.equals("Cmd")) {
                ad = new Command();
                ad.setNum(builder.toString());
                System.out.println("Cmd:"+builder.toString());
            }else if (localName.equals("Status")) {
                ad.setState(builder.toString());
                System.out.println("Status:"+builder.toString());
                lists.add(ad);
            } else if (localName.equals("Function")) {
                //遍历完成一个file节点保存一组数据

            }
        }
    }

}
