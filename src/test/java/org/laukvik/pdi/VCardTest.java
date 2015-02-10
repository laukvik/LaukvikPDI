package org.laukvik.pdi;

import java.io.File;
import java.io.IOException;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class VCardTest {

    File file = null;

    public VCardTest() {
    }

    @Before
    public void before() throws IOException {
        file = File.createTempFile("Sun_Microsystems", "vcf");
    }

    @Test
    public void shouldWrite() {
        try {
            writeVCard();
        }
        catch (Exception ex) {
            fail("Not able to write");
        }
    }

    @Test
    public void shouldRead() {
        try {
            writeVCard();
            readVCard();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("Not able to read");
        }
    }

    public static File getResource(String filename) {
        ClassLoader classLoader = VCardTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    public void writeVCard() throws Exception {
        /* Create an empty VCard */
        VCard card = new VCard();
        card.add(VCard.VERSION, "2.0");

        /* Set a few properties  */
        card.add(VCard.FULLNAME, "Sun Microsystems, Inc.");
        card.add(VCard.ORGANISATION, "Sun Microsystems, Inc.");
        card.add(VCard.ADDRESS, "4150 Network Circle;Santa Clara, CA 95054");
        card.add(VCard.TELEPHONE, "1-800-555-9", VCard.DOMESTIC);
        card.add(VCard.TELEPHONE, "1-650-960-1300", VCard.INTERNATIONAL);

        PDIManager.save(card, file);

    }

    public void readVCard() throws Exception {
        /* Read an existing VCard */
        VCard vcard = (VCard) PDIManager.read(file);
        /* Display name */
        System.out.println(vcard.get(VCard.FULLNAME));
        /* List all phone numbers */
        System.out.println(vcard.list(VCard.TELEPHONE));
        /* List all domestic phone numbers */
        System.out.println(vcard.list(VCard.TELEPHONE, VCard.DOMESTIC));
        /* Print out all international numbers */
        System.out.println(vcard.list(VCard.INTERNATIONAL));

    }

}
