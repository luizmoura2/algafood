package com.algaworks.algafood.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regexteste {

	public static void main(String[] args) {
		String text    =
		        "This is the text which is to be searched " +
		        "for occurrences of the word 'is' Hi5 HiD Hi*.";

		String regex = "is";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);

		int count = 0;
		/*while(matcher.find()) {
		    count++;
		    System.out.println("found is: " + count + " : "
		            + matcher.start() + " - " + matcher.end());
		}


		regex = "Hi\\d";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(text);
		while(matcher.find()) {
		    count++;
		    System.out.println("found Hi\\d Hi5: " + count + " : "
		            + matcher.start() + " - " + matcher.end());
		}
		
		String regexD = "Hi\\D";
		pattern = Pattern.compile(regexD);
		matcher = pattern.matcher(text);
		while(matcher.find()) {
		    count++;
		    System.out.println("found Hi\\D HiD, Hi*: " + count + " : "
		            + matcher.start() + " - " + matcher.end());
		}
		
		String regexw = "Hi\\w";
		pattern = Pattern.compile(regexw);
		matcher = pattern.matcher(text);
		while(matcher.find()) {
		    count++;
		    System.out.println("found Hi\\w Hi5, HiD: " + count + " : "
		            + matcher.start() + " - " + matcher.end());
		}
		
		String regexW = "Hi\\W";
		pattern = Pattern.compile(regexW);
		matcher = pattern.matcher(text);
		while(matcher.find()) {
		    count++;
		    System.out.println("found Hi\\W Hi*, HiD: " + count + " : "
		            + matcher.start() + " - " + matcher.end());
		}
		
		text = "Line 1\nLine2\nLine3";
		pattern = Pattern.compile("^");
		matcher = pattern.matcher(text);
		while(matcher.find()){
		    System.out.println("Found match at ^: "  + matcher.start() + " to " + matcher.end());
		}
		
		text = "http://jenkov.com http://jenkov2.com";
		pattern = Pattern.compile("^http://");
		matcher = pattern.matcher(text);
		while(matcher.find()){
		    System.out.println("Found match at ^http:// : "  + matcher.start() + " to " + matcher.end()+matcher.toString());
		}
		
		text = "http://jenkov.com http://jenkov2.com";
		pattern = Pattern.compile(".com$");
		matcher = pattern.matcher(text);
		while(matcher.find()){
		    System.out.println("Found match at .com$: "  + matcher.start() + " to " + matcher.end());
		}
		
		text = "Mary had a little lamb";
		pattern = Pattern.compile("\\b");// \\b Procurar paralavras
		matcher = pattern.matcher(text);
		while(matcher.find()){
		    System.out.println("Found match at  \\b: "  + matcher.start() + " to " + matcher.end());
		}
		
		text = "Mary had a little lamb";
		pattern = Pattern.compile("\\bla");// \\bla procura palavras que começam com 'la'
		matcher = pattern.matcher(text);
		while(matcher.find()){
		    System.out.println("Found match at  \\bla: "  + matcher.start() + " to " + matcher.end());
		}

		text = "Mary had a little lamb";
		 pattern = Pattern.compile("\\B");
		 matcher = pattern.matcher(text);
		while(matcher.find()){
		    System.out.println("Found match at: "  + matcher.start() + " to " + matcher.end());
		}

		text = "Cindarella and Sleeping Beauty sat in a tree";
		 pattern = Pattern.compile("[Cc][Ii].*");
		 matcher = pattern.matcher(text);
		System.out.println("matcher.matches() = " + matcher.matches());

		text = "Cindarella and Sleeping Beauty sat in a tree";
		 pattern = Pattern.compile(".*Ariel.*|.*Sleeping Beauty.*");
		 matcher = pattern.matcher(text);
		System.out.println("matcher.matches() = " + matcher.matches());


		text    =
	            "This is the text to be searched " +
	            "for occurrences of the pattern.";

	        String patternx = ".*is.*";

	        boolean matches = Pattern.matches(patternx, text);

	        System.out.println("matches = " + matches);
		//================================================ 
		text    =
	            "This is the text to be searched " +
	            "for occurrences of the http:// pattern.";

	        String patternString = ".*http://.*";

	        pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

	        matcher = pattern.matcher(text);

	        boolean matches = matcher.matches();

	        System.out.println("matches = " + matches);
	     //===========================================   
		text = "A sep Text sep With sep Many sep Separators";
        
        String patternString = "sep";
        pattern = Pattern.compile(patternString);
        System.out.println("Pattern: "+pattern.pattern());
        String[] split = pattern.split(text);
        
        System.out.println("split.length = " + split.length);
        
        for(String element : split){
            System.out.println("element = " + element);
        }
        //------------------------------------
        
		text    =
                "This is the text to be searched " +
                "for occurrences of the http:// pattern.";

        String patternString = "This is the";

        pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);

        System.out.println("lookingAt = " + matcher.lookingAt());
        System.out.println("matches   = " + matcher.matches());

        //---------------------------------------------------
        text    =
        "John writes about this, and John writes about that," +
                " and John writes about everything. " ;

		String patternString1 = "(John) (.+?) ";
		
		 pattern = Pattern.compile(patternString1);
		 matcher = pattern.matcher(text);
		
		while(matcher.find()) {
		  System.out.println("found1: " + matcher.group(1));
		  System.out.println("found2: " + matcher.group(2));

		}
		//==================================
		
		text    =
                "John writes about this, and John Doe writes about that," +
                        " and John Wayne writes about everything."
              ;

      String patternString1 = "((John) (.+?)) ";

       pattern = Pattern.compile(patternString1);
       matcher = pattern.matcher(text);

      while(matcher.find()) {
          System.out.println("found: <"  + matcher.group(1) +
                             "> <"       + matcher.group(2) +
                             "> <"       + matcher.group(3) + ">");
      }
      //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * /

		
		text    =
                "John writes about this, and John Doe writes about that," +
                        " and John Wayne writes about everything."
              ;

      String patternString1 = "((John) (.+?)) ";

       pattern = Pattern.compile(patternString1);
       matcher = pattern.matcher(text);

      String replaceAll = matcher.replaceAll("Joe Blocks ");
      System.out.println("replaceAll   = " + replaceAll);
      / *replaceAll   = Joe Blocks about this, and Joe Blocks writes about that,
       *  and Joe Blocks writes about everything.
       * /

      String replaceFirst = matcher.replaceFirst("Joe Blocks ");
      System.out.println("replaceFirst = " + replaceFirst);
      / *replaceFirst = Joe Blocks about this, and John Doe writes about that, 
       * and John Wayne writes about everything.
       */
	      text    =
	              "John writes about this, and John Doe writes about that," +
	                      " and John Wayne writes about everything."
	            ;

	    String patternString1 = "((John) (.+?)) ";
	
	    pattern      = Pattern.compile(patternString1);
	    matcher      = pattern.matcher(text);
	    StringBuffer stringBuffer = new StringBuffer();
	
	    while(matcher.find()){
	        matcher.appendReplacement(stringBuffer, "Joe Blocks ");
	        System.out.println(stringBuffer.toString());
	    }
	    matcher.appendTail(stringBuffer);
	
	    System.out.println(stringBuffer.toString());


	}
}
