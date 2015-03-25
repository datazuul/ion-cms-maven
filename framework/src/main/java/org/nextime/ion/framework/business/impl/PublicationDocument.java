package org.nextime.ion.framework.business.impl;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.nextime.ion.framework.business.Publication;
import org.nextime.ion.framework.helper.Viewer;
import org.nextime.ion.framework.logger.Logger;

public class PublicationDocument {

    public PublicationDocument() {
    }

    public static Document getDocument(Publication p, int version, String indexName) {
        try {
            Document doc = new Document();

            // ajoute le champ id
            doc.add(Field.Keyword("id", p.getId()));

            // ajoute le champ version
            doc.add(Field.Keyword("version", version + ""));

            // cherche le style correspondant pour l'indexation
            Style style = ((TypePublicationImpl) p.getType()).getStyleSheet("indexation_" + indexName);

            // recupere le contenu de la publication
            String content = "";

            if (style != null) {
                content = new String(Viewer.getView(p, version, "indexation_" + indexName));
            } else {
                Logger.getInstance().log("Pas de style d'indexation " + indexName + " pour le type " + p.getType().getId() + ".", PublicationDocument.class);
            }

            // ajoute le contenu
            doc.add(Field.Text("content", content));

            return doc;
        } catch (Exception e) {
            Logger.getInstance().error(
                    "Impossible d'indexer la publication " + p.getId() + ".",
                    PublicationDocument.class,
                    e);
            return null;
        }
    }
}
