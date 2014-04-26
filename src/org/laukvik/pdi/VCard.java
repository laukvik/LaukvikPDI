/*
 * LaukvikPDI is the java implementation of the Internet Mail 
 * Consortium's vCard and vCalendar.
 * 
 * Copyright (C) 2005  Morten Laukvik
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, 
 * Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.laukvik.pdi;

/**
 * A <code>VCard</code> is basically the same as a business card with the exception
 * of that a <code>VCard<code> can store a lot more information.
 * 
 * 
 * @version  $Revision: 1.3 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @link		http://www.ietf.org/rfc/rfc2426.txt
 * @since	1.5
 */
public class VCard extends VProperty{
	
	/* Main types */
	/** Constant used to identify VCard objects */
	public final static String VCARD		    = "VCARD";
	
	/* Predefined types */
	/** Constant used to specify the proile. If the PROFILE type is present, then 
	 * its Property value MUST be VCARD */
	public final static String PROFILE		= "PROFILE";
	/** Constant used to specify the presentation text associated with the source 
	 * for the VCard, as specified in the SOURCE type. */
	public final static String OBJECTNAME		= "NAME";
	public final static String [] PREDEFINED_TYPES = {PROFILE,OBJECTNAME};
	
	
	/* Identification types */
	/** Constant used to identify the full name of a person eg. Morten Laukvik */
	public final static String FULLNAME		= "FN";
	/** Constant used to identify the full name of a person with first,middle and 
	 * last name an a non-human readable way */
	public final static String NAME			= "N";
	/** Constant used to identify the nickname of a person */
	public final static String NICKNAME		= "NICKNAME";
	/** Constant used to identify a picture of a person */
	public final static String PHOTO			= "PHOTO";
	/** Constant used to identify the birthday of a person */
	public final static String BIRTHDAY		= "BDAY";
	/** Constants used in the VCard profile to capture information associated with 
	 * the identification and naming of the person or resource associated with the 
	 * VCard. */
	public final static String[] IDENTIFICATION_TYPES = { FULLNAME, NAME,
			NICKNAME, PHOTO, BIRTHDAY };
	
	
	/* Delivery address types */
	/** Constant used to identify the delivery address for a person */
	public final static String ADDRESS		= "ADR";
	/** Constant used to specify the formatted text corresponding to delivery
	 * address of the object the VCard represents. */
	public final static String LABEL			= "LABEL";
	/** Constants for addressing types */
	public final static String [] DELIVERY_TYPES = {ADDRESS,LABEL};
	
	
	/* Telecommunication addressing types */
	/** Constant used to identify the telephone number for a person */
	public final static String TELEPHONE		= "TEL";
	/** Constant used to identify the email address for a person */
	public final static String EMAIL			= "EMAIL";
	/** Constant used to specify the type of electronic mail software that is used 
	 * by the individual associated with the VCard. */
	public final static String MAILER			= "MAILER";
	/** These types are concerned with information associated with the 
	 * telecommunications addressing of the object the VCard represents. */
	public final static String [] TELECOMMUNICATION_TYPES = {TELEPHONE,EMAIL,MAILER};
	
	
	/* Geographical types */
	/** Constant used to specify information related to the time zone of the object 
	 * the VCard represents. */
	public final static String TIMEZONE		= "TZ";
	/** Constant used to specify information related to the global
   positioning of the object the vCard represents. */
	public final static String GEOGRAPHY		= "GEO";
	/** Constants that are concerned with information associated with geographical 
	 * positions or regions associated with the object the VCard represents. */
	public final static String [] GEOGRAPHICAL_TYPES = {TIMEZONE,GEOGRAPHY};
	
	
	/* Organisational types */
	/** Constant used to identify the working title for a person */
	public final static String TITLE			= "TITLE";
	/** Constant used to identify the working role for a person */
	public final static String ROLE			= "ROLE";
	/** Constant used to describe the logo for a person */
	public final static String LOGO			= "LOGO";
	/** Constant used to specify information about another person who will act on 
	 * behalf of the individual or resource associated with the VCard. */
	public final static String AGENT			= "AGENT";
	/** Constant used to identify the company or organisation a person works for */
	public final static String ORGANISATION	= "ORG";
	/** These types are concerned with information associated with characteristics 
	 * of the organization or organizational units of the object the VCard 
	 * represents. */
	public final static String[] ORGANISATIONAL_TYPES = { TITLE, ROLE, LOGO,
			AGENT, ORGANISATION };
	
	
	/* Explanatory types */
	/** Constant used to specify application category information about the VCard.*/
	public final static String CATEGORIES	    = "CATEGORIES";
	/** Constants used to specify supplemental information or a comment that is 
	 * associated with the VCard. */
	public final static String NOTE			= "NOTE";
	/** Constant used to specify the identifier for the product that created the 
	 * VCard object */
	public final static String PRODID			= "PRODID";
	/** Constant used to specify revision information about the current VCard */
	public final static String REV			= "REV";
	/** Constant used to specify the family name or given name text to be used for 
	 * national-language-specific sorting of the FN and N types*/
	public final static String SORT_STRING		= "SORT-STRING";
	/** Constant used to specify a digital sound content information that annotates 
	 * some aspect of the VCard. By default this type is used to specify the proper 
	 * pronunciation of the name type value of the VCard. */
	public final static String SOUND			= "SOUND";
	/** Constant used to specify a value that represents a globally unique 
	 * identifier corresponding to the individual or resource associated with the 
	 * VCard */
	public final static String UID			= "UID";
	/** Constant used to specify a uniform resource locator associated with the 
	 * object that the VCard refers to */
	public final static String URL			= "URL";
	/** Constant used to specify the version of the vCard specification used to 
	 * format this VCard */
	public final static String VERSION		= "VERSION";
	/** Constants that are concerned with additional explanations, such as that
	 * related to informational notes or revisions specific to the VCard. */
	public final static String[] EXPLANATORY_TYPES = { CATEGORIES, NOTE,
			PRODID, REV, SORT_STRING, SOUND, UID, URL, VERSION };
	
	
	/* Security types */
	/** Constant used to specify the access classification for a VCard object. */
	public final static String CLASS			= "CLASS";
	/** Constant used to specify a public key or authentication certificate 
	 * associated with the object that the VCard represents.*/
	public final static String KEY			= "KEY";
	/** Constants that are concerned with the security of communication pathways
	 * or access to the VCard. */
	public final static String [] SECURITY_TYPES = {CLASS,KEY};

	
	public final static VParameter INTERNATIONAL = new VParameter("type","intl");
	public final static VParameter DOMESTIC = new VParameter("type","dom");
	
	public final static VParameter HOME = new VParameter("type","home");
	public final static VParameter WORK = new VParameter("type","work");
	
	public final static VParameter POSTAL = new VParameter("type","postal");
	public final static VParameter PARCEL = new VParameter("type","parcel");

	
	/**
	 * Creates a new <code>VCard</code> instance 
	 * 
	 */
	public VCard() {
		super(null);
		setValue( VCARD );
		setIsGroup(true);
	}

	/**
	 * Creates a new <code>VCard</code> instance with the specified 
	 * <code>parent</code>
	 * 
	 * @param parent the parent Property to set
	 */
	public VCard(VProperty parent) {
		super(parent);
		setValue( VCARD );
		setIsGroup(true);
	}

}