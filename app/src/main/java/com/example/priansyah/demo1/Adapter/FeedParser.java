package com.example.priansyah.demo1.Adapter;

import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class FeedParser {
    private static final int TAG_ID = 1;
    private static final int TAG_LINK = 4;
    private static final int TAG_PUBLISHED = 3;
    private static final int TAG_TITLE = 2;
    private static final String ns;

    public class Entry {
        public final String id;
        public final String link;
        public final long published;
        public final String title;

        Entry(String id, String title, String link, long published) {
            this.id = id;
            this.title = title;
            this.link = link;
            this.published = published;
        }

        public String getTitle() {
            return this.title;
        }

        public String getLink() {
            return this.link;
        }
    }

    static {
        ns = null;
    }

    public List<Entry> parse(InputStream in) throws XmlPullParserException, IOException, ParseException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
            parser.setInput(in, null);
            parser.nextTag();
            List<Entry> readFeed = readFeed(parser);
            return readFeed;
        } finally {
            in.close();
        }
    }

    private List<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        List<Entry> entries = new ArrayList();
        parser.require(TAG_TITLE, ns, "channel");
        while (parser.next() != TAG_TITLE) {
            if (parser.getEventType() == TAG_TITLE) {
                if (parser.getName().equals("item")) {
                    entries.add(readEntry(parser));
                } else {
                    skip(parser);
                }
            }
        }
        return entries;
    }

    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        parser.require(TAG_TITLE, ns, "item");
        String id = null;
        String title = null;
        String link = null;
        while (parser.next() != TAG_PUBLISHED) {
            if (parser.getEventType() == TAG_TITLE) {
                String name = parser.getName();
                if (name.equals("id")) {
                    id = readTag(parser, TAG_ID);
                } else if (name.equals("title")) {
                    title = readTag(parser, TAG_TITLE);
                } else if (name.equals("link")) {
                    String tempLink = readTag(parser, TAG_LINK);
                    if (tempLink != null) {
                        link = tempLink;
                    }
                } else if (!name.equals("published")) {
                    skip(parser);
                }
            }
        }
        return new Entry(id, title, link, 0);
    }

    private String readTag(XmlPullParser parser, int tagType) throws IOException, XmlPullParserException {
        switch (tagType) {
            case TAG_ID /*1*/:
                return readBasicTag(parser, "id");
            case TAG_TITLE /*2*/:
                return readBasicTag(parser, "title");
            case TAG_PUBLISHED /*3*/:
                return readBasicTag(parser, "published");
            case TAG_LINK /*4*/:
                return readAlternateLink(parser);
            default:
                throw new IllegalArgumentException("Unknown tag type: " + tagType);
        }
    }

    private String readBasicTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(TAG_TITLE, ns, tag);
        String result = readText(parser);
        parser.require(TAG_PUBLISHED, ns, tag);
        return result;
    }

    private String readAlternateLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = null;
        parser.require(TAG_TITLE, ns, "link");
        if (parser.getAttributeValue(null, "rel").equals("alternate")) {
            link = parser.getAttributeValue(null, "href");
        }
        do {
        } while (parser.nextTag() != TAG_PUBLISHED);
        return link;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        if (parser.next() != TAG_LINK) {
            return null;
        }
        String result = parser.getText();
        parser.nextTag();
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != TAG_TITLE) {
            throw new IllegalStateException();
        }
        int depth = TAG_ID;
        while (depth != 0) {
            switch (parser.next()) {
                case TAG_TITLE /*2*/:
                    depth += TAG_ID;
                    break;
                case TAG_PUBLISHED /*3*/:
                    depth--;
                    break;
                default:
                    break;
            }
        }
    }
}
