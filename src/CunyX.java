import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is an implementation of Linux Operating System shell.
 * It spawns processes on the user's input or from a batch file, if 
 * one is specified as an argument. 
 *
 * @author Pawel Forfa
 * @version 1.0
 */

public class CunyX{

	public static void main(String[] args) throws IOException, InterruptedException {

		/*	
		 * If more than one argument (file) was passed in, then exit.
		 * Program currently does not support multiple batch files.
		 */
		if(args.length > 1){
			System.err.println("There is only support for running the program with one argument.");
			System.exit(0);
		}			

		//Entering Batch Mode
		else if(args.length == 1){
			System.out.println("Executing Batch File.");
			try{				
				Scanner scanner = new Scanner( new File(args[0]));
				String textFileCommands = scanner.useDelimiter("\\A").next();

				InputParser commandParser = new InputParser();				
				ArrayList<String[]> commands = new ArrayList<String[]>();
				commands = commandParser.parser(textFileCommands);	

				boolean checkForQuit = false;
				
				//Check if the list of commands was completely empty
				if(!commands.isEmpty()){
					for(String[] commandForProcessBuilder : commands){
						System.out.println();
						checkForQuit = runProcess(commandForProcessBuilder);
						
						if(checkForQuit){
							System.out.println("quit");
							System.out.println("Requested program termination from 'quit' command. Goodbye.");
							System.exit(0);
						}
					}
				}
				scanner.close();
			}
			catch(FileNotFoundException fnfe){
				System.err.println("File does not exist. Re-run the program with a valid file.");
			}
			catch(IllegalArgumentException ill){
				System.err.println("Supplied commands exceeds maximum character limit. Partition your commands into smaller segments.");
			}
		}//Batch Mode Ends

		//Entering Interactive Mode since a file has not been specified
		else{

			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Enter CunyX Shell commands. Type help for examples.");
			//Get's the local machine's user name and posts it as the prompt value.
			String sessionUser = "fopa3932";

			//Infinite While Loop that Waits for User's input
			while(true){				
				try{
					Boolean requestedProgramTermination = false;
					System.out.print(sessionUser + "> ");
					String userString = userInput.readLine();

					if(userString.toLowerCase().equals("help")){
						System.out.println("You requested help. \r\n\r\n"
								+ "The following commands can be on the CunyX Shell:\r\n"
								+ "ls pwd grep etc...\r\n"
								+ "The commands can be chained in any order, and separated by semicolons (;).\r\n"
								+ "Exit this interactive mode by typing quit.");
					}
					//If provided input to the console was empty
					else if(userString.isEmpty()){
						System.out.print("");
					}
					else{						
						InputParser commandParser = new InputParser();
						ArrayList<String[]> commands = new ArrayList<String[]>();
						commands = commandParser.parser(userString);

						//Check if the list of commands was completely empty
						if(!commands.isEmpty()){
							for(String[] commandForProcessBuilder : commands){
								Boolean checkForQuit = runProcess(commandForProcessBuilder);
								if(checkForQuit){
									System.out.println("quit");
									System.out.println("Requested program termination from 'quit' command. Goodbye.");
									System.exit(0);
								}
							}
						}
					}
				}
				catch(IllegalArgumentException ill){
					System.err.println("Supplied commands exceeds maximum character limit. Partition your commands into smaller segments.");
					System.out.print("");
					//					System.out.print(sessionUser + "> ");
				}
				catch(NullPointerException e){
					System.out.println("You have pressed \'Ctrl + D\'. Program terminating, goodbye.");
					userInput.close();
					System.exit(0);
				}
				catch(Exception e){
					System.out.println("General exception has occured. Exiting program.");
					System.exit(0);
				}
			}//end of Infinite While Loop that waits for User Input
		}//end of Interactive Mode	
	}


	/**
	 * A validation method for ProcessBuilder. Checks whether a commandSet will throw an error if it were to be passed into the ProcessBuilder.
	 *
	 * @param commandSet String Array with a command and possible arguments to it.
	 * @return Returns false if any of the Strings in commandSet are null or contain one or more spaces.
	 *  	   Returns true if the commandSet is free of basic syntax errors.
	 */
	private static Boolean isValidCommand(String[] commandSet){
		/* 
		 * Checks if any string in the string[] being passed in is white space or empty
		 * due to possible hit and miss in the InputParser 
		 * (e.g. ';   ;' shouldn't execute and should not throw an error)
		 * It would simply be discarded as a blank command and not be ran through process builder.
		 * A warning will also be printed to a user that a blank command was found.
		 */
		for(String singleCommandPart : commandSet){
			if(singleCommandPart.equals("") || singleCommandPart.contains("\\s")){
				System.out.println("Found a blank command. Not executing.");				
				return false;
			}
		}
		System.out.println();
		return true;
	}

	/**
	 * This function invokes the isValidCommand function to check whether it should run a process or not.
	 * If the commandSet is a valid, then it passes the commandSet to ProcessBuilder. ProcessBuilder may 
	 * throw IOException if the command passed cannot be executed by the Operating System.
	 * 
	 * @param commandSet String Array with a command and possible arguments to it.
	 * @return Returns true if the String 'quit' was found anywhere in the commandSet.
	 * 		   Returns false if the String 'quit' was not found.
	 */
	private static Boolean runProcess(String[] commandSet){
		/*
		 * Check whether it is worth invoking the processBuilder
		 * Early check for commands that included multiple ;;;		
		 * The parser would produce empty strings, therefore		
		 * causing processBuilder to throw an error.
		 * 
		 * The rest of the commands need to be executed, 
		 * although invalid commands/character may've been entered.
		 */
		if(isValidCommand(commandSet)){
			ProcessBuilder pb = new ProcessBuilder(commandSet);
			try {

				/*
				 * Check if the length of the commands is one
				 * If so then it means that the command "quit" was called"
				 * Return true from this method without building this process.
				 * The return value of true will signify whether to proceed with quitting
				 * the program after the rest of the commands finish executing.
				 */
				for(String checkForQuitRequest : commandSet){
					
					if(checkForQuitRequest.equals("quit")){
						return true;
					}else{
						System.out.print(checkForQuitRequest + " ");
					}
				}
				System.out.println();

				Process process = pb.start();

				// obtain the input and output streams
				InputStream is = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);

				String line;
				while ( (line = br.readLine()) != null){
					System.out.println(line);
				}	
				br.close();
			} 
			catch(IOException e){
				System.out.println("Command not found. Please select a valid option.");
			}
			//Flush the buffer of any remnants.
			//Returns false because the quit command was not called.
			return false;
		}
		/*
		 * Else simply don't do anything, so don't run the process 
		 * if any part of the string[] passed in was empty.
		 * 
		 * Also return false because the program did not execute the quit command,
		 * this is logical by default since nothing was passed in.
		 */
		return false;
	}//end of runProcess

}
