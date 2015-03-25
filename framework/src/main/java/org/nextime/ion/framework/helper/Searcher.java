package org.nextime.ion.framework.helper;

import java.io.File;
import java.util.Vector;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.config.Config;

public class Searcher {

    public static synchronized Vector search(
            String keyWords,
            String indexName) {
        Vector result = new Vector();
        try {
            IndexReader reader
                    = IndexReader.open(
                            new File(Config.getInstance().getIndexRoot(), indexName));
            QueryParser parser = new QueryParser("content", new StopAnalyzer());
            Query query = parser.parse(keyWords);
            IndexSearcher searcher = new IndexSearcher(reader);
            Hits hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                Document doc = hits.doc(i);
                String id = doc.get("id");
                Publication p = Publication.getInstance(id);
                SearchResult sr = new SearchResult(p, Integer.parseInt(doc.get("version")), hits.score(i));
                result.add(sr);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

}
