LaukvikPDI
================================================================================

LaukvikPDI is a Java implementation of the Internet Mail Consortiums VCard and 
VCalendar. The API comes with an easy to use Java Swing application which 
demonstrates the features of the API.

Creating a new VCard

    /* Create an empty VCard */
    VCard card = new VCard();
    card.add(VCard.VERSION, "2.0");

    /* Set a few properties  */
    card.add(VCard.FULLNAME, "Sun Microsystems, Inc.");
    card.add(VCard.ORGANISATION, "Sun Microsystems, Inc.");
    card.add(VCard.ADDRESS, "4150 Network Circle;Santa Clara, CA 95054");
    card.add(VCard.TELEPHONE, "1-800-555-9", VCard.DOMESTIC);
    card.add(VCard.TELEPHONE, "1-650-960-1300", VCard.INTERNATIONAL);

    /* Exports the VCard to file */
    String filename = "Sun_Microsystems.vcf";
    String directory = System.getProperty("user.home");
    File file = new File(directory, filename);
    try {
        PDIManager.save(card, file);
    } catch (Exception e) {
        e.printStackTrace();
    }


Reading a VCard

    try {
        /* Read an existing VCard */
        File file = new File(System.getProperty("user.home"), "Sun_Microsystems.vcf");
        VCard vcard = (VCard) PDIManager.read(file);
        /* Display name */
        System.out.println(vcard.get(VCard.FULLNAME));
        /* List all phone numbers */
        System.out.println(vcard.list(VCard.TELEPHONE));
        /* List all domestic phone numbers */
        System.out.println(vcard.list(VCard.TELEPHONE, VCard.DOMESTIC));
        /* Print out all international numbers */
        System.out.println(vcard.list(VCard.INTERNATIONAL));
    } catch (Exception e) {
        e.printStackTrace();
    }

Creating a VCalendar

    /* Create empty calendar */
    VCalendar cal = new VCalendar();
    /* Adding a simple todo item*/
    cal.addTodo("Remember to create JavaDoc", VTodo.PRIORITY_1, new Date(), "No comments");
    /* Creating a single event in schedule */
    GregorianCalendar from = new GregorianCalendar();
    GregorianCalendar to = new GregorianCalendar();
    to.add(GregorianCalendar.HOUR_OF_DAY, 3);
    cal.addEvent("Visit JavaZone", from.getTime(), to.getTime(), "");
    try {
        /* Exporting calendar to standard ics file */
        File file = new File(System.getProperty("user.home"), "mycalendar.ics");
        PDIManager.save(cal, file);
    } catch (Exception e) {
        e.printStackTrace();
    }