package com.bestgo.adsplugin.utils;

import android.text.TextUtils;
import android.text.format.Time;
import android.util.Xml;

import com.bestgo.adsplugin.ads.entity.NewsEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class parses generic Atom feeds.
 *
 * <p>Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single entry (post) in the XML feed.
 *
 * <p>An example of an Atom feed can be found at:
 * http://en.wikipedia.org/w/index.php?title=Atom_(standard)&oldid=560239173#Example_of_an_Atom_1.0_feed
 */
public class FeedParser {

    // Constants indicting XML element names that we're interested in
    private static final int TAG_ID = 1;
    private static final int TAG_TITLE = 2;
    private static final int TAG_DESCRIPTION = 3;
    private static final int TAG_LINK = 4;
    private static final int TAG_IMAGE = 5;
    private static final int TAG_PUBDATE = 6;

    // We don't use XML namespaces
    private static final String ns = null;

    public List<NewsEntity> parse(InputStream in)
            throws XmlPullParserException, IOException, ParseException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<NewsEntity> readFeed(XmlPullParser parser)
            throws XmlPullParserException, IOException, ParseException {
        List<NewsEntity> entries = new ArrayList<NewsEntity>();

        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals("channel")) {
                break;
            }
        }
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private NewsEntity readEntry(XmlPullParser parser)
            throws XmlPullParserException, IOException, ParseException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String id = null;
        String title = null;
        String link = null;
        String pubDate = null;
        String description = null;
        String imgUrl = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("guid")){
                // Example: <id>urn:uuid:218AC159-7F68-4CC6-873F-22AE6017390D</id>
                id = readTag(parser, TAG_ID);
            } else if (name.equals("title")) {
                // Example: <title>Article title</title>
                title = readTag(parser, TAG_TITLE);
            } else if (name.equals("link")) {
                // Example: <link rel="alternate" type="text/html" href="http://example.com/article/1234"/>
                //
                // Multiple link types can be included. readAlternateLink() will only return
                // non-null when reading an "alternate"-type link. Ignore other responses.
                String tempLink = readTag(parser, TAG_LINK);
                if (tempLink != null) {
                    link = tempLink;
                }
            } else if (name.equals("pubDate")) {
                // Example: <published>2003-06-27T12:00:00Z</published>
                pubDate = readTag(parser, TAG_PUBDATE);
            } else if (name.equals("description")) {
                String html = readBasicTag(parser, "description");
                Document document = Jsoup.parse(html);
                Elements imgs = document.select("img");
                for (Element ele : imgs) {
                    String src = ele.attr("src");
                    if (!TextUtils.isEmpty(src)) {
                        imgUrl = src;
                        if (imgUrl.startsWith("//")) {
                            imgUrl = "http:" + imgUrl;
                        }
                        break;
                    }
                }
                description = document.select("font").text();
            } else {
                skip(parser);
            }
        }
        return new NewsEntity(id, title, link, description, pubDate, imgUrl);
    }

    private String readTag(XmlPullParser parser, int tagType)
            throws IOException, XmlPullParserException {
        String tag = null;
        String endTag = null;

        switch (tagType) {
            case TAG_ID:
                return readBasicTag(parser, "guid");
            case TAG_TITLE:
                return readBasicTag(parser, "title");
            case TAG_PUBDATE:
                return readBasicTag(parser, "pubDate");
            case TAG_LINK:
                return readBasicTag(parser, "link");
            default:
                throw new IllegalArgumentException("Unknown tag type: " + tagType);
        }
    }

    private String readBasicTag(XmlPullParser parser, String tag)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return result;
    }

    private String readAlternateLink(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String link = null;
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (relType.equals("alternate")) {
            link = parser.getAttributeValue(null, "href");
        }
        while (true) {
            if (parser.nextTag() == XmlPullParser.END_TAG) break;
            // Intentionally break; consumes any remaining sub-tags.
        }
        return link;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}