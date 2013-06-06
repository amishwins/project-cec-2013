package cec.view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Auxiliary Class used in the Presentation Layer that implements methods to
 * validate user's input on Email fields and Folder names.
 * */

public class Validator {

	Pattern pattern;
	Matcher matcher;

	/**
	 * Method to verify if the email addresses supplied by the user are valid.
	 * This takes into consideration that the user can supply both to and cc, or
	 * only to, or only cc
	 * 
	 * @param to
	 *            the email address in the to field
	 * @param cc
	 *            the email address in the cc field
	 * @return boolean returns true if the email address are valid
	 */
	public boolean isValidSendees(String to, String cc) {
		if (to.isEmpty() && cc.isEmpty())
			return false;

		if (!to.isEmpty() && !cc.isEmpty())
			return isValidTo(to) && isValidCC(cc);

		if (!to.isEmpty())
			return isValidTo(to);

		if (!cc.isEmpty())
			return isValidCC(cc);

		return true;
	}

	private boolean isValidTo(String emailString) {
		String[] emails = emailString.split(";");

		pattern = Pattern.compile("(.+)@(.+)(\\.)(.+)");

		for (String email : emails) {
			matcher = pattern.matcher(email);
			if (matcher.find() == false)
				return false;
		}

		return true;
	}

	private boolean isValidCC(String emailString) {
		return isValidTo(emailString);
	}

	/**
	 * Verify that the user entered folder name does not contain any characters
	 * which prevents it from being saved in the windows file system
	 * 
	 * @param folderName
	 *            the name which was supplied by the user
	 * @return boolean returns true if the string does not contain any special
	 *         characters
	 */
	public boolean isValidFolderName(String folderName) {
		pattern = Pattern.compile("^[a-zA-Z0-9_\\s]+$");
		matcher = pattern.matcher(folderName);
		if (matcher.find() == false) {
			return false;
		}
		return true;
	}

	/**
	 * Verify that what the user is searching for is not empty and does not
	 * contain any special character except the symbol "@"
	 * 
	 * @param folderName
	 *            the name which was supplied by the user
	 * @return boolean returns true if the string does not contain any special
	 *         characters
	 */
	public boolean isValidSearched(String stringToFind) {

		String modifiedString = stringToFind.toUpperCase();

		String toRemove = "[[^A-Z]&&[^0-9]&&[^@._]]";
		pattern = Pattern.compile(toRemove);
		matcher = pattern.matcher(modifiedString);

		if (matcher.find())
			modifiedString = matcher.replaceAll(" ").trim();
		return modifiedString.length() > 0;

	}

	public boolean isValidDates(String startDate, String endDate) {
		if (startDate.isEmpty() || endDate.isEmpty())
			return false;

		if (!startDate.isEmpty() && !endDate.isEmpty())
			return isValidDate(startDate) && isValidDate(endDate);

		return false;
	}

	private boolean isValidDate(String date) {
		pattern = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})$");
		matcher = pattern.matcher(date);
		if (matcher.find()) {
			int year = Integer.parseInt(matcher.group(1));
			int month = Integer.parseInt(matcher.group(2));
			int day = Integer.parseInt(matcher.group(3));
			if (year < 1900 || month > 12 || day > 31) {
				return false;
			} else {
				return true;
			}

		}
		return false;
	}

	public boolean hasNotPassedDates(String startDate, String endDate) {
		if (!startDate.isEmpty() && !endDate.isEmpty()) {
			return hasNotPassed(startDate) && hasNotPassed(endDate);
		}
		return false;
	}

	public boolean hasNotPassedDates(String startDate, String startTime,
			String endDate, String endTime) {
		if (!startDate.isEmpty() && !endDate.isEmpty()) {
			return hasNotPassed(startDate,startTime) && hasNotPassed(endDate,endTime);
		}
		return false;
	}

	private boolean hasNotPassed(String date, String time) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		
		Calendar today = Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		int month = (today.get(Calendar.MONTH))+1;
		int day = today.get(Calendar.DAY_OF_MONTH);
		int hour = today.get(Calendar.HOUR_OF_DAY);
		int minute = today.get(Calendar.MINUTE);
		
		String AM_PM;
		
	    if(hour<12){
	    	AM_PM="AM";
	    }else{
	    	AM_PM="PM";
	    }
	    
	    if(hour>12){
	    	hour = hour -12;
	    }
  
		Date currentDate = null;
		Date dateToBeTested = null;
		try {
			dateToBeTested = formatter.parse(date + " " + time);
			currentDate = formatter.parse(year + "-" + month + "-" + day + " "
					+ hour + ":" + minute + " " + AM_PM);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		int i = currentDate.compareTo(dateToBeTested);
		if (i < 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean hasNotPassed(String date) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		int month = (today.get(Calendar.MONTH) + 1);
		int day = today.get(Calendar.DAY_OF_MONTH);
		Date currentDate = null;
		Date dateToBeTested = null;
		try {
			dateToBeTested = formatter.parse(date);
			currentDate = formatter.parse(year + "-" + month + "-" + day);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		int i = currentDate.compareTo(dateToBeTested);
		if (i > 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isStartTimeAndEndTimeInOrder(String startDate,
			String startTime, String endDate, String endTime) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		Date startDateTime = null;
		Date endDateTime = null;
		try {
			startDateTime = formatter.parse(startDate + " " + startTime);
			endDateTime = formatter.parse(endDate + " " + endTime);
		} catch (ParseException e) {
			
			throw new RuntimeException(e);
		}
		int i = startDateTime.compareTo(endDateTime);
		if (i < 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isValidLocation(String place) {
		return (!(place.trim().isEmpty()));
	}
}
