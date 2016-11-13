import java.util.ArrayList;

/**
 * This class is used to parse a long String of commands.
 * @author Pawel Forfa
 * @version 1.0
 */
public class InputParser {

	/**
	 * Empty constructor to initialize the parser.
	 */
	public InputParser(){						
	}

	/**
	 * Parser function that tokenizes commands based on semicolons, and further splits it based on white space.
	 * @param unparsedInput A long String supplied by the user or batch file that contains untokenized commands.
	 * @return Returns an ArrayList of String Arrays that contain all the tokenized commands for a ProcessBuilder.
	 */
	public ArrayList<String[]> parser(String unparsedInput){

		if(unparsedInput.length() > 512){
			throw new IllegalArgumentException("Command overflow. Exceeded 512 characters.");
		}
		else{
			ArrayList<String[]> commandSet = new ArrayList<String[]>();

			//Split the whole user input string into multiple strings based on ;
			//Also gets rid of preceding and trailing white spaces.
			String[] splitCommandSet = unparsedInput.trim().split("\\;+");

			//Split each of the previously created strings based on white space characters.
			//This section prepares the strings to become parameters for ProcessBuilder.
			for(String singleUnparsedCommand : splitCommandSet){
				//Do not create a String entry if there are only white space (referring to "\\s+", -1)
				String[] singleParsedCommand = singleUnparsedCommand.trim().split("\\s+", -1);
				if(!singleParsedCommand.equals(null)){
					commandSet.add(singleParsedCommand);
				}
			}

			/*
			 * Debugging portion for testing how the unparsed String got partitioned.
			 * 
			System.out.println("================");
			for(String[] partitionedBySemi : commandSet ){
				System.out.println("Single command start!");
				for(String wordInCommand : partitionedBySemi){
					System.out.println("\"" + wordInCommand + "\"");
				}
				System.out.println("Single command end!");
			}
			System.out.println("================");
			System.out.println(commandSet.size() + " commands should be executed.");
			 */

			return commandSet;
		}
	}	

}
